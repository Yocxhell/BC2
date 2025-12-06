package com.example.bgchanger;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        CatchInput input = new CatchInput();
        String userInput;
        int i = 0;
        
        System.out.println("\n=== Background Changer v0.5.0 open-beta ===");
        do {i++;
            userInput = input.stringValue("\n" + i + "| Press ENTER to change background (or type 'exit' to quit): ");

            if (!userInput.equalsIgnoreCase("exit")) {
                try {
                    ImageSelector.run();
                } catch (IOException e) {
                    System.err.println("\nAn error occurred while selecting a new background: ");
                    e.printStackTrace();
                }
            }

        } while (!userInput.equalsIgnoreCase("exit"));

    }
}
