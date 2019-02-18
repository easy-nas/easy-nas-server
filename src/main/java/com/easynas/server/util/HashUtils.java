package com.easynas.server.util;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import lombok.NonNull;

import java.io.File;
import java.io.IOException;

/**
 * @author liangyongrui
 */
public class HashUtils {

    private static final String SALT = "123456";

    private HashUtils() {
    }

    public static String hash(@NonNull final String s) {
        return Hashing.sha256().hashUnencodedChars(s + SALT).toString();
    }

    public static String fileSha256Sum(@NonNull final String path) throws IOException {
        return Files.asByteSource(new File(path)).hash(Hashing.sha256()).toString();
    }
}
