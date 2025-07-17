package com.diakonovtomer.projektObjektorientierung.model.map;

import com.diakonovtomer.projektObjektorientierung.Config;
import java.io.*;
import com.google.gson.*;          // +  Gson  (add dependency)
import java.util.List;

/**
 * Generates a tile map for the game, either from an external Rust-based
 * executable (returning JSON), or via fallback hardcoded map.
 * <p>
 * Supports dynamic loading of map layout by executing a Rust binary specified
 * in the configuration under {@code EXTERNAL_MAPGENERATOR_EXE}. If the execution
 * fails or returns invalid data, a default hardcoded map is used.
 * </p>
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Generate map data as 2D array of {@link TileType}</li>
 *   <li>Convert JSON output from Rust into internal representation</li>
 *   <li>Provide fallback map if external generation fails</li>
 * </ul>
 *
 * Example usage:
 * <pre>{@code
 * MapGenerator generator = new MapGenerator(80, 80);
 * TileType[][] map = generator.generate();
 * }</pre>
 * 
 * @author adiakonov
 */
public class MapGenerator {

    private final int width;
    private final int height;
            
    /** The map as a 2D array of tile types */
    private TileType[][] map;

    /**
     * Constructs a new generator with desired map dimensions.
     *
     * @param width  map width in tiles
     * @param height map height in tiles
     */
    public MapGenerator(int width, int height) {
        this.width  = width;
        this.height = height;
        this.map    = new TileType[height][width];
    }
    
    /**
     * Generates the map layout using an external Rust executable if available,
     * or falls back to a hardcoded demo map.
     *
     * @return 2D array of {@code TileType} representing the game world
     */
    public TileType[][] generate() {
        String exe = Config.get("EXTERNAL_MAPGENERATOR_EXE");
        if (loadFromExe(exe)) {
            return map;
        }

        // Fallback map (for debug or offline mode)
        map = new TileType[][] {
            {TileType.WALL,   TileType.WALL,   TileType.WALL,   TileType.WALL,   TileType.WALL,  TileType.WALL,   TileType.WALL,   TileType.WALL,   TileType.WALL,   TileType.WALL  },
            {TileType.WALL,   TileType.PLAYER, TileType.FLOOR,  TileType.WALL,   TileType.FLOOR, TileType.FLOOR,   TileType.FLOOR,  TileType.FLOOR,  TileType.FLOOR,  TileType.WALL   },
            {TileType.WALL,   TileType.FLOOR,  TileType.FLOOR,  TileType.WALL,   TileType.FLOOR, TileType.FLOOR ,  TileType.FLOOR,  TileType.FLOOR,  TileType.FLOOR,  TileType.WALL   },
            {TileType.WALL,   TileType.FLOOR,  TileType.FLOOR,  TileType.DOOR,   TileType.FLOOR, TileType.FLOOR,  TileType.FLOOR,  TileType.FLOOR,  TileType.FLOOR,  TileType.WALL   },
            {TileType.WALL,   TileType.FLOOR,  TileType.FLOOR,  TileType.WALL,   TileType.FLOOR, TileType.FLOOR,  TileType.FLOOR,  TileType.FLOOR,  TileType.FLOOR,  TileType.WALL   },
            {TileType.WALL,   TileType.WALL,   TileType.FLOOR,  TileType.WALL,   TileType.FLOOR, TileType.WALL,   TileType.WALL,   TileType.WALL,   TileType.WALL,   TileType.WALL   },
            {TileType.IMPASS, TileType.WALL,   TileType.FLOOR,  TileType.WALL,   TileType.FLOOR, TileType.FLOOR,  TileType.FLOOR,  TileType.WALL,   TileType.IMPASS, TileType.IMPASS},
            {TileType.IMPASS, TileType.WALL,   TileType.FLOOR,  TileType.WALL,   TileType.FLOOR, TileType.FLOOR,  TileType.FLOOR,  TileType.WALL,   TileType.IMPASS, TileType.IMPASS},
            {TileType.IMPASS, TileType.WALL,   TileType.WALL,   TileType.WALL,   TileType.WALL,  TileType.WALL,   TileType.FLOOR,  TileType.WALL,   TileType.IMPASS, TileType.IMPASS},
            {TileType.IMPASS, TileType.IMPASS, TileType.IMPASS, TileType.IMPASS, TileType.IMPASS, TileType.WALL,   TileType.WALL,   TileType.WALL,   TileType.IMPASS, TileType.IMPASS}            
        };
        return map;
    }

    /**
     * Attempts to execute the external Rust-based generator and parse the result.
     *
     * @param exePath full path to the Rust executable
     * @return {@code true} if map was successfully generated from the external tool
     */
    private boolean loadFromExe(String exePath) {

        try {
            ProcessBuilder pb = new ProcessBuilder(exePath);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            // читаем stdout целиком
            StringBuilder jsonBuf = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()))) {

                String line;
                while ((line = br.readLine()) != null) {
                    jsonBuf.append(line);
                }
            }

            if (p.waitFor() != 0) {
                System.err.println("mapGenerator.exe ended with code " + p.exitValue());
                return false;
            }

            parseJson(jsonBuf.toString());
            return true;

        } catch (IOException | InterruptedException | JsonParseException e) {
            System.err.println("Failed to load map from exe: " + e.getMessage());
            return false;
        }
    }

    /**
     * Parses the given JSON string returned by Rust and builds the map grid.
     *
     * @param json JSON string containing width, height, and a grid array
     */
    private void parseJson(String json) {

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        int w = root.get("width").getAsInt();
        int h = root.get("height").getAsInt();

        // если Rust‑карта не совпадает по размеру с текущим генератором –
        // просто берём её размеры
        this.map = new TileType[h][w];

        /* ----- grid ----- */
        List<JsonElement> rows = root.getAsJsonArray("grid").asList();
        for (int y = 0; y < h; y++) {
            String line = rows.get(y).getAsString();
            for (int x = 0; x < w; x++) {
                char c = line.charAt(x);
                map[y][x] = charToTile(c);
            }
        }
    }

    /**
     * Translates a single character from the Rust map format into a {@code TileType}.
     *
     * @param c character representing a tile
     * @return matching {@code TileType}, or FLOOR by default
     */
    private TileType charToTile(char c) {
        return switch (c) {
            case '0' -> TileType.IMPASS;
            case '.' -> TileType.FLOOR;
            case '#' -> TileType.WALL;
            case '"' -> TileType.GRATE;
            case '*' -> TileType.DOOR;
            case '~' -> TileType.DOORGRATE;
            case '-' -> TileType.ARCH;
            case '+' -> TileType.ARCHGRATE;
            case 'A' -> TileType.CORNERITL;
            case 'B' -> TileType.CORNERITR;
            case 'C' -> TileType.CORNERIBL;
            case 'D' -> TileType.CORNERIBR;
            case 'E' -> TileType.CORNEROTL;
            case 'F' -> TileType.CORNEROTR;
            case 'G' -> TileType.CORNEROBL;
            case 'H' -> TileType.CORNEROBR;
            //case 'S' -> TileType.ENTRANCE;
            case 'S' -> TileType.PLAYER;
            case 'X' -> TileType.EXIT;
            default   -> TileType.FLOOR;      // fallback
        };
    }

    /**
     * Prints the current map to stdout in a simple text format.
     * Mainly for debugging purposes.
     */
    public void printMap() { for (TileType[] row : map) { for (TileType t : row) { System.out.print(t + " "); } System.out.println(); } }
}
