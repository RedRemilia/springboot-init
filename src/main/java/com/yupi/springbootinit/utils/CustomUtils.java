package com.yupi.springbootinit.utils;

import java.security.SecureRandom;
import java.util.Random;

public final class CustomUtils {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String DIGITS = "0123456789";
    private static final String CHARS = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";

    public static String generateRandomStr(int length) {
        if (length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    public static String generateRandomStr(int length, boolean onlyNumber) {
        if (length <= 0) {
            return "";
        }
        if (onlyNumber) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
            }
            return sb.toString();
        } else {
            return generateRandomStr(length);
        }
    }



    public static Integer generateRandomInt(int length) {
        if (length <= 0) {
            return 0;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(DIGITS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return Integer.valueOf(sb.toString());
    }
}
