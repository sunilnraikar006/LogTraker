package com.sgs.mylibrary.model;

import java.util.List;

/**
 * ConfigModel class will capture
 *  *     * @param id
 *  *      * @param key
 *  *      * @param schemaName
 *  *      * @param androidSettings
 *  and sending data to server with the help of configurationHelper class
 */
public class ConfigModel {

    private int id;
    private String key;
    private String schemaName;
    private List<AndroidSettingModel> androidSettings;

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return schemaName
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * @param schemaName
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * @return androidSettings
     */
    public List<AndroidSettingModel> getAndroidSettings() {
        return androidSettings;
    }

    /**
     * @param androidSettings
     */
    public void setAndroidSettings(List<AndroidSettingModel> androidSettings) {
        this.androidSettings = androidSettings;
    }
}
