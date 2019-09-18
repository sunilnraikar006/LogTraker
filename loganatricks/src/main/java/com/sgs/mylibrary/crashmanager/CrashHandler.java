package com.sgs.mylibrary.crashmanager;

import android.app.Application;

import com.sgs.mylibrary.SessionHelper;


/**
 * CrashHandler is used to catch the exception if any crash occurs
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = CrashHandler.class.getSimpleName();
    private final Application application;
    private final Thread.UncaughtExceptionHandler rootHandler;

    public CrashHandler(Application application) {
        this.application = application;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        rootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * @param thread
     * @param ex is the throwable object
     * exception is captured in unCaughtException method
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        try {
            SessionHelper.getInstance(application).addExceptionLog(ex);
            SessionHelper.getInstance(application).stopSession();
        } catch (Exception e) {

        } finally {
            rootHandler.uncaughtException(thread,ex);
        }
    }


}