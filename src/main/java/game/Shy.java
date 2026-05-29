package game;

import javafx.scene.paint.Color;

// Shy — runs away unless cornered (few open neighbors), then attacks.
public class Shy extends Ghost {

    public Shy(GameMap map) {
        super(map, GameMap.Tile.SPAWN_G2, 1.5);
    }

    @Override
    public String getName() { return "Shy"; } // give your ghost a name

    @Override
    protected int[] chooseTarget(Player player, GameMap map) {
        // TODO (Base): Implement Shy's personality.
        //
        if (frightened)
        {
            return new int[]{ player.col(map), player.row(map)}; // shy chases the player when frightened
        }
        else
        {
            int sc = this.col(map);
            int sr = this.row(map);
            int open = 0;
            if (!map.isWall(sc + 1, sr)) // this. checks if the ghosts exact position has walls open on each side
            {
                open++;
            }
            if (!map.isWall(sc - 1, sr))
            {
                open++;
            }
            if (!map.isWall(sc, sr + 1))
            {
                open++;
            }
            if (!map.isWall(sc, sr - 1))
            {
                open++;
            }
            if (open <= 1)
            {
                return new int[]{ player.col(map), player.row(map)};
            }
            else
            {
                int pc = player.col(map);
                int pr = player.row(map);
                // corners are at 1, 1 / 1, map.rows -2 / map.cols -2, 1 / and map.cols - 2, map.rows - 2
                double dist1 = Math.hypot(1 - pc, 1 - pr);
                double dist2 = Math.hypot((map.cols - 2) - pc, 1 - pr);
                double dist3 = Math.hypot(1 - pc, (map.rows - 2) - pr);
                double dist4 = Math.hypot((map.cols - 2) - pc, (map.rows - 2) - pr);
                double largestDist = 0;
                int targetCol = 1;
                int targetRow = 1;
                if (dist1 > largestDist) // each of these checks whether the distance to each corner is larger than the previous largest
                {
                    largestDist = dist1;
                    targetCol = 1;
                    targetRow = 1;
                }
                if (dist2 > largestDist)
                {
                    largestDist = dist2;
                    targetCol = 1;
                    targetRow = map.rows - 2;
                }
                if (dist3 > largestDist)
                {
                    largestDist = dist3;
                    targetCol = map.cols - 2;
                    targetRow = 1;
                }
                if (dist4 > largestDist)
                {
                    largestDist = dist4; // largestDist just helps with the tracking of distance, doesn't actually get used
                    targetCol = map.cols - 2;
                    targetRow = map.rows - 2;
                }
                return new int[]{ targetCol, targetRow }; // target column and row are set to whatever was largest and returned
            }
        }
        // Shy has two modes:
        //   1. FLEE   — target the maze corner FARTHEST from the player
        //   2. ATTACK — target the player's exact tile (like Shadow)
        //
        // Shy switches from FLEE to ATTACK when it is "cornered":
        //   Count how many of Shy's four neighbors (up/down/left/right) are open
        //   (not walls). If only ONE neighbor is open, Shy is cornered.
        //   Use col(map) and row(map) for Shy's position, and map.isWall() to test each neighbor.
        //
        // To find the farthest corner from the player:
        //   Check each of the four near-corner tiles and pick the one with the
        //   greatest Math.hypot distance from the player's tile.
        //
        // When frightened, CHASE the player instead of fleeing.
        // Note: this is intentionally the OPPOSITE of every other ghost's frightened
        // behavior — Shy is bold when cornered and bold when scared.
        //
        // How to verify: run the game and walk toward Shy — it should move away
        // from you. Trap it in a dead-end corridor and it should turn and chase.
    }

    // When chooseTarget() is working, add this ghost to the list in GameApp.java:
    //   new Shy(map)

    @Override
    protected Color getBodyColor() { return Color.web("#00ffff"); }
}
