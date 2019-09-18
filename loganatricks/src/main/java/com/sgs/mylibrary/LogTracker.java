package com.sgs.mylibrary;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.sgs.mylibrary.crashmanager.CrashHandler;
import com.sgs.mylibrary.logtracker.LogManager;
import com.sgs.mylibrary.orm.SugarContext;
import com.sgs.mylibrary.util.LibConstants;
import com.sgs.mylibrary.util.SharedPref;

public final class LogTracker {
    private static final String TAG = LogTracker.class.getSimpleName();
    public static Application application;

    public static final int MASK_CODE = -19;
    public static final int UNMASK_CODE = -1;
    private static SessionHelper sessionHelper;

    private static LogManager mInstance;
    private boolean isIntialsed = false;
    private static LogTracker logInstance;


    private LogTracker(Application app) {
        try {
            application = app;

            SugarContext.init(app);/*orm intialisation*/
            new CrashHandler(application);       /*crash handler intialisation*/

            sessionHelper = SessionHelper.getInstance(application);
            //sessionHelper.startSession();
            sessionHelper.getConfig();
            /*lifecycle intialisation*/
            ProcessLifecycleOwner.get().getLifecycle().addObserver(sessionHelper);
            application.registerActivityLifecycleCallbacks(sessionHelper);
            application.registerComponentCallbacks(sessionHelper);

            //sessionHelper.getConfig();
            // SharedPref.getInstance().save(LibConstants.DEV_TOKEN, token);

        } catch (Exception e) {
            Log.e(TAG, "initialize: " + e.getMessage());
        }


    }

    public static LogTracker getInstance(Application application) {
        if (logInstance == null)
            logInstance = new LogTracker(application);
        return logInstance;
    }

    public static void intialise(String token) {
        SharedPref.getInstance().save(LibConstants.DEV_TOKEN, token);
    }


    /**
     * method will track all the exception
     * developer need to add this method in catch block
     *
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
     *
     * @param error
     */
    public void trackError(Error error) {
        if (!isIntialsed)
            return;
        sessionHelper.addErrorLog(error);
    }

    /**
     * method will track warning error information
     * developer need to add this method in catch block
     *
     * @param warn
     */
    public void trackWarnError(String warn) {
        if (!isIntialsed)
            return;
        sessionHelper.addWarningLog(warn);
    }

    /**
     * method will track info error
     * developer need to add this method in catch block
     *
     * @param info
     */
    public void trackInfoError(String info) {
        if (!isIntialsed)
            return;
        sessionHelper.addInfoLog(info);
    }

}
