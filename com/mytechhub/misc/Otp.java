package com.mytechhub.misc;

import java.time.LocalDateTime;

class Otp {
    private final String identifier;
    private final LocalDateTime expiryDate;
    private final String hashValue;

    public Otp(final String identifier,
               final LocalDateTime expiryDate,
               final String hashValue) {
        this.identifier = identifier;
        this.expiryDate = expiryDate;
        this.hashValue = hashValue;
    }

    public String getIdentifier() {
        return identifier;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public String getHashValue() {
        return hashValue;
    }
}
