package com.sgs.mylibrary.orm;

import android.content.Context;

import com.sgs.mylibrary.orm.util.ContextUtil;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Class used to intialise the SugarDb
 */
public class SugarContext {

    private static SugarDbConfiguration dbConfiguration = null;
    private static SugarContext instance = null;
    private SugarDb sugarDb;
    private Map<Object, Long> entitiesMap;

    /**
     * constructor used to intialise the sugarDb instance
     * and entity map instance
     */
    private SugarContext() {
        this.sugarDb = SugarDb.getInstance();
        this.entitiesMap = Collections.synchronizedMap(new WeakHashMap<Object, Long>());
    }

    /**
     *
     * @return sugar context
     */
    public static SugarContext getSugarContext() {
        if (instance == null) {
            throw new NullPointerException("SugarContext has not been initialized properly. Call SugarContext.init(Context) in your Application.onCreate() method and SugarContext.terminate() in your Application.onTerminate() method.");
        }
        return instance;
    }

    /**
     * method used to initialise the db configuration
     * and creating the new instance of the sugar context
     * @param context
     */
    public static void init(Context context) {
        ContextUtil.init(context);
        instance = new SugarContext();
        dbConfiguration = null;
    }

    /**
     * method used to initialise the db configuration
     * and creating the new instance of the sugar context
     * @param context
     * @param configuration
     */
    public static void init(Context context, SugarDbConfiguration configuration) {
        init(context);
        dbConfiguration = configuration;
    }


    /**
     * method used to teminate the context
     */
    public static void terminate() {
        if (instance == null) {
            return;
        }
        instance.doTerminate();
        ContextUtil.terminate();
    }

    /*
     * Per issue #106 on Github, this method won't be called in
     * any real Android device. This method is used purely in
     * emulated process environments such as an emulator or
     * Robolectric Android mock.
     */
    private void doTerminate() {
        if (this.sugarDb != null) {
            this.sugarDb.getDB().close();
        }
    }

    /**
     * method returns the sugardbconfiguration
     * @return
     */
    public static SugarDbConfiguration getDbConfiguration() {
        return dbConfiguration;
    }

    /**
     * method returns the sugar db object
     * @return
     */
    public SugarDb getSugarDb() {
        return sugarDb;
    }

    /**
     *
     * @return entity map
     */
    public Map<Object, Long> getEntitiesMap() {
        return entitiesMap;
    }
}
