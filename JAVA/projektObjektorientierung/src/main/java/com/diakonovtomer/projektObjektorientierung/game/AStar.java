package com.diakonovtomer.projektObjektorientierung.game;

import com.diakonovtomer.projektObjektorientierung.model.map.Tile;
import java.util.*;

/**
 * Defines movement directions used in the algorithm:
 * 4-directional (up, down, left, right) or 8-directional (diagonals allowed).
 */
enum DirMode {
    
    /** 4-directional movement (no diagonals). */
    CROSS(false, new int[]{-1,0,1,0}, new int[]{0,1,0,-1}),
    
    /** 8-directional movement (diagonals allowed). */
    OCTILE(true , new int[]{-1,-1,-1,0,1,1,1,0}, new int[]{-1,0,1,1,1,0,-1,-1});

    /** Whether diagonal movement is allowed. */
    final boolean allowDiagonal;
    
    /** Whether cutting corners (diagonal through blocked adjacent tiles) is allowed. */
    final boolean canCutCorners;
    
    /** Row direction offsets. */
    final int[] dr;
    
    /** Column direction offsets. */
    final int[] dc;

    DirMode(boolean corners, int[] dr, int[] dc) {
        this.allowDiagonal = (dr.length == 8);
        this.canCutCorners = corners;
        this.dr = dr;
        this.dc = dc;
    }
}

/**
 * Represents a node in the A* open list.
 *
 * @param r the row index
 * @param c the column index
 * @param g the actual cost from start to this node
 * @param f the estimated total cost (g + h)
 */
record Node(int r, int c, int g, int f) implements Comparable<Node> {
    public int compareTo(Node o) { return Integer.compare(this.f, o.f); }
}

/**
 * Implements the A* (A-star) pathfinding algorithm.
 * <p>
 * Supports both 4-directional (orthogonal) and 8-directional (diagonal) movement,
 * with optional corner-cutting control.
 * </p>
 * The path is calculated over a {@code Tile[][]} grid, where each tile must
 * implement {@link Tile#isPassable()}.
 * 
 * Coordinates are in the form {@code [row][column]} = {@code [y][x]}.
 * 
 * @author Artiem
 */
public class AStar {

    /**
     * Performs A* pathfinding on a grid of {@link Tile}s.
     *
     * @param grid   the grid of tiles
     * @param startY starting row (Y)
     * @param startX starting column (X)
     * @param goalY  goal row (Y)
     * @param goalX  goal column (X)
     * @param mode   movement mode (4- or 8-directional)
     * @return the path as a list of {@code int[] {row, col}} coordinates,
     *         or an empty list if no path was found
     */
    public static List<int[]> findPath(Tile[][] grid, int startY, int startX, int goalY, int goalX, DirMode mode) {
        final int rows = grid.length, cols = grid[0].length;
        int sr = startY, sc = startX;
        int gr = goalY, gc = goalX;

        /* 2. gScore and queue */
        final int INF = Integer.MAX_VALUE/4;
        int[][] gScore = new int[rows][cols];
        for (int[] row: gScore) Arrays.fill(row, INF);
        gScore[sr][sc] = 0;

        PriorityQueue<Node> open = new PriorityQueue<>();
        open.add(new Node(sr, sc, 0, heuristic(sr,sc,gr,gc,mode)));

        int[][] parentR = new int[rows][cols], parentC = new int[rows][cols];
        for (int[] row : parentR) Arrays.fill(row, -1);
        for (int[] row : parentC) Arrays.fill(row, -1);

        /* 3. Main loop */
        while (!open.isEmpty()) {
            Node cur = open.poll();
            if (cur.r()==gr && cur.c()==gc) return rebuildPath(parentR,parentC,gr,gc);

            int[] dr = {-1, 0, 1, 0, -1, -1, 1, 1};
            int[] dc = {0, 1, 0, -1, -1, 1, -1, 1};
            
            for (int i=0; i<8; i++) {
                int nr = cur.r() + dr[i];
                int nc = cur.c() + dc[i];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (!grid[nr][nc].isPassable()) continue;

                boolean diagonal = dr[i] != 0 && dc[i] != 0;

                //prohibit cutting corner if at least one side is blocked
                if (diagonal) {
                    int ortho1R = cur.r();
                    int ortho1C = cur.c() + dc[i];
                    int ortho2R = cur.r() + dr[i];
                    int ortho2C = cur.c();

                    if (!isInside(ortho1R, ortho1C, rows, cols) || !isInside(ortho2R, ortho2C, rows, cols)) continue;
                    if (!grid[ortho1R][ortho1C].isPassable() || !grid[ortho2R][ortho2C].isPassable()) {
                        continue; // diagonal through the corner is prohibited
                    }
                }

                int stepCost = diagonal ? 14 : 10;
                int tentativeG = cur.g() + stepCost;
                if (tentativeG >= gScore[nr][nc]) continue;

                gScore[nr][nc] = tentativeG;
                parentR[nr][nc] = cur.r();
                parentC[nr][nc] = cur.c();
                int f = tentativeG + heuristic(nr, nc, gr, gc, /* use diagonal */ true);
                open.add(new Node(nr, nc, tentativeG, f));
            }
        }
        return List.of(); // path not found
    }
    
    /**
     * Checks if a given coordinate is within grid bounds.
     *
     * @param r row index
     * @param c column index
     * @param rows total number of rows
     * @param cols total number of columns
     * @return true if inside bounds, false otherwise
     */
    private static boolean isInside(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    /**
     * Heuristic cost estimate from current node to goal.
     * Uses Manhattan or Octile distance depending on movement mode.
     *
     * @param r     current row
     * @param c     current column
     * @param gr    goal row
     * @param gc    goal column
     * @param mode  movement mode
     * @return estimated cost
     */
    private static int heuristic(int r,int c,int gr,int gc, DirMode mode) {
        int dx=Math.abs(r-gr), dy=Math.abs(c-gc);
        if (!mode.allowDiagonal) return 10*(dx+dy);
        return 10*(dx+dy) - 6*Math.min(dx,dy);    // Octile ×10
    }

    /**
     * Reconstructs the path from the goal node using parent maps.
     *
     * @param pr parent row references
     * @param pc parent column references
     * @param r goal row
     * @param c goal column
     * @return list of coordinates from start to goal
     */
    private static List<int[]> rebuildPath(int[][] pr,int[][] pc,int r,int c){
        LinkedList<int[]> path=new LinkedList<>();
        while (r!=-1 && c!=-1){
            path.addFirst(new int[]{r,c});
            int nr=pr[r][c], nc=pc[r][c];
            r=nr; c=nc;
        }
        return path;
    }
    
    /**
     * Overloaded heuristic function using direct boolean flag.
     *
     * @param r current row
     * @param c current column
     * @param gr goal row
     * @param gc goal column
     * @param allowDiagonal whether diagonal movement is allowed
     * @return estimated cost
     */
    private static int heuristic(int r,int c,int gr,int gc, boolean allowDiagonal) {
        int dx = Math.abs(r - gr);
        int dy = Math.abs(c - gc);
        if (!allowDiagonal) return 10 * (dx + dy);
        return 10 * (dx + dy) - 6 * Math.min(dx, dy); // Octile ×10
    }
    
    /**
     * Prints the path to console in (row,col) format.
     *
     * @param path the path as a list of coordinates
     */
    public static void printPath(List<int[]> path) {
        if (path.isEmpty()) {
            System.out.println("⛔ Path not found [AStar.printPath]");
            return;
        }
        StringBuilder sb = new StringBuilder("Path: ");
        for (int i = 0; i < path.size(); i++) {
            int[] p = path.get(i);
            sb.append('(').append(p[0]).append(',').append(p[1]).append(')');
            if (i < path.size() - 1) sb.append(" -> ");
        }
        System.out.println(sb);
        System.out.println("[AStar.printPath]");
    }
    
    /**
     * Prints the grid with the path visualized.
     * Passable tiles are displayed as '.', walls as '#', and the path as '*'.
     *
     * @param grid the tile grid
     * @param path the list of path coordinates
     */
    public static void printPathOnGrid(Tile[][] grid, List<int[]> path) {
        for (int r = 0; r < grid.length - 1; r++){
            for (int c = 0; c < grid[0].length - 1; c++){
                String value = grid[r][c].isPassable() ? " . " : " # ";
                for (int[] p : path) if (p[0] == r && p[1] == c) value =  " * ";
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }          
}
