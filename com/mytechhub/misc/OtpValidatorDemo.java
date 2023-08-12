package com.mytechhub.misc;

import java.util.Scanner;

public class OtpValidatorDemo {
    public static void main(String[] args) {
        OtpService otpService = new OtpService();
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("\n1. Generate OTP" +
                    " \n2. Validate OTP " +
                    " \n3. Exit\nYour option:");
            option = scanner.nextInt();
            String identifier;
            String rawOtp;
            try {
                switch (option) {
                    case 1:
                        System.out.println("user-identifier:");
                        identifier =  scanner.next();
                        rawOtp =  otpService.generateOtp(identifier);
                        System.out.printf("Generated Otp: [%s]", rawOtp);
                        break;
                    case 2:
                        System.out.println("user-identifier:");
                        identifier =  scanner.next();
                        System.out.println("Otp:");
                        rawOtp = scanner.next();
                        System.out.printf("is OTP valid: %s",
                                otpService.validateOtp(rawOtp,identifier));
                        break;
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } while (option == 1 || option == 2);
    }
}
