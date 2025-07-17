package com.diakonovtomer.projektObjektorientierung.network;

import com.diakonovtomer.projektObjektorientierung.Config;
import com.diakonovtomer.projektObjektorientierung.game.SaveGame;
import com.diakonovtomer.projektObjektorientierung.util.AuthContext;
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * API Service – communicates with the remote backend.
 * Provides methods for login, registration, saving/loading game data, etc.
 * Uses Java 11+ HttpClient and works asynchronously via CompletableFuture.
 *
 * All JSON parsing/serialization is handled using Gson.
 */
public final class ApiService {

    private static final String BASE = Config.get("SERVER_URL"); // <‑‑‑
    private static final Gson   GSON = new Gson();
    private static final HttpClient HTTP = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();
    private static String jwt; // stores the JWT token after login

    /* -------------------- HTTP helpers -------------------- */

    /**
     * Creates a basic HTTP request builder with correct headers.
     */
    private static HttpRequest.Builder req(String path) {
        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(BASE + path))
                .header("Content-Type", "application/json");
        if (jwt != null)
            b.header("Authorization", "Bearer " + jwt);
        return b;
    }
  
    /**
     * Builds a POST request with JSON body.
     * Uses token from AuthContext.
     */
    private static <T> HttpRequest jsonPost(String path, T body) {
        String json = GSON.toJson(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(BASE + path))
            .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
            .header("Content-Type", "application/json");
        String token = AuthContext.getToken();   // брать из AuthContext
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + jwt);
        }
        return builder.build();
    }
    
    /**
     * Sends an asynchronous request and parses the result to given class.
     */
    private static <T> CompletableFuture<T> send(HttpRequest r, Class<T> t) {
        return HTTP.sendAsync(r, HttpResponse.BodyHandlers.ofString())
            .thenApply(resp -> {
                System.out.println("RAW JSON = " + resp.body());   // ➜ глянь вывод
                int c = resp.statusCode();
                if (c/100 != 2)
                    throw new RuntimeException( "HTTP "+c+": "+resp.body() );
                return GSON.fromJson(resp.body(), t);
            }
        );
    }

    /* -------------------- API calls -------------------- */

    /**
     * Registers a new user.
     */
    public static CompletableFuture<Void> register(String u, String p) {
        return send(jsonPost(Config.get("API_URL_REGISTER"), Map.of("username",u,"password",p)), Map.class).thenAccept(x -> {});
    }
    
    /**
     * Logs in and stores the JWT token.
     */
    public static CompletableFuture<String> login(String u, String p) {
        return send(jsonPost(Config.get("API_URL_LOGIN"), Map.of("username",u,"password",p)), LoginResp.class)
           .thenApply(r -> { 
               jwt = r.token;
               return r.token;
           }
        );
    }
    
    /**
     * Alternative login/auth endpoint. Also stores JWT token.
     */
    public static CompletableFuture<String> auth(String u, String p) {
        return send(jsonPost(Config.get("API_URL_AUTH"), Map.of("username",u,"password",p)), LoginResp.class)
           .thenApply(r -> {
               jwt = r.token;
               return r.token;
           }
        );
    }
    
    /**
     * Sends save data to the backend.
     */
    public static CompletableFuture<String> save(SaveGame data) {
        HttpRequest req = jsonPost(Config.get("API_URL_SAVE"), data);
        return HTTP.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenApply(resp -> {
            int code = resp.statusCode();
            if (code / 100 != 2)
                throw new RuntimeException( "HTTP " + code + ": " + resp.body());
            return resp.body(); // server response
        });
    }
    
    /**
     * Loads the most recent save for the logged-in user.
     */
    public static CompletableFuture<LoadResp> load(){
        HttpRequest r = req(Config.get("API_URL_LOAD")).GET().build();
        return send(r, LoadResp.class);
    }
    
    /**
     * Loads list of all saved game states for current user.
     */
    public static CompletableFuture<LoadResp[]> loadList(){
        HttpRequest r = req(Config.get("API_URL_LOADLIST")).GET().build();
        return send(r, LoadResp[].class);  // 👈 массив LoadResp
    }
    
    /**
     * Loads a specific save entry by ID.
     */
    public static CompletableFuture<LoadResp> loadDataById(int id){
        String path = Config.get("API_URL_LOADDATABYID") + id;
        HttpRequest r = req(path).GET().build();
        return send(r, LoadResp.class);
    }

    /* -------------------- DTO classes for JSON -------------------- */

    /** DTO class for login response */
    private static class LoginResp { String token; }
    
    /** DTO class for loading saved game state */
    public static  class LoadResp  {
        public int id;
        public int map_level;
        public String ascii_map;
        public Map<String,?> chr;
        public String updated;
        public int positionTileX	;
        public int positionTileY;
    }
    
    /**
     * Gets current JWT token (can be used in UI or logs).
     */
    public static String getJwt(){
        return jwt;
    }
}
