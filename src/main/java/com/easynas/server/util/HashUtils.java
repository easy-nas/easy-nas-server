package com.easynas.server.util;

import com.google.common.hash.Hashing;
import lombok.NonNull;

/**
 * @author liangyongrui
 */
public class HashUtils {
    private HashUtils() {
    }

    public static String hash(@NonNull String s, @NonNull String salt) {
        return Hashing.sha256().hashUnencodedChars(s + salt).toString();
    }
}
