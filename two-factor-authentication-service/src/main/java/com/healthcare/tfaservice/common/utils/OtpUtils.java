package com.healthcare.tfaservice.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

@Component
public class OtpUtils {

    @Value("${otp.isHash}")
    private Boolean IS_HASH;

    @Value("${otp.salt}")
    private String SALT;

    private static final String PBKDF2_WITH_HMAC_SHA1 = "PBKDF2WithHmacSHA1";
    private static final String SHA1PRNG = "SHA1PRNG";
    private static final String PERCENT_ZERO = "%0";
    private static final String D = "d";
    private static final String COLON = ":";

    public String generateHash(String otp) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (!IS_HASH) {
            return otp;
        }

        int iterations = 1000;
        char[] chars = otp.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA1);

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + COLON + toHex(salt) + COLON + toHex(hash);
    }

    public boolean validateOtp(String originalOtp, String hashOtp) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (!IS_HASH) {
            return originalOtp.equals(hashOtp);
        }

        String[] parts = hashOtp.split(COLON);
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalOtp.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_WITH_HMAC_SHA1);

        byte[] hashByte = skf.generateSecret(spec).getEncoded();
        int diff = hash.length ^ hashByte.length;

        for (int i = 0; i < hash.length && i < hashByte.length; i++) {
            diff |= hash[i] ^ hashByte[i];
        }

        return diff == 0;
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance(SHA1PRNG);
        byte[] bytes = SALT.getBytes(StandardCharsets.UTF_8);
        sr.nextBytes(bytes);
        return bytes;
    }

    private String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format(PERCENT_ZERO + paddingLength + D, 0) + hex;
        } else {
            return hex;
        }
    }

    private byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
