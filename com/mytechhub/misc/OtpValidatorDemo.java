package com.mytechhub.misc;

import com.mytechhub.MyTechHub;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class OtpValidatorDemo implements MyTechHub {

    private final ConcurrentHashMap<String, Otp> otpStore;

    protected OtpValidatorDemo(){
        this.otpStore = new ConcurrentHashMap<>();
    }

    private String getRandomOtp() {
        int value = new Random().nextInt(9999);
        return String.format("%4s", value)
                .replace(' ', '0');
    }

    private String returnHash(final String rawOtp,
                              final String identifier)
            throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest
                .getInstance("SHA-256");
        String builder = rawOtp +
                "|" +
                identifier;

        byte[] encodedBytes = digest.digest(builder.getBytes());
        return bytesToHexadecimal(encodedBytes);
    }

    private String generateAndStoreOtp(String identifier)
            throws NoSuchAlgorithmException {
        if (null == identifier) {
            throw new RuntimeException("The user identifier " +
                    "value shouldn't be null");
        }

        final String rawOtp = getRandomOtp();
        final String hexadecimalValue =
                returnHash(rawOtp, identifier);
        final Otp otpObject = new Otp(identifier,
                hexadecimalValue,
                LocalDateTime.now()
                        .plusSeconds(60));
        otpStore.put(identifier, otpObject);
        return rawOtp;
    }

    private boolean validateRawOtp(final String rawOtp,
                                   final String identifier)
            throws NoSuchAlgorithmException {

        if (!otpStore.containsKey(identifier)) {
            throw new RuntimeException("otp not found!");
        }

        Otp otpObject = otpStore.get(identifier);
        if (LocalDateTime.now()
                .isAfter(otpObject.getExpiryDate())) {
            return false;
        }

        String hashValue = returnHash(rawOtp, identifier);
        return hashValue.equals(otpObject.getHashValue());
    }

    private String bytesToHexadecimal(byte[] hash) {
        StringBuilder hexadecimal =
                new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexadecimal.append('0');
            }
            hexadecimal.append(hex);
        }
        return hexadecimal.toString();
    }

    public String generateOtp(String identifier){
        try {
            //TODO Here you need to integrate with
            // third-party services to send otp to a mobile device
            return generateAndStoreOtp(identifier);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate the otp");
        }
    }

    public boolean validateOtp(final String identifier,
                              final String rawOtp) {

        try {
            return validateRawOtp(rawOtp, identifier);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        OtpValidatorDemo otpValidatorDemo = new OtpValidatorDemo();
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
                        rawOtp =  otpValidatorDemo.generateOtp(identifier);
                        System.out.printf("Generated Otp: [%s]", rawOtp);
                        break;
                    case 2:
                        System.out.println("user-identifier:");
                        identifier =  scanner.next();
                        System.out.println("Otp:");
                        rawOtp = scanner.next();
                        System.out.printf("is OTP valid: %s",
                                otpValidatorDemo.validateOtp(identifier,rawOtp));
                        break;
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } while (option == 1 || option == 2);
    }
}

class Otp {
    private final String identifier;
    private final String hashValue;
    private final LocalDateTime expiryDate;

    protected Otp(String identifier,String hashValue,
                  LocalDateTime expiryDate) {
        this.identifier = identifier;
        this.hashValue = hashValue;
        this.expiryDate = expiryDate;
    }

    protected String getIdentifier(){
        return this.identifier;
    }

    protected String getHashValue(){
        return this.hashValue;
    }

    protected LocalDateTime getExpiryDate(){
        return this.expiryDate;
    }
}
