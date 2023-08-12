package com.mytechhub.misc;

import com.mytechhub.MyTechHub;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

//TODO How to generate OTP
//TODO How to validate OTP without storing rawOtp

public class OtpService implements MyTechHub {

    private final ConcurrentHashMap<String, Otp>
        otpStore;
    public OtpService(){
        otpStore = new ConcurrentHashMap<>();
    }

    private String getRandomOtp(){
        int otp = new Random().nextInt(9999);
        return String.format("%4s",otp)
                .replace(' ','0');
    }

    private String returnHashValue(String rawOtp,
                                   String identifier)
            throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest
                .getInstance("SHA-256");
        String builder = rawOtp
                +"|"+
                identifier;
        byte[] bytes = digest.digest(builder.getBytes());
        return bytesToHexadecimal(bytes);
    }

    private String generateOtpAndStore(String identifier)
            throws NoSuchAlgorithmException {
        if(null == identifier){
            throw new RuntimeException("User identifier " +
                    "is required to generate the otp");
        }
        String rawOtp = getRandomOtp();
        String hashValue = returnHashValue(rawOtp, identifier);
        Otp otpObject = new Otp(identifier,
                LocalDateTime.now()
                        .plusSeconds(60),
                hashValue);
        otpStore.put(identifier,otpObject);
        return rawOtp;
    }

    public String generateOtp(String identifier) {
        try {
            return generateOtpAndStore(identifier);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("failed to generate otp");
        }
    }

    private boolean validateRawOtp(String rawOtp,
                                   String identifier)
            throws NoSuchAlgorithmException {

        if(!otpStore.containsKey(identifier)){
            throw new RuntimeException("No OTP found!");
        }

        Otp otpObject = otpStore.get(identifier);
        if(LocalDateTime.now()
                .isAfter(otpObject.getExpiryDate())){
            return false;
        }

        String hashValue = returnHashValue(rawOtp,
                identifier);
        return hashValue.equals(otpObject.getHashValue());
    }

    public boolean validateOtp(String rawOtp,
                               String identifier){
        try {
            return validateRawOtp(rawOtp,identifier);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
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
}


