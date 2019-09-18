package com.sgs.mylibrary.orm;

import java.util.Locale;

/**
 * @author jonatan.salas
 */
public class SugarDbConfiguration {

    /**
     * Tells SQLite which is the database default locale
     */
    private Locale databaseLocale;

    /**
     * Tells SQLite how much it can grow
     */
    private Long maxSize;

    /**
     * Tells SQLite the page size that have
     */
    private Long pageSize;

    public SugarDbConfiguration() { }

    /**
     * get the locale of the database
     * @return
     */
    public Locale getDatabaseLocale() {
        return databaseLocale;
    }

    /**
     * method is used to set the database locale
     * @param databaseLocale
     * @return SugarDbConfiguration
     */
    public SugarDbConfiguration setDatabaseLocale(Locale databaseLocale) {
        this.databaseLocale = databaseLocale;
        return this;
    }

    /**
     * method used to get the max size
     * @return Long
     */
    public Long getMaxSize() {
        return maxSize;
    }

    /**
     * method used to set the maxsize
     * @param maxSize
     * @return SugarDbConfiguration
     */
    public SugarDbConfiguration setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    /**
     * method used to get the page size
     * @return Long
     */
    public Long getPageSize() {
        return pageSize;
    }

    /**
     * method used to set the page size in long datatype
     * @param pageSize
     * @return
     */
    public SugarDbConfiguration setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public String toString() {
        return "SugarDbConfiguration{" +
                ", databaseLocale=" + databaseLocale +
                ", maxSize=" + maxSize +
                ", pageSize=" + pageSize +
                '}';
    }
}
