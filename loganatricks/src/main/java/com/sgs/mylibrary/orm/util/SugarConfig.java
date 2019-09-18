package com.sgs.mylibrary.orm.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SugarConfig required for configuring SugarRecord
 */
public class SugarConfig {

    static Map<Class<?>, List<Field>> fields = new HashMap<>();

    /**
     * method for setting the column fields
     * @param clazz
     * @param fieldz
     */
    public static void setFields(Class<?> clazz, List<Field> fieldz) {
         fields.put(clazz, fieldz);
    }

    /**
     * method for getting the columns
     * @param clazz
     * @return
     */
    public static List<Field> getFields(Class<?> clazz) {
        if (fields.containsKey(clazz)) {
            List<Field> list = fields.get(clazz);
            return Collections.synchronizedList(list);
        }

        return null;
    }

    /**
     * method for clearing the cache
     */
    public static void clearCache() {
        fields.clear();
        fields = new HashMap<>();
    }
}
