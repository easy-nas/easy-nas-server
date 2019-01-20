package com.easynas.server.util;

import com.google.gson.Gson;
import lombok.NonNull;

/**
 * @author liangyongrui
 */
public class JsonUtils {

    private JsonUtils() {
    }

    public static <T> String toJsonString(@NonNull T object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
