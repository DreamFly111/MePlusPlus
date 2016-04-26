package com.meplus.speech.utils;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by dandanba on 3/1/16.
 */
public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();
    private static final ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return mapper.readValue(content, valueType);
        } catch (IOException e) {
            Log.e(TAG, "readValue", e);
        }
        return null;
    }

    public static String writeValueAsString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "writeValueAsString", e);
        }
        return null;
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
