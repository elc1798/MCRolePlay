package me.kunai.mcroleplay.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JSONReader {

    private static Gson gson = new Gson();

    public static <T> T fromFile(String filePath, Class<T> klass, HashMap<Type, JsonDeserializer> customDeserializers) throws FileNotFoundException {
        return buildGson(customDeserializers).fromJson(new FileReader(filePath), klass);
    }

    public static <T> T fromString(String json, Class<T> klass, HashMap<Type, JsonDeserializer> customDeserializers) throws FileNotFoundException {
        return buildGson(customDeserializers).fromJson(json, klass);
    }

    private static Gson buildGson(HashMap<Type, JsonDeserializer> customDeserializers) {
        if (customDeserializers == null || customDeserializers.size() == 0) {
            return gson;
        } else {
            GsonBuilder gsonBuilder = new GsonBuilder();
            for (Map.Entry<Type, JsonDeserializer> entry : customDeserializers.entrySet()) {
                gsonBuilder.registerTypeAdapter(entry.getKey(), entry.getValue());
            }

            return gsonBuilder.create();
        }
    }
}
