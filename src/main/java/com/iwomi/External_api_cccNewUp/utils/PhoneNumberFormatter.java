package com.iwomi.External_api_cccNewUp.utils;

public class PhoneNumberFormatter {
    public static String format(String number) {
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

    public static void main(String[] args) {
        String number1 = "654321987"; // Without country code
        String number2 = "+237789123456"; // With country code
        String number3 = "237789123456"; // With country code

        try {
            String formattedNumber1 = format(number1);
            String formattedNumber2 = format(number2);
            String formattedNumber3 = format(number3);

            System.out.println("Formatted number 1: " + formattedNumber1); // Output: +237654321987
            System.out.println("Formatted number 2: " + formattedNumber2); // Output: +237789123456
            System.out.println("Formatted number 3: " + formattedNumber3); // Output: +237789123456
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid number: " + e.getMessage());
        }
    }
}
