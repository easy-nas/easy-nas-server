package com.easynas.server.util;

import com.google.common.hash.Hashing;
import lombok.NonNull;

/**
 * @author liangyongrui
 */
public class HashUtils {

    private static final String SALT = "123456";

    private HashUtils() {
    }

    public static String hash(@NonNull String s) {
        return Hashing.sha256().hashUnencodedChars(s + SALT).toString();
    }
}
