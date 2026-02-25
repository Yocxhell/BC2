package com.example.bgchanger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

public class MusicProcessor {

    // Mappe con le associazioni keyword -> file .bank
    private static final Map<String, String> ESTATE_MUSIC_KEY_TO_BANK_FILE = Map.ofEntries(
        Map.entry("default", "default.bank"),
        Map.entry("town", "town.bank"),
        Map.entry("darkest_room", "darkest_room.bank"),
        Map.entry("darkest_room_boss", "darkest_room_boss.bank"),
        Map.entry("courtyard_room", "courtyard_room.bank"),
        Map.entry("courtyard_room_boss", "courtyard_room_boss.bank"),
        Map.entry("cove", "cove.bank"),
        Map.entry("ruins", "ruins.bank"),
        Map.entry("warrens", "warrens.bank"),
        Map.entry("weald", "weald.bank"),
        Map.entry("farmstead", "farmstead.bank"),
        Map.entry("farmstead_spacetime_haunting", "farmstead_spacetime_haunting.bank"),
        Map.entry("farmstead_spacetime_blazing", "farmstead_spacetime_blazing.bank"),
        Map.entry("farmstead_spacetime_splendorous", "farmstead_spacetime_splendorous.bank"),
        Map.entry("farmstead_spacetime_gleaming", "farmstead_spacetime_gleaming.bank")
    );
    
    private static final Map<String, String> BR_MUSIC_KEY_TO_BANK_FILE = Map.ofEntries(
        Map.entry("catacombs_room", "catacombs_room.bank"),
        Map.entry("catacombs_room_boss_janissary", "catacombs_room_boss_janissary.bank"),
        Map.entry("catacombs_room_boss_enchantress", "catacombs_room_boss_enchantress.bank"),
        Map.entry("caverns_room", "caverns_room.bank"),
        Map.entry("caverns_room_boss_gastrosaur", "caverns_room_boss_gastrosaur.bank"),
        Map.entry("caverns_room_boss_greater-sandwurm", "caverns_room_boss_greater-sandwurm.bank"),
        Map.entry("exposed-interior_room_day", "exposed-interior_room_day.bank"),
        Map.entry("exposed-interior_room_night", "exposed-interior_room_night.bank"),
        Map.entry("exposed-interior_room_boss_warhawk", "exposed-interior_room_boss_warhawk.bank"),
        Map.entry("exposed-interior_room_boss_steelpath-mathron", "exposed-interior_room_boss_steelpath-mathron.bank"),
        Map.entry("treasury_room", "treasury_room.bank"),
        Map.entry("treasury_room_boss_alraune", "treasury_room_boss_alraune.bank"),
        Map.entry("treasury_room_boss_nucleic-core", "treasury_room_boss_nucleic-core.bank"),
        Map.entry("exposed-interior_room_miniboss_sand-charmer", "exposed-interior_room_miniboss_sand-charmer.bank"),
        Map.entry("flameseeker_room_miniboss_otekh", "flameseeker_room_miniboss_otekh.bank"),
        Map.entry("general_room_miniboss_ghoul-mother", "general_room_miniboss_ghoul-mother.bank"),
        Map.entry("exposed-interior_room_miniboss_the-flayed", "exposed-interior_room_miniboss_the-flayed.bank"),
        Map.entry("general_room_miniboss_yngolian", "general_room_miniboss_yngolian.bank")
);

    private static final String DEFAULT_BANK_FILE = "default.bank";

    /**
     * Cerca il file .bank corrispondente alla keyword.
     * Se non lo trova in nessuna mappa, restituisce il default.
     * @param key bank name
     * @return 
     */
    public static String getBankFileName(String key) {
        String file = ESTATE_MUSIC_KEY_TO_BANK_FILE.get(key);
        if (file == null) {
            file = BR_MUSIC_KEY_TO_BANK_FILE.get(key);
        }
        if (file == null) {
            file = DEFAULT_BANK_FILE;
        }
        return file;
    }

    /**
     * Copia il file .bank corrispondente alla musica selezionata nella cartella di output.
     * Il file copiato avrà sempre il nome fisso "dlc_thepit_music.bank".
     *
     * @param musicName keyword della musica (es. "farmstead", "town" ecc)  
     * @param outputDir   cartella di destinazione per il file .bank
     */
    // Updated method for handling the bank file path
    public static void applyMusic(String musicName, Path outputDir) {
        try {
            // Check if the output directory exists; if not, create it
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
                System.out.println("!| Output directory created at: " + outputDir.toAbsolutePath());
            }

            // Define the bank file path based on musicName
            Path bankPath = Paths.get("audio banks", musicName + ".bank");

            // Check if the bank file exists
            if (!Files.exists(bankPath)) {
                System.err.println("!|️ Bank file not found at: " + bankPath.toAbsolutePath());
                return;
            }

            // Define the destination path in the output directory for the bank file
            Path dest = outputDir.resolve("dlc_thepit_music.bank");

            // Copy the bank file to the output directory
            Files.copy(bankPath, dest, StandardCopyOption.REPLACE_EXISTING);

            App.debugPrint("\nDEBUG| Music applied: " + dest.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("!|️ Error applying music bank: " + e.getMessage());
        }
    }
}
