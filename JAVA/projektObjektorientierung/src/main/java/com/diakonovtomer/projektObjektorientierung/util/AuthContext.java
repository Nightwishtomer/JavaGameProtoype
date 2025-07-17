package com.diakonovtomer.projektObjektorientierung.util;

/**
 * The {@code AuthContext} class acts as a static authentication context
 * for temporarily storing the current user's login credentials and game token
 * during the runtime of the application.
 * <p>
 * This is primarily used to simplify access to the currently authenticated user
 * across different parts of the game (e.g., when sending HTTP requests).
 * </p>
 * 
 * <p><strong>⚠️ Security Note:</strong> This class stores the password in plain text in memory.
 * This is acceptable for educational or local use but should be avoided in production environments.</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * AuthContext.set("player1", "password123", "jwt.token.here");
 * if (AuthContext.hasToken()) {
 *     String user = AuthContext.getUser();
 *     String token = AuthContext.getToken();
 *     // Use token for API calls
 * }
 * }</pre>
 * 
 * This class is not instantiable.
 * 
 * @author adiakonov
 */
public final class AuthContext {
    private static String userName;
    private static String password;
    private static String gameToken;
    
    /**
     * Sets the authentication context with the given user credentials and token.
     *
     * @param name   the username of the authenticated user
     * @param pass   the password (in plain text)
     * @param token  the JWT or session token used for authenticated API requests
     * @throws RuntimeException if any unexpected error occurs
     */
    public static void set(String name, String pass, String token) {
        try {
            userName = name;
            password = pass;
            gameToken = token;
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting username or password", e);
        }
    }

    /**
     * Returns the currently stored username.
     *
     * @return the username, or {@code null} if not set
     */
    public static String getUser() {
        return userName;
    }

    /**
     * Returns the currently stored password.
     *
     * @return the password, or {@code null} if not set
     */
    public static String getPass() {
        return password;
    }
    
    /**
     * Returns the authentication token (JWT).
     *
     * @return the token string, or {@code null} if not set
     */
    public static String getToken() {
        return gameToken;
    }
    
    /**
     * Checks if a valid token is currently stored.
     *
     * @return {@code true} if a token is available and not blank, {@code false} otherwise
     */
    public static boolean hasToken() {
        return gameToken != null && !gameToken.isBlank();
    }    
}
