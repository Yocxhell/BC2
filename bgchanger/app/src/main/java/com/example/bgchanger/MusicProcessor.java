package com.example.bgchanger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

public class MusicProcessor {

    // Mappe con le associazioni keyword -> file .bank
    private static final Map<String, String> ESTATE_MUSIC_KEY_TO_BANK_FILE = Map.of(
        "default", "default.bank",
        "farmstead", "farmstead.bank",
        "darkest_room", "darkest_room.bank",
        "darkest_room_boss", "darkest_room_boss.bank",
        "courtyard_room", "courtyard_room.bank",
        "courtyard_room_boss", "courtyard_room_boss.bank",
        "cove", "cove.bank",
        "ruins", "ruins.bank",
        "warrens", "warrens.bank",
        "weald", "weald.bank"
    );

    private static final Map<String, String> ESTATE_MUSIC_KEY_TO_BANK_FILE_2 = Map.of(
        "town", "town.bank"
    );
    
    private static final Map<String, String> BR_MUSIC_KEY_TO_BANK_FILE = Map.of(
        "catacombs_room", "catacombs_room.bank",
        "catacombs_room_boss_janissary", "catacombs_room_boss_janissary.bank",
        "catacombs_room_boss_enchantress", "catacombs_room_boss_enchantress.bank",
        "caverns_room", "caverns_room.bank",
        "caverns_room_boss_gastrosaur", "caverns_room_boss_gastrosaur.bank",
        "caverns_room_boss_greater-sandwurm", "caverns_room_boss_greater-sandwurm.bank",
        "exposed-interior_room_day", "exposed-interior_room_day.bank",
        "exposed-interior_room_night", "exposed-interior_room_night.bank",
        "exposed-interior_room_boss_warhawk", "exposed-interior_room_boss_warhawk.bank",
        "exposed-interior_room_boss_steelpath-mathron", "exposed-interior_room_boss_steelpath-mathron.bank"
    );
    
    private static final Map<String, String> BR_MUSIC_KEY_TO_BANK_FILE_2 = Map.of(
        "treasury_room","treasury_room.bank",
        "treasury_room_boss_alraune","treasury_room_boss_alraune.bank",
        "treasury_room_boss_nucleic-core","treasury_room_boss_nucleic-core.bank",
        "exposed-interior_room_miniboss_sand-charmer","exposed-interior_room_miniboss_sand-charmer.bank",
        "flameseeker_room_miniboss_otekh","flameseeker_room_miniboss_otekh.bank",
        "general_room_miniboss_ghoul-mother","general_room_miniboss_ghoul-mother.bank",
        "exposed-interior_room_miniboss_the-flayed","exposed-interior_room_miniboss_the-flayed.bank",
        "general_room_miniboss_yngolian","general_room_miniboss_yngolian.bank"
    );

    private static final String DEFAULT_BANK_FILE = "default.bank";

    /**
     * Cerca il file .bank corrispondente alla keyword.
     * Se non lo trova in nessuna mappa, restituisce il default.
     * @param key bank name
     */
    public static String getBankFileName(String key) {
        String file = ESTATE_MUSIC_KEY_TO_BANK_FILE.get(key);
        if (file == null) {
            file = ESTATE_MUSIC_KEY_TO_BANK_FILE_2.get(key);
        }
        if (file == null) {
            file = BR_MUSIC_KEY_TO_BANK_FILE.get(key);
        }
        if (file == null) {
            file = BR_MUSIC_KEY_TO_BANK_FILE_2.get(key);
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
                System.out.println("✔ Output directory created at: " + outputDir.toAbsolutePath());
            }

            // Define the bank file path based on musicName
            Path bankPath = Paths.get("audio banks", musicName + ".bank");

            // Check if the bank file exists
            if (!Files.exists(bankPath)) {
                System.err.println("⚠️ Bank file not found at: " + bankPath.toAbsolutePath());
                return;
            }

            // Define the destination path in the output directory for the bank file
            Path dest = outputDir.resolve("dlc_thepit_music.bank");

            // Copy the bank file to the output directory
            Files.copy(bankPath, dest, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("\n✔ Music applied: " + dest.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("⚠️ Error applying music bank: " + e.getMessage());
        }
    }
}
