package com.meplus.fancy.utils;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by dandanba on 3/1/16.
 */
public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();
    private static ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    static {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }

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

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }
}
