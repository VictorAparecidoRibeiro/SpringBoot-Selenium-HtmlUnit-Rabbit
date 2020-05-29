package com.example.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    public static <T> T readJson(String json, Class<T> cls) {
        try {
            return new ObjectMapper().readValue(json, cls);
        } catch (Exception ex) {
            return null;
        }
    }

    public static <T> String writeJson(T object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }
}
