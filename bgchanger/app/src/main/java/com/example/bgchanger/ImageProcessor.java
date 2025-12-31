package com.example.bgchanger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

public class ImageProcessor {

    private static final Path LOADING_SCREENS_DIR = Paths.get("loading screens");
    private static final Path TORCHES_DIR = Paths.get("torches");
    // Mappe esistenti color grades
    private static final Map<String, String> ESTATE_COLOR_GRADE_FILENAMES_MAP = Map.of(
        "default", "default_colour_grade.png",
        "courtyard", "courtyard_colour_grade_4.png",
        "crypts", "crypts_colour_grade_4.png",
        "cove", "cove_colour_grade_4.png",
        "warrens", "warrens_colour_grade_4.png",
        "weald", "weald_colour_grade_4.png",
        "starfield", "starfield_colour_grade.png",
        "flashback_shieldbreaker", "colour_grade_flashback_shieldbreaker.png"
    );
    private static final Map<String, String> DARKEST_COLOR_GRADE_FILENAMES_MAP = Map.of(
        "darkest_quest_1", "darkest_quest_1_colour_grade.png",
        "darkest_quest_2", "darkest_quest_2_colour_grade.png",
        "darkest_quest_3", "darkest_quest_3_colour_grade.png",
        "space_corridor", "space_corridor_colour_grade.png",
        "heartroom", "heartroom_colour_grade.png"
    );
    private static final Map<String, String> FARMSTEAD_COLOR_GRADE_FILENAMES_MAP = Map.of(
        "spacetime_pre_time", "dynamic_colour_grade.wave_pre_time_and_space.png",
        "spacetime_blue", "dynamic_colour_grade.wave_transition_blue.png",
        "spacetime_green", "dynamic_colour_grade.wave_transition_green.png",
        "spacetime_red", "dynamic_colour_grade.wave_transition_red.png",
        "spacetime_yellow", "dynamic_colour_grade.wave_transition_yellow.png"
    );
    private static final Map<String, String> BLACK_RELIQUARY_COLOR_GRADE_FILENAMES_MAP = Map.of(
        "catacombs", "catacombs_colour_grade_4.png",
        "caverns", "caverns_colour_grade_4.png",
        "exposed_interior_day", "exposed_interior_colour_grade_day.png",
        "exposed_interior_night", "exposed_interior_colour_grade_night.png",
        "exposed_interior_eclipse", "exposed_interior_colour_grade_eclipse.png",
        "treasury", "treasury_colour_grade_4.png",
        "flameseeker", "flameseeker_colour_grade_4.png",
        "moon", "moon_colour_grade_4.png"
    );

    private static final Map<String, String> ESTATE_LOADING_SCREEN_FILENAMES_MAP = Map.of(
        "default", "loading_screen.arena.png",
        "crypts", "loading_screen.crypts.png",
        "weald", "loading_screen.weald.png",
        "warrens", "loading_screen.warrens.png",
        "cove", "loading_screen.cove.png",
        "town", "loading_screen.town.png",
        "darkest_1","loading_screen.darkest_dungeon_1.png",
        "darkest_3","loading_screen.darkest_dungeon_3.png",
        "darkest_4","loading_screen.darkest_dungeon_4.png"
    );
    
    private static final Map<String, String> ESTATE_BOSS_LOADING_SCREEN_FILENAMES_MAP = Map.of(
        "necromancer", "loading_screen.necromancer.png",
        "prophet", "loading_screen.prophet.png",
        "hag", "loading_screen.hag.png",
        "brigad_cannon", "loading_screen.brigand_cannon.png",
        "formless_flesh", "loading_screen.formless_flesh.png",
        "swine_prince", "loading_screen.swine_prince.png",
        "drowned_crew","loading_screen.drowned_crew.png",
        "siren","loading_screen.siren.png"
    );
    
    private static final Map<String, String> ESTATE_DLC_LOADING_SCREEN_FILENAMES_MAP = Map.of(
        "courtyard", "loading_screen.courtyard.png",
        "courtyard_baron", "loading_screen.courtyard_baron.png",
        "courtyard_viscount", "loading_screen.courtyard_viscount.png",
        "courtyard_countess", "loading_screen.courtyard_countess.png",
        "farmstead_1", "loading_screen.farm.png",
        "farmstead_2", "loading_screen.farm_2.png"
    );
    
    private static final Map<String, String> BLACK_RELIQUARY_LOADING_SCREEN_FILENAMES_MAP = Map.of(
        "catacombs", "loading_screen.catacombs.png",
        "caverns", "loading_screen.caverns.png",
        "treasury", "loading_screen.treasury.png",
        "exposed_interior", "loading_screen.exposed_interior.png"
    );
    
    private static final Map<String, String> BLACK_RELIQUARY_BOSS_LOADING_SCREEN_FILENAMES_MAP = Map.of(
        "janissary", "loading_screen.janissary.png",
        "enchantress", "loading_screen.enchantress.png",
        "gastrosaur", "loading_screen.gastrosaur.png",
        "greater_sandwurm", "loading_screen.greater_wurm.png",
        "alraune", "loading_screen.alraune.png",
        "nucleic_core", "loading_screen.zcact.png",
        "warhawk", "loading_screen.warhawk.png",
        "steelpath", "loading_screen.steelpath.png"
    );
    
    private static final Map<String, String> BLACK_RELIQUARY_MINIBOSS_LOADING_SCREEN_FILENAMES_MAP = Map.of(
        "flameseeker_otekh", "loading_screen.otekh_invasion.png",
        "ghoul_mother", "loading_screen.ghoul_retention.png",
        "eclipse", "loading_screen.eclipse_invasion.png"
    );

    private static final Map<String, String> TORCH_FILENAMES_MAP = Map.of(
        "default", "default_torch.png"
    );

    // Metodo esistente per i color grades
    public static String getColorGradeFilename(String colorGrade) {
        String filename = ESTATE_COLOR_GRADE_FILENAMES_MAP.get(colorGrade);
        if (filename == null) {
            filename = DARKEST_COLOR_GRADE_FILENAMES_MAP.get(colorGrade);
            if (filename == null) {
                filename = FARMSTEAD_COLOR_GRADE_FILENAMES_MAP.get(colorGrade);
            }
            if (filename == null) {
                filename = BLACK_RELIQUARY_COLOR_GRADE_FILENAMES_MAP.get(colorGrade);
            }
        }
        return filename;
    }

    // Nuovi metodi per loading e torch
    public static String getLoadingScreenFilename(String loading) {
        String filename = ESTATE_LOADING_SCREEN_FILENAMES_MAP.get(loading);
        if (filename == null) {
            filename = ESTATE_BOSS_LOADING_SCREEN_FILENAMES_MAP.get(loading);
            if (filename == null) {
                filename = ESTATE_DLC_LOADING_SCREEN_FILENAMES_MAP.get(loading);
            }
            if (filename == null) {
                filename = BLACK_RELIQUARY_LOADING_SCREEN_FILENAMES_MAP.get(loading);
            }
            if (filename == null) {
                filename = BLACK_RELIQUARY_BOSS_LOADING_SCREEN_FILENAMES_MAP.get(loading);
            }
            if (filename == null) {
                filename = BLACK_RELIQUARY_MINIBOSS_LOADING_SCREEN_FILENAMES_MAP.get(loading);
            }
        }
        return filename;
    }

    public static String getTorchFilename(String torchKey) {
        return TORCH_FILENAMES_MAP.getOrDefault(torchKey, "default_torch.png");
    }

    // Metodo per applicare il color grade (esistente)
    public static void applyColorGrade(String colorGrade, Path outputDir) {
        try {
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
                System.out.println("!| Output directory created at: " + outputDir.toAbsolutePath());
            }

            String filename = getColorGradeFilename(colorGrade);
            if (filename == null) {
                System.err.println("!|️ Color grade not found for: " + colorGrade);
                return;
            }

            Path colorGradePath = Paths.get("color grades", filename);
            if (!Files.exists(colorGradePath)) {
                System.err.println("!|️ Color grade image not found at: " + colorGradePath.toAbsolutePath());
                return;
            }

            Path dest = outputDir.resolve("colour_grade_4.png");
            Files.copy(colorGradePath, dest, StandardCopyOption.REPLACE_EXISTING);
            App.debugPrint("\nDEBUG| Color grade applied: " + dest.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("X|️ Error applying color grade: " + e.getMessage());
        }
    }

    public static void applyLoadingScreen(String loadingKey, Path outputDir) throws IOException {
        if (loadingKey == null || loadingKey.isBlank()) {
            loadingKey = "default";
        }

        String filename = getLoadingScreenFilename(loadingKey);
        Path source = LOADING_SCREENS_DIR.resolve(filename);

        if (!Files.exists(source)) {
            System.err.println("!|️ Loading screen file not found: " + source.toAbsolutePath());
            return;
        }

        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        Path dest = outputDir.resolve("loading_screen.arena_0.png"); // fixed output filename

        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);

        App.debugPrint("DEBUG| Loading screen applied: " + dest.toAbsolutePath());
    }



    public static void copyTorch(String torchKey, Path outputDir) throws IOException {
        if (torchKey == null || torchKey.isBlank()) {
            torchKey = "default";
        }

        String filename = getTorchFilename(torchKey);
        Path source = TORCHES_DIR.resolve(filename);  // fixed input path

        if (!Files.exists(source)) {
            System.err.println("!|️ Torch file not found: " + source.toAbsolutePath());
            return;
        }

        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        Path dest = outputDir.resolve(filename);

        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);

        App.debugPrint("DEBUG| Torch applied: " + filename);
    }

}

