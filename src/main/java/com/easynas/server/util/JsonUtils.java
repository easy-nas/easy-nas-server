package com.easynas.server.util;

import com.google.gson.Gson;

/**
 * @author liangyongrui
 */
public class JsonUtils {

    private JsonUtils() {

    }

    public static <T> String toJsonString(T object){
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
