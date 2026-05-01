package com.fiap_g14.foodlink.api.security;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
    String encode(String rawPassword);
}
