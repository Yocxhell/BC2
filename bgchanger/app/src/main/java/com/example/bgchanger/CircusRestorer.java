package com.example.bgchanger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class CircusRestorer {

    public static void restoreCircus(Path rootDir) throws IOException {
        Path source = Paths.get("restore").toAbsolutePath(); // absolute path

        if (!Files.exists(source) || !Files.isDirectory(source)) {
            System.out.println("!| Restore folder not found: " + source);
            return;
        }
        
        System.out.println("\n\nRestoring...\n");
        App.debugPrint("DEBUG| rootDir: " + rootDir + "\nDEBUG| source: " + source);
        AtomicInteger filesCopied = new AtomicInteger(0);

        Files.walkFileTree(source, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                //System.out.println("Visiting directory: " + dir);
                Path targetDir = rootDir.resolve(source.relativize(dir));
                if (!Files.exists(targetDir)) {
                    Files.createDirectories(targetDir);
                    System.out.println("!| Created directory: " + targetDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                //System.out.println("Visiting file: " + file);
                Path targetFile = rootDir.resolve(source.relativize(file));
                try {
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    App.debugPrint("DEBUG| Restored file: " + targetFile);
                    filesCopied.incrementAndGet();
                } catch (IOException e) {
                    System.err.println("!| Failed to restore file: " + file + " -> " + e.getMessage());
                }
                return FileVisitResult.CONTINUE;
            }
        });

        System.out.println("\n | Circus restored successfully. Total files restored: " + filesCopied.get());
    }
}
