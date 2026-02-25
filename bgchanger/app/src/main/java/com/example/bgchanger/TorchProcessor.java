package com.example.bgchanger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

public class TorchProcessor {

    // Map torch type -> base filename for arena_torch
    private static final Map<String, String> TORCH_MAP = Map.ofEntries(
        Map.entry("default", "arena_torch_default"),
        Map.entry("generic", "arena_torch_generic"),
        Map.entry("blood", "arena_torch_blood"),
        Map.entry("comet", "arena_torch_comet"),
        Map.entry("comet_haunting", "arena_torch_comet_haunting"),
        Map.entry("comet_blazing", "arena_torch_comet_blazing"),
        Map.entry("comet_splendorous", "arena_torch_comet_splendorous"),
        Map.entry("comet_gleaming", "arena_torch_comet_gleaming"),
        Map.entry("br-sun", "arena_torch_br-sun"),
        Map.entry("br-moon", "arena_torch_br-moon"),
        Map.entry("br-eclipse", "arena_torch_br-eclipse")
    );

    // Map torch type -> base filename for arena_torch_burndown
    private static final Map<String, String> BURNDOWN_MAP = Map.of(
        "default", "arena_torch_burndown_default",
        "generic", "arena_torch_burndown_generic",
        "blood", "arena_torch_burndown_blood",
        "comet", "arena_torch_burndown_comet",
        "br-sun", "arena_torch_burndown_br-sun",
        "br-moon", "arena_torch_burndown_br-moon",
        "br-eclipse", "arena_torch_burndown_br-eclipse"
    );

    private static final String DEFAULT_TORCH = TORCH_MAP.get("default");

    // The three extensions for torch files
    private static final String[] TORCH_EXTENSIONS = {".sprite.png", ".sprite.atlas", ".sprite.skel"};

    public static void applyTorch(String torchType, Path torchOutputDir, Path indicatorOutputDir) {
        // Copy regular torch files
        copyTorchFiles(torchOutputDir.resolve("arena_torch"), TORCH_MAP, torchType, "arena_torch");
        copyTorchFiles(torchOutputDir.resolve("arena_torch_burndown"), BURNDOWN_MAP, torchType, "arena_torch_burndown");

        // Copy round indicator file
        try {
            if (!Files.exists(indicatorOutputDir)) {
                Files.createDirectories(indicatorOutputDir);
                System.out.println("!| Indicator output directory created at: " + indicatorOutputDir.toAbsolutePath());
            }

            String sourceName = torchType.equals("default")
                    ? "arena.screen.raid.darkest_default"
                    : "arena.screen.raid.darkest_custom";

            Path source = Paths.get("round indicator", sourceName);
            Path dest = indicatorOutputDir.resolve("arena.screen.raid.darkest");

            if (!Files.exists(source)) {
                System.err.println("!|️ Round indicator file not found: " + source.toAbsolutePath());
            } else {
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                App.debugPrint("DEBUG| Round indicator applied: " + dest.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("X|️ Error applying round indicator: " + e.getMessage());
        }
    }

    private static void copyTorchFiles(Path outputDir, Map<String, String> map, String torchType, String fixedOutputName) {
        try {
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
                System.out.println("!| Torch output directory created at: " + outputDir.toAbsolutePath());
            }

            String baseName = map.getOrDefault(torchType, DEFAULT_TORCH);

            for (String ext : TORCH_EXTENSIONS) {
                Path source = Paths.get("torches", baseName + ext);       // source folder
                Path dest = outputDir.resolve(fixedOutputName + ext);     // fixed output filenames

                if (!Files.exists(source)) {
                    System.err.println("!| Torch file not found: " + source.toAbsolutePath());
                    continue;
                }

                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                App.debugPrint("DEBUG| Torch applied: " + dest.toAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("X|️ Error applying torch files: " + e.getMessage());
        }
    }
}
