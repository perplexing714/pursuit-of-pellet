package game;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreTree {

    private ScoreNode root;

    public void insert(int score, int level) {
        root = insert(root, score, level);
    }

    private ScoreNode insert(ScoreNode node, int score, int level) {
        // TODO (Phase 4): Insert a new score into the BST recursively.
        //
        // Follow the insert() pattern from the Phase 4 guide, adapting it to
        // pass the level parameter when constructing a new ScoreNode.
        if (node == null)
        {
            return new ScoreNode(score, level);
        }
        if (score < node.score)
        {
            node.left = insert(node.left, score, level);
        }
        else {
            node.right = insert(node.right, score, level);
        }
        return node;
    }

    // Reverse in-order (right → node → left) yields descending order
    public List<ScoreNode> getTopScores(int n) {
        List<ScoreNode> result = new ArrayList<>();
        collectDescending(root, result, n);
        return result;
    }

    private void collectDescending(ScoreNode node, List<ScoreNode> result, int n) {
        // TODO (Phase 4): Collect the top n scores in descending order.
        //
        // The Phase 4 guide shows printInOrder() which visits left → node → right.
        // Adapt that pattern to visit right → node → left instead (larger scores
        // live in the right subtree), and stop once result has n items.
        if (node == null || result.size() >= n)
        {
            return;
        }
        collectDescending(node.right, result, n);
        if (result.size() < n)
        {
            result.add(node);
        }
        collectDescending(node.left, result, n);
    }

    public boolean isEmpty() {
        return root == null;
    }

    // -----------------------------------------------------------------------
    // Persistence — scores survive between sessions via a plain text file.
    // Each line: "<score> <level>"
    // -----------------------------------------------------------------------

    public void saveToFile(Path path) {
        // TODO (Phase 3): Write all scores to the file, one per line.
        // 1. Get the formatted lines by calling collectInOrder(root, lines).
        // 2. Write each line using BufferedWriter — see the Phase 3 guide for the pattern.
        List<String> lines = new ArrayList<>();
        collectInOrder(root, lines);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collectInOrder(ScoreNode node, List<String> lines) {
        if (node == null) return;
        collectInOrder(node.left, lines);
        lines.add(node.score + " " + node.level);
        collectInOrder(node.right, lines);
    }

    public void loadFromFile(Path path) {
        // TODO (Phase 3): Read scores back from the file written by saveToFile().
        // 1. Return early if the file does not exist (Files.exists(path)).
        // 2. Read all lines with Files.readAllLines(path) — wrap in try/catch.
        // 3. Use a HashSet to skip duplicate lines — see the Phase 3 guide for
        //    how HashSet works and why add() is useful here.
        // 4. For each unique line, split on " ", parse the two integers,
        //    and call insert().
        if (!Files.exists(path))
        {
            return;
        }
        Set<String> seen = new HashSet<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (seen.add(line))
                {
                    String[] parts = line.split(" ");
                    int score = Integer.parseInt(parts[0]);
                    int level = Integer.parseInt(parts[1]);
                    insert(score, level);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
