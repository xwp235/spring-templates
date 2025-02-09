package jp.onehr.base.common.utils;

import com.google.gson.Gson;

public class SimpleJsonUtil {

    private static final Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);  // 将对象转换为 JSON 字符串
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);  // 将 JSON 字符串转换为对象
    }

}
