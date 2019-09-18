package com.sgs.mylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

import static com.sgs.mylibrary.orm.util.ContextUtil.getSharedPreferences;

/**
 * SharedPref class is helper class to store the primitive data types.
 */
public class SharedPref {

    private static final String TAG = "SharedPrefsHelper";

    private static final String SHARED_PREFS_NAME = "aha";

    private static SharedPref instance;

    private SharedPreferences sharedPreferences;

    /** creating singelton class for sharedPreference
     * @return
     */
    public static synchronized SharedPref getInstance() {
        if (instance == null) {
            instance = new SharedPref();
        }

        return instance;
    }

    /**
     * SharePref constructor
     */
    private SharedPref() {
        instance = this;
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * method for deleting primitive data type value
     * @param key
     */
    public void delete(String key) {
        if (sharedPreferences.contains(key)) {
            getEditor().remove(key).commit();
        }
    }

    /**
     * method for saving primitive data type value
     * @param key
     * @param value
     */
    public void save(String key, Object value) {
        SharedPreferences.Editor editor = getEditor();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-supported preference");
        }

        editor.commit();
    }

    /**
     * method for getting a primitive data type value
     * @param key
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    /**
     * method for getting a primitive data type value by passing default value
     * @param key
     * @param defValue
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    /** method will check the string
     * @param key
     * @return
     */
    public boolean has(String key) {
        return sharedPreferences.contains(key);
    }

    public void clearAllData() {
        SharedPreferences.Editor editor = getEditor();
        editor.clear().commit();
    }

    /**
     * method used for intialising the sharedPrefernec Editor class
     * @return
     */
    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

}
