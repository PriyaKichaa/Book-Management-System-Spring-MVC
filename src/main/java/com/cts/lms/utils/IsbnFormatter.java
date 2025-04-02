package com.cts.lms.utils;

public class IsbnFormatter {

    public static String formatIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }

        if (!isbn.matches("^\\d{10}$|^\\d{13}$")) {
            throw new IllegalArgumentException("Invalid ISBN: Must be exactly 10 or 13 digits (numbers only).");
        }

        if (isbn.length() == 10) {
            return isbn.charAt(0) + "-" +
                    isbn.substring(1, 4) + "-" +
                    isbn.substring(4, 9) + "-" +
                    isbn.substring(9);
        }

        return isbn.substring(0, 3) + "-" +
                isbn.charAt(3) + "-" +
                isbn.substring(4, 6) + "-" +
                isbn.substring(6, 12) + "-" +
                isbn.substring(12);
    }
}