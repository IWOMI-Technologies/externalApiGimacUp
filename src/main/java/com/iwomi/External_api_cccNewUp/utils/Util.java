package com.iwomi.External_api_cccNewUp.utils;

public class Util {
    public static String formatPhoneNumber(String number) {
        if (number.length() < 9) {
            throw new IllegalArgumentException("Number must be at least 9 digits long");
        }

        String lastNineDigits = number.substring(number.length() - 9);

        if (number.length() == 9 || number.startsWith("+237") || number.startsWith("237")) {
            return "237" + lastNineDigits;
        } else {
            throw new IllegalArgumentException("Enter a valid number.");
        }
    }

    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
