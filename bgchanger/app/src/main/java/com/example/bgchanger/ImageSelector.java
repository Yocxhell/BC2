package com.example.bgchanger;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class ImageSelector {

    private static final String CONFIG_FILE = "config.txt";                        
    private static final String STATE_FILE = "used_backgrounds.txt";               
    private static final String BACKGROUND_CONFIG_FILE = "backgrounds.config.txt"; 
    private static Path bgDir;
    private static Path bgOutputDir;
    private static Path audioOutputDir;
    private static Path loadingOutputDir;
    private static Path torchOutputDir;
    private static Path indicatorOutputDir;
    private static String outputFilename;
    private static boolean pathsLoaded = false;
    private static boolean ranksLoaded = false;
    private static boolean selectionTypeAsked = false;
    private static String userRank = "novice";  // default rank
    private static SelectionType selectionType = SelectionType.RANDOM;

    private static final Map<String, String> RANK_FILENAMES_MAP = Map.of(
        "darkest",    "arena.entrance_room_wall_darkest.png",
        "champion",   "arena.entrance_room_wall_champion.png",
        "veteran",    "arena.entrance_room_wall_veteran.png",
        "apprentice", "arena.entrance_room_wall_apprentice.png",
        "novice",     "arena.entrance_room_wall_novice.png"
    );

    public static void run() throws IOException {
        loadOrAskForPaths();
        loadOrAskForRank();
        askOrChangeSelectionType();

        System.out.println("✔ Current selection mode: " + selectionType.name());

        outputFilename = RANK_FILENAMES_MAP.getOrDefault(userRank, "arena.entrance_room_wall_novice.png");

        File imageDir = bgDir.toFile();
        File outDir = bgOutputDir.toFile();

        if (!outDir.exists()) outDir.mkdirs();

        List<String> allBackgrounds = Arrays.stream(Objects.requireNonNull(imageDir.listFiles()))
                .filter(f -> f.isFile() && !f.isHidden() && f.getName().toLowerCase().endsWith(".png") && !f.getName().equalsIgnoreCase(BACKGROUND_CONFIG_FILE))
                .map(File::getName)
                .collect(Collectors.toList());

        if (allBackgrounds.isEmpty()) {
            System.out.println("\n⚠️ No backgrounds found");
            return;
        }

        Set<String> usedBackgrounds = loadUsedImages();

        if (usedBackgrounds.containsAll(allBackgrounds)) {
            System.out.println("\n✅ All backgrounds used, cycle reset\n");
            usedBackgrounds.clear();
            saveUsedImages(usedBackgrounds);
        }

        List<String> available;
        switch (selectionType) {
            case RANDOM:
                available = allBackgrounds.stream()
                        .filter(img -> !usedBackgrounds.contains(img))
                        .collect(Collectors.toList());
                break;
            case PROGRESSIVE:
                available = getProgressiveSelection(allBackgrounds, usedBackgrounds);
                break;
            case EXCLUSIVE_PROGRESSIVE:
                available = getExclusiveProgressiveSelection(allBackgrounds, usedBackgrounds);
                break;
            default:
                available = allBackgrounds;
        }

        if (available.isEmpty()) {
            System.out.println("\n⚠️ No available backgrounds");
            return;
        }

        String selectedImage = available.get(new Random().nextInt(available.size()));
        System.out.println("\n✔ Background selected: " + selectedImage);

        BackgroundConfig bgConfig = BackgroundConfig.readBackgroundConfig(Paths.get("background pool", BACKGROUND_CONFIG_FILE), selectedImage);
        System.out.println("\n✔ Applied configuration:");
        if (selectionType == SelectionType.PROGRESSIVE || selectionType == SelectionType.EXCLUSIVE_PROGRESSIVE) {
            System.out.println("  - Progression: " + (bgConfig != null ? bgConfig.getProgression() : "N/A"));
        }
        System.out.println("  - Color Grade: " + (bgConfig != null ? bgConfig.getColorGrade() : "default"));
        System.out.println("  - Effect: " + (bgConfig != null ? bgConfig.getEffect() : "none"));
        System.out.println("  - Music: " + (bgConfig != null ? bgConfig.getMusic() : "default"));
        System.out.println("  - Loading: " + (bgConfig != null ? bgConfig.getLoading() : "default"));
        System.out.println("  - Torch: " + (bgConfig != null ? bgConfig.getTorch() : "default"));

        if (bgConfig != null) {
            ImageProcessor.applyColorGrade(bgConfig.getColorGrade(), bgOutputDir);
            MusicProcessor.applyMusic(bgConfig.getMusic(), audioOutputDir);
            ImageProcessor.applyLoadingScreen(bgConfig.getLoading(), loadingOutputDir);
            TorchProcessor.applyTorch(bgConfig.getTorch(), torchOutputDir, indicatorOutputDir);
        } else {
            ImageProcessor.applyColorGrade("default", bgOutputDir);
            MusicProcessor.applyMusic("default", audioOutputDir);
            ImageProcessor.applyLoadingScreen("default", loadingOutputDir);
            TorchProcessor.applyTorch("default", torchOutputDir, indicatorOutputDir);
        }

        if (!selectedImage.toLowerCase().endsWith(".png")) {
            System.out.println("⚠️ Selected background is not a .png file. Selecting another one...");
            run();
            return;
        }

        Path source = bgDir.resolve(selectedImage);
        Path dest = bgOutputDir.resolve(outputFilename);
        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);

        usedBackgrounds.add(selectedImage);
        saveUsedImages(usedBackgrounds);

        System.out.println("✔ Background applied: " + dest.toAbsolutePath());
    }

    private static void loadOrAskForPaths() {
        if (pathsLoaded) return;

        CatchInput input = new CatchInput();

        File config = new File(CONFIG_FILE);
        boolean shouldReset = false;

        if (config.exists()) {
            String reset = input.stringValue("⚙️ Do you want to change output paths? (y/n): ").trim().toLowerCase();
            shouldReset = reset.equals("y") || reset.equals("yes");
        }

        if (!config.exists() || shouldReset) {
            String bcPath = input.stringValue("\n📥 Insert path for darkest dungeon ('.../Steam/steamapps/common/DarkestDungeon'): ");
            //String outPath = input.stringValue("\n📥 Insert output path for backgrounds ('.../dlc/1117860_arena_mp/dungeons/arena'): ");
            //String audioOutPath = input.stringValue("\n📥 Insert output path for custom music ('.../dlc/1117860_arena_mp/audio/secondary_banks'): ");
            //String loadingOutPath = input.stringValue("\n📥 Insert output path for loading screens ('.../dlc/1117860_arena_mp/loading_screen'): ");
            //String torchOutPath = input.stringValue("\n📥 Insert output path for torches ('.../dlc/1117860_arena_mp/fx'): ");

            bgOutputDir = Paths.get(bcPath + "/dlc/1117860_arena_mp/dungeons/arena");
            audioOutputDir = Paths.get(bcPath + "/dlc/1117860_arena_mp/audio/secondary_banks");
            loadingOutputDir = Paths.get(bcPath + "/dlc/1117860_arena_mp/loading_screen");
            torchOutputDir = Paths.get(bcPath + "/dlc/1117860_arena_mp/fx");
            indicatorOutputDir = Paths.get(bcPath + "/dlc/1117860_arena_mp/scripts/layout");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
                writer.write("background_output_dir=" + bgOutputDir.toAbsolutePath().toString());
                writer.newLine();
                writer.write("audio_output_dir=" + audioOutputDir.toAbsolutePath().toString());
                writer.newLine();
                writer.write("loading_output_dir=" + loadingOutputDir.toAbsolutePath().toString());
                writer.newLine();
                writer.write("torch_output_dir=" + torchOutputDir.toAbsolutePath().toString());
                writer.newLine();
                writer.write("indicator_output_dir=" + indicatorOutputDir.toAbsolutePath().toString());
                writer.newLine();
                writer.write("selection_type=" + selectionType.name().toLowerCase());
                writer.newLine();
                writer.write("rank=" + userRank);
                writer.newLine();
                System.out.println("\n✔ Configuration saved in '" + CONFIG_FILE + "'");
            } catch (IOException e) {
                System.err.println("\n⚠️ An error occurred while saving configuration");
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("background_output_dir=")) {
                        bgOutputDir = Paths.get(line.split("=")[1].trim());
                    } else if (line.startsWith("audio_output_dir=")) {
                        audioOutputDir = Paths.get(line.split("=")[1].trim());
                    } else if (line.startsWith("loading_output_dir=")) {
                        loadingOutputDir = Paths.get(line.split("=")[1].trim());
                    } else if (line.startsWith("torch_output_dir=")) {
                        torchOutputDir = Paths.get(line.split("=")[1].trim());
                    } else if (line.startsWith("indicator_output_dir=")) {
                        indicatorOutputDir = Paths.get(line.split("=")[1].trim());
                    } else if (line.startsWith("selection_type=")) {
                        try {
                            selectionType = SelectionType.valueOf(line.split("=")[1].trim().toUpperCase());
                        } catch (IllegalArgumentException ignored) {
                            selectionType = SelectionType.RANDOM;
                        }
                    } else if (line.startsWith("rank=")) {
                        userRank = line.split("=")[1].trim();
                    }
                }
                System.out.println("\n✔ Paths loaded");
            } catch (IOException e) {
                System.err.println("\n⚠️ An error occurred while loading paths, please set new ones");
                config.delete();
                loadOrAskForPaths(); // Retry
            }
        }

        pathsLoaded = true;
    }

    private static void askOrChangeSelectionType() {
        if (selectionTypeAsked) return;  // Skip if already asked

        CatchInput input = new CatchInput();
        File config = new File(CONFIG_FILE);

        if (!config.exists()) {
            // Ask once if no config
            String userInput = input.stringValue("? Select your background selection type (random, progressive, exclusive_progressive): ").toLowerCase();
            try {
                selectionType = SelectionType.valueOf(userInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ Invalid selection type. Using default 'random'.");
                selectionType = SelectionType.RANDOM;
            }
            saveConfigWithRank(userRank);
            selectionTypeAsked = true;
            bgDir = (selectionType == SelectionType.PROGRESSIVE) ? Paths.get("background pool", "progressive"): Paths.get("background pool", "random"); //choose background directory based upon selection type
            return;
        }

        String reset = input.stringValue("⚙️ Do you want to change selection type? (y/n): ").trim().toLowerCase();
        if (reset.equals("y") || reset.equals("yes")) {
            String newSelectionTypeInput = input.stringValue("Select your background selection type (random, progressive): ").toLowerCase(); //exclusive_progressive not implemented yet
            try {
                SelectionType newSelectionType = SelectionType.valueOf(newSelectionTypeInput.toUpperCase());
                if (newSelectionType != selectionType) {
                    selectionType = newSelectionType;

                    File usedFile = new File(STATE_FILE);
                    if (usedFile.exists()) {
                        usedFile.delete();
                        System.out.println("\n✅ Used backgrounds file reset due to selection type change.");
                    }

                    saveConfigWithRank(userRank);
                    System.out.println("✔ Selection type changed to: " + selectionType.name());
                } else {
                    System.out.println("Selection type unchanged.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ Invalid selection type. Keeping previous: " + selectionType.name());
            }
        } else {
            System.out.println("Selection type remains: " + selectionType.name());
        }

        selectionTypeAsked = true;
        bgDir = (selectionType == SelectionType.PROGRESSIVE) ? Paths.get("background pool", "progressive"): Paths.get("background pool", "random"); //choose background directory based upon selection type
    }

    private static void loadOrAskForRank() {
        if (ranksLoaded) return;

        CatchInput input = new CatchInput();
        File config = new File(CONFIG_FILE);

        if (!config.exists()) {
            userRank = input.stringValue("? Insert your rank (darkest, champion, veteran, apprentice, novice): ").toLowerCase();
            saveConfigWithRank(userRank);
            ranksLoaded = true;
            return;
        }

        // Already loaded from config in loadOrAskForPaths(), so just confirm
        String reset = input.stringValue("⚙️ Do you want to change your saved rank? (y/n): ").trim().toLowerCase();
        if (reset.equals("y") || reset.equals("yes")) {
            userRank = input.stringValue("? Insert your rank (darkest, champion, veteran, apprentice, novice): ").toLowerCase();
            saveConfigWithRank(userRank);
        } else {
            System.out.println("✔ Rank loaded: " + userRank);
        }

        ranksLoaded = true;
    }

    private static Set<String> loadUsedImages() {
        Set<String> used = new HashSet<>();
        File usedFile = new File(STATE_FILE);
        if (usedFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(usedFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    used.add(line.trim());
                }
            } catch (IOException e) {
                System.err.println("⚠️ Failed to read used images state file.");
            }
        }
        return used;
    }

    private static void saveUsedImages(Set<String> used) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATE_FILE))) {
            for (String bg : used) {
                writer.write(bg);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("⚠️ Failed to save used images state file.");
        }
    }

    private static void saveConfigWithRank(String rank) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write("background_output_dir=" + (bgOutputDir != null ? bgOutputDir.toAbsolutePath().toString() : ""));
            writer.newLine();
            writer.write("audio_output_dir=" + (audioOutputDir != null ? audioOutputDir.toAbsolutePath().toString() : ""));
            writer.newLine();
            writer.write("loading_output_dir=" + (loadingOutputDir != null ? loadingOutputDir.toAbsolutePath().toString() : ""));
            writer.newLine();
            writer.write("torch_output_dir=" + (torchOutputDir != null ? torchOutputDir.toAbsolutePath().toString() : ""));
            writer.newLine();
            writer.write("indicator_output_dir=" + (indicatorOutputDir != null ? indicatorOutputDir.toAbsolutePath().toString() : ""));
            writer.newLine();
            writer.write("selection_type=" + selectionType.name().toLowerCase());
            writer.newLine();
            writer.write("rank=" + rank);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("⚠️ An error occurred while saving configuration.");
        }
    }

    private static List<String> getProgressiveSelection(List<String> all, Set<String> used) {
        List<String> sorted = all.stream()
                .sorted(Comparator.comparingInt(img -> {
                    BackgroundConfig conf = BackgroundConfig.readBackgroundConfig(Paths.get("background pool", BACKGROUND_CONFIG_FILE), img);
                    return conf != null ? conf.getProgression() : Integer.MAX_VALUE;
                }))
                .collect(Collectors.toList());

        for (String img : sorted) {
            if (!used.contains(img)) {
                return Collections.singletonList(img);
            }
        }
        return Collections.emptyList();
    }

    private static List<String> getExclusiveProgressiveSelection(List<String> all, Set<String> used) {
        List<String> sorted = all.stream()
                .sorted(Comparator.comparingInt(img -> {
                    BackgroundConfig conf = BackgroundConfig.readBackgroundConfig(Paths.get("background pool", BACKGROUND_CONFIG_FILE), img);
                    return conf != null ? conf.getProgression() : Integer.MAX_VALUE;
                }))
                .collect(Collectors.toList());

        OptionalInt minUsedProgression = used.stream()
                .map(img -> BackgroundConfig.readBackgroundConfig(Paths.get("background pool", BACKGROUND_CONFIG_FILE), img))
                .filter(Objects::nonNull)
                .mapToInt(BackgroundConfig::getProgression)
                .min();

        int minProg = minUsedProgression.orElse(-1);

        for (String img : sorted) {
            BackgroundConfig conf = BackgroundConfig.readBackgroundConfig(Paths.get("background pool", BACKGROUND_CONFIG_FILE), img);
            if (conf == null) continue;
            int prog = conf.getProgression();

            if (!used.contains(img) && prog != minProg) {
                return Collections.singletonList(img);
            }
        }

        return Collections.emptyList();
    }

    enum SelectionType {
        RANDOM,
        PROGRESSIVE,
        EXCLUSIVE_PROGRESSIVE
    }
}
