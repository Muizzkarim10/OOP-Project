package flappybird;

import java.io.*;
import java.util.*;

public class FileHandling {
    public void saveScore(String playerName, int score) {
        try {
            File file = new File("src/flappybird/scores.txt");
            List<String> lines = new ArrayList<>();
            boolean updated = false;

            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(playerName + ":")) {
                        lines.add(playerName + ": " + score); // Replace old score
                        updated = true;
                    } else {
                        lines.add(line); // Keep others
                    }
                }
                reader.close();
            }

            if (!updated) {
                lines.add(playerName + ": " + score); // Add new player
            }

            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for (String line : lines) {
                writer.println(line);
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving score: " + playerName);
        }
    }

    public int getHighScore() {
        int max = 0;
        try {
            File file = new File("src/flappybird/scores.txt");
            if (!file.exists()) return 0;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    int score = Integer.parseInt(parts[1].trim());
                    if (score > max) {
                        max = score;
                    }
                }
            }
            reader.close();
        } catch (IOException | NumberFormatException ignored) {
        }
        return max;
    }
}
