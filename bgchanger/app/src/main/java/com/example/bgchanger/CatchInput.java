package com.example.bgchanger;

import java.util.Scanner;

public class CatchInput {

    private final Scanner scanner;

    // Constructor initializes the scanner
    public CatchInput() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Read input string value from the user.
     * 
     * @param prompt The prompt message to show before reading the input.
     * @return A final (immutable) string value entered by the user.
     */
    public final String stringValue(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        return input.trim();  // Return the trimmed value as final
    }

    /**
     * Read input integer value from the user.
     * 
     * @param prompt The prompt message to show before reading the input.
     * @return A final (immutable) integer value entered by the user.
     */
    public final int intValue(String prompt) {
        System.out.print(prompt);
        int input = scanner.nextInt();
        scanner.nextLine();  // Clear the buffer
        return input;  // Return the integer value as final
    }

    /**
     * Read input long value from the user.
     * 
     * @param prompt The prompt message to show before reading the input.
     * @return A final (immutable) long value entered by the user.
     */
    public final long longValue(String prompt) {
        System.out.print(prompt);
        long input = scanner.nextLong();
        scanner.nextLine();  // Clear the buffer
        return input;  // Return the long value as final
    }

    /**
     * Read input float value from the user.
     * 
     * @param prompt The prompt message to show before reading the input.
     * @return A final (immutable) float value entered by the user.
     */
    public final float floatValue(String prompt) {
        System.out.print(prompt);
        float input = scanner.nextFloat();
        scanner.nextLine();  // Clear the buffer
        return input;  // Return the float value as final
    }

    /**
     * Read input double value from the user.
     * 
     * @param prompt The prompt message to show before reading the input.
     * @return A final (immutable) double value entered by the user.
     */
    public final double doubleValue(String prompt) {
        System.out.print(prompt);
        double input = scanner.nextDouble();
        scanner.nextLine();  // Clear the buffer
        return input;  // Return the double value as final
    }

    /**
     * Read input boolean value from the user (yes/no).
     * 
     * @param prompt The prompt message to show before reading the input.
     * @return A final (immutable) boolean value entered by the user.
     */
    public final boolean booleanValue(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("y") || input.equals("yes");  // Return the boolean value as final
    }
}
