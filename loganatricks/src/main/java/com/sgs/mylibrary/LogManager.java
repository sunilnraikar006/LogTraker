package com.sgs.mylibrary;


import android.app.Application;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.sgs.mylibrary.crashmanager.CrashHandler;
import com.sgs.mylibrary.orm.SugarContext;
import com.sgs.mylibrary.util.LibConstants;
import com.sgs.mylibrary.util.SharedPref;

import java.util.ArrayList;


/**
 * LogManager will be avaible to the Developer
 */
public class LogManager {

    private static final String TAG = LogManager.class.getSimpleName();
    public static Application application;

    public static final int MASK_CODE = -19;
    public static final int UNMASK_CODE = -1;
    private static SessionHelper sessionHelper;

    private static LogManager mInstance;
    private boolean isIntialsed = false;

    /**
     * INTIALISE ALL THE MODULES present in SDK
     * @param app
     * @param token
     */
    public  static final void initialize(final Application app, String token) {
        try {
            application = app;

            SugarContext.init(app);/*orm intialisation*/
            new CrashHandler(application);       /*crash handler intialisation*/
         /*   FFmpeg fFmpeg = FFMPEGHelper.getInstance(app).getFfmpeg();
            fFmpeg.loadBinary(new FFMPEGBinaryLoaderCallback());*/
            sessionHelper = SessionHelper.getInstance(application);
//            sessionHelper.startSession();
            sessionHelper.getConfig();
              /*lifecycle intialisation*/
            ProcessLifecycleOwner.get().getLifecycle().addObserver(sessionHelper);
            application.registerActivityLifecycleCallbacks(sessionHelper);
            application.registerComponentCallbacks(sessionHelper);

            //sessionHelper.getConfig();
            SharedPref.getInstance().save(LibConstants.DEV_TOKEN, token);

        } catch (Exception e) {
            Log.e(TAG, "initialize: " + e.getMessage());
        }
    }

   /* public static void destroy() {
        SugarContext.terminate();
        application.unregisterActivityLifecycleCallbacks(sessionHelper);
        application.unregisterComponentCallbacks(sessionHelper);
    }*/

    /**
     * method will mark a particular view as sensitiveInformation
     * @param view
     */
    public static void markViewAsSensitiveInformation(View view) {
        view.setTag(MASK_CODE);
    }

    /**
     * method will mark a particular view as nonSenstiveInformation
     * @param view
     */
    public static void unMarkViewAsSensitiveInformation(View view) {
        view.setTag(UNMASK_CODE);
    }

    /** method will mark a list of views as senstive information
     * @param views
     */
    public static void markViewsAsSensitiveInformation(ArrayList<View> views) {
        for (View view : views) {
            view.setTag(MASK_CODE);
        }
    }

    /**
     * will mark a list of views as nonSensitiveInformation
     * @param views
     */
    public static void unMarkViewsAsSensitiveInformation(ArrayList<View> views) {

        for (View view : views) {
            view.setTag(UNMASK_CODE);
        }

    }

    /**
     * method will track all the exception
     * developer need to add this method in catch block
     * @param e
     * @param methodName
     * @param className
     */
    public static void trackException(Exception e, String methodName, String className) {
       sessionHelper.addExceptionLog(e, methodName, className);
    }

    /**
     * method will track all the error
     * developer need to add this method in catch block
     * @param error
     */
    public  void trackError(Error error) {
        if(!isIntialsed)
            return;
        sessionHelper.addErrorLog(error);
    }

    /**
     * method will track warning error information
     * developer need to add this method in catch block
     *
     * @param warn
     */
    public  void trackWarnError(String warn) {
        if(!isIntialsed)
            return;
        sessionHelper.addWarningLog(warn);
    }

    /**
     * method will track info error
     * developer need to add this method in catch block
     * @param info
     */
    public  void trackInfoError(String info) {
        if(!isIntialsed)
            return;
        sessionHelper.addInfoLog(info);
    }

}
