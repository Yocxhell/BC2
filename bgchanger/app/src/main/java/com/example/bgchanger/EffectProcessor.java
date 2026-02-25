package com.example.bgchanger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EffectProcessor {

    private static final Map<String, String> CRYSTALLINE_COMET_MAP = Map.of(
            "crystalline_blazing", "coinflip_comet_blazing",
            "crystalline_haunting", "coinflip_comet_haunting",
            "crystalline_splendorous", "coinflip_comet_splendorous",
            "crystalline_gleaming", "coinflip_comet_gleaming"
    );

    /**
     * Applies the selected effect set to the game folder.
     *
     * @param effectKey "default", "crystalline", or "crystalline_*" variant
     */
    public static void applyEffectByKey(String effectKey) throws IOException {
        if (effectKey == null || effectKey.isBlank()) {
            effectKey = "default";
        }

        Path rootDir = ImageSelector.getRootDir();
        AtomicInteger filesCopied = new AtomicInteger(0);

        // Determine the main effect folder to copy from (default or crystalline)
        String mainEffectFolderKey = effectKey.startsWith("crystalline") ? "crystalline" : effectKey;
        Path mainSource = Paths.get("effects", "effect_sets", mainEffectFolderKey).toAbsolutePath();

        if (!Files.exists(mainSource) || !Files.isDirectory(mainSource)) {
            System.err.println("!| Effect set folder not found: " + mainSource);
            return;
        }

        App.debugPrint("DEBUG| Applying effect set: " + effectKey + " ...\n");
        App.debugPrint("DEBUG| rootDir: " + rootDir + "\nDEBUG| mainSource: " + mainSource);

        // Copy main effect folder recursively
        Files.walkFileTree(mainSource, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                final Path targetDir = rootDir.resolve(mainSource.relativize(dir));
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                    App.debugPrint("DEBUG| Created directory: " + targetDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                final Path targetFile = rootDir.resolve(mainSource.relativize(file));
                try {
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    App.debugPrint("DEBUG| Applied file: " + targetFile);
                    filesCopied.incrementAndGet();
                } catch (IOException e) {
                    System.err.println("!| Failed to apply file: " + file + " -> " + e.getMessage());
                }
                return FileVisitResult.CONTINUE;
            }
        });

        // If effect is a crystalline comet variant, copy from resources folder to coinflip
        // If effect is a crystalline comet variant, copy from resources folder to coinflip
            if (CRYSTALLINE_COMET_MAP.containsKey(effectKey)) {
                String cometFolderName = CRYSTALLINE_COMET_MAP.get(effectKey);
                Path cometSource = Paths.get("effects", "effect_resources", "crystalline_resources", "fx", "coinflip", cometFolderName).toAbsolutePath();
                Path cometDest = rootDir.resolve("dlc/1117860_arena_mp/fx/coinflip");

                copyFolderFiles(cometSource, cometDest, filesCopied);

                App.debugPrint("DEBUG| Crystalline comet override applied: " + effectKey + ". Total files copied: " + filesCopied.get());
            } else {
            App.debugPrint("DEBUG| Effect set applied successfully: " + effectKey + ". Total files copied: " + filesCopied.get());
        }
    }

    /**
     * Copies all files from source folder to destination folder (non-recursive).
     * Creates destination folder if missing.
     */
    private static void copyFolderFiles(Path source, Path dest, AtomicInteger filesCopied) throws IOException {
        if (!Files.exists(source) || !Files.isDirectory(source)) {
            System.err.println("!| Source folder not found: " + source);
            return;
        }

        if (!Files.exists(dest)) {
            Files.createDirectories(dest);
            App.debugPrint("DEBUG| Created destination folder: " + dest);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(source)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    Path targetFile = dest.resolve(file.getFileName());
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    App.debugPrint("DEBUG| Copied comet file: " + targetFile);
                    filesCopied.incrementAndGet();
                }
            }
        }
    }
}