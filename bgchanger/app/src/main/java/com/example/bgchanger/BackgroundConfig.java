package com.example.bgchanger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BackgroundConfig {

    private final String colorGrade;
    private final String effect;
    private final String music;
    private final int progression;
    private final String loading;
    private final String torch;  

    public BackgroundConfig(String colorGrade, String effect, String music, int progression, String loading, String torch) {
        this.colorGrade = colorGrade;
        this.effect = effect;
        this.music = music;
        this.progression = progression;
        this.loading = loading;
        this.torch = torch;
    }

    public String getColorGrade() {
        return colorGrade;
    }

    public String getEffect() {
        return effect;
    }

    public String getMusic() {
        return music;
    }

    public int getProgression() {
        return progression;
    }

    public String getLoading() {
        return loading;
    }

    public String getTorch() {
        return torch;
    }

    /**
     * Reads the backgrounds config file and returns the config for the specified selectedImage.
     * Lines starting with '#' or empty lines are skipped.
     *
     * @param configFilePath Path to the backgrounds config file.
     * @param selectedImage The background image filename to look for.
     * @return BackgroundConfig object for the image, or null if not found.
     */
    public static BackgroundConfig readBackgroundConfig(Path configFilePath, String selectedImage) {
        try (BufferedReader reader = Files.newBufferedReader(configFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Check if the line starts with the selected image filename + " ="
                if (line.startsWith(selectedImage + " =")) {
                    // Example line format:
                    // crypts.entrance_room_wall.png = colorGrade = crypts, effect = none, music = ruins, progression = 0, loading = default, torch = default

                    // Remove the filename and " = " part to get the rest
                    String configPart = line.substring((selectedImage + " = ").length()).trim();

                    String[] keyValues = configPart.split(",");

                    // Default values (loading and torch obbligatori, quindi non null)
                    String colorGrade = "default";
                    String effect = "none";
                    String music = "default";
                    int progression = 0;
                    String loading = "default";
                    String torch = "default";

                    for (String kv : keyValues) {
                        String[] parts = kv.trim().split("=");
                        if (parts.length == 2) {
                            String key = parts[0].trim().toLowerCase();
                            String value = parts[1].trim();

                            switch (key) {
                                case "colorgrade":
                                    colorGrade = value;
                                    break;
                                case "effect":
                                    effect = value;
                                    break;
                                case "music":
                                    music = value;
                                    break;
                                case "progression":
                                    try {
                                        progression = Integer.parseInt(value);
                                    } catch (NumberFormatException e) {
                                        progression = 0; // default fallback
                                    }
                                    break;
                                case "loading":
                                    loading = value;
                                    break;
                                case "torch":
                                    torch = value;
                                    break;
                            }
                        }
                    }

                    return new BackgroundConfig(colorGrade, effect, music, progression, loading, torch);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        }

        System.err.println("Config for " + selectedImage + " not found.");
        return null;
    }
}
