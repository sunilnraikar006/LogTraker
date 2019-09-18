package com.sgs.mylibrary.log_interface;

import android.content.Context;

import com.sgs.mylibrary.ormmodel.LASession;


import java.util.ArrayList;


abstract public class AbSession {

    public enum SessionState {
        CREATED,
        STARTED,
        PAUSED,
        RESUMED,
        STOPPED
    }

    private Context context;

    private LASession sessionModel;
    private SessionState state;

    public AbSession(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

   /* public SDKSessionProperty getSessionProperty() {
        return sessionProperty;
    }*/

    public SessionState getState() {
        return state;
    }


   /* public final void  createSession() {
        this.sessionProperty = new SDKSession(context);
        this.sessionDurations = new ArrayList<>();
        state = SessionState.CREATED;
         onCreate();
    }

    public final void startSession() {
        long ts = System.currentTimeMillis();
        state = SessionState.STARTED;
        *//*
            < UPDATE SESSION TABLE >
         *//*
        sessionModel =

                new LASession(getSessionProperty().getSessionId(),
                        getState().name()) {
                };
        sessionModel.save();

        sessionProperty.saveSessionStartEPOCH(ts);

        *//*
            </ UPDATE SESSION TABLE >
         *//*
        onStart();
    }

    public final void pauseSession() {
        state = SessionState.PAUSED;
        *//*
            < UPDATE SESSION TABLE >
         *//*
        sessionModel.sessionState = getState().name();
        sessionModel.save();
        *//*
            </ UPDATE SESSION TABLE >
         *//*
        *//*
            < UPDATE SESSION_PROPERTY TABLE >
        *//*
        sessionProperty.saveSessionEndEPOCH();
        *//*
            </ UPDATE SESSION_PROPERTY TABLE >
        *//*
        onPause();
    }

    public final void resumeSession() {
        state = SessionState.RESUMED;
        *//*
            < UPDATE SESSION TABLE >
         *//*
        sessionModel.sessionState = getState().name();
        sessionModel.save();
        *//*
            </ UPDATE SESSION TABLE >
         *//*
        onResume();
    }

    public final void stopSession() {
        state = SessionState.STOPPED;
        *//*
            < UPDATE SESSION TABLE >
         *//*
        sessionModel.sessionState = getState().name();
        sessionModel.save();
        *//*
            </ UPDATE SESSION TABLE >
        *//*
        *//*
            < UPDATE SESSION_PROPERTY TABLE >
        *//*
        sessionProperty.saveSessionEndEPOCH();
        *//*
            </ UPDATE SESSION_PROPERTY TABLE >
        *//*
       // new SessionStopTracker(System.currentTimeMillis()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new AbSession[]{this});
        onStop();
    }

    public final void haltSession() {
        state = SessionState.STOPPED;
        *//*
            < UPDATE SESSION TABLE >
         *//*
        sessionModel.sessionState = getState().name();
        sessionModel.save();
        *//*
            </ UPDATE SESSION TABLE >
        *//*
        *//*
            < UPDATE SESSION_PROPERTY TABLE >
        *//*
        sessionProperty.saveSessionEndEPOCH();
        *//*
            </ UPDATE SESSION_PROPERTY TABLE >
        *//*
        onHalt();
    }*/

    abstract public void onCreate();

    abstract public void onStart();

    abstract public void onPause();

    abstract public void onResume();

    abstract public void onStop();

    abstract public void onHalt();



}
