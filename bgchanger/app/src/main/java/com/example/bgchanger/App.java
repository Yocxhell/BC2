package com.example.bgchanger;

import java.io.IOException;

public class App {

    private static boolean debugMode = false; //debug mode off by default

    public static void main(String[] args) {
        CatchInput input = new CatchInput();
        String userInput;
        int i = 0;

        System.out.println("\n=== Background Changer v0.5.1 beta ===");

        do {
            i++;
            userInput = input.stringValue("\n" + i + "| Press ENTER to change background\n" +
                    " | Type 'restore_circus' to restore original files\n" +
                    " | Type 'debug_mode' to toggle debug mode (currently: " + (debugMode ? "ON" : "OFF") + ")\n" +
                    " | Type 'exit' to quit: \n\n");

            switch(userInput.toLowerCase()) {
                case "restore_circus":
                    try {
                        ImageSelector.loadOrAskForPaths();
                        CircusRestorer.restoreCircus(ImageSelector.getRootDir());
                    } catch (IOException e) {
                        System.err.println("X| An error occurred while restoring Circus:");
                        e.printStackTrace();
                    }
                    break;

                case "debug_mode":
                    debugMode = !debugMode;
                    System.out.println("\n | Debug mode is now " + (debugMode ? "ON\n" : "OFF\n"));
                    break;

                case "exit":
                    break;

                default:
                    try {
                        ImageSelector.run();
                        debugPrint("DEBUG| Background changed successfully!");
                    } catch (IOException e) {
                        System.err.println("X| An error occurred while selecting a new background:");
                        e.printStackTrace();
                    }
                    break;
            }

        } while (!userInput.equalsIgnoreCase("exit"));

        System.out.println("\nExiting...");
    }

    public static void debugPrint(String message) {
        if (debugMode) {
            System.out.println(message);
        }
    }
}
