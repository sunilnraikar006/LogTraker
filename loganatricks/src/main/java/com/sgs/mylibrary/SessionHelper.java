package com.sgs.mylibrary;


import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.sgs.mylibrary.ormmodel.LAConfiguration;
import com.sgs.mylibrary.ormmodel.LACrashEvent;
import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.ormmodel.LAPacket;
import com.sgs.mylibrary.ormmodel.LASession;
import com.sgs.mylibrary.ormmodel.LASessionEvent;
import com.sgs.mylibrary.screenshot.ScreenRecorder;
import com.sgs.mylibrary.util.ConfigurationHelper;
import com.sgs.mylibrary.util.Constant;
import com.sgs.mylibrary.util.DBHelper;
import com.sgs.mylibrary.util.GarbageCleaner;
import com.sgs.mylibrary.util.LibConstants;
import com.sgs.mylibrary.util.SharedPref;
import com.sgs.mylibrary.util.Utiltity;
import com.sgs.mylibrary.video.ZipCreator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.Context.WINDOW_SERVICE;
import static com.sgs.mylibrary.orm.util.ContextUtil.getPackageName;

/**
 * SessionHelper class is for communicaton between other classes and contains lifeCycle call back method
 */
public class SessionHelper implements LifecycleObserver,
        Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    public static final String TAG = SessionHelper.class.getSimpleName();
    private Context context;
    private static SessionHelper sessionHelper;
    private static TelephonyManager telephonyManager;
    private SessionUploader sessionUploader;
    private LASession currentLASession;
    private LAPacket currentLAPacket;
    private boolean stopSession = false;

    private long previousPauseTime = 0;
    private Activity activeActivity;
    private boolean isViewStart = false;
    private boolean isSessionEnabled = false;
    private Activity topActivity;

    private boolean isFirst = true;
    private ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor)
            Executors.newScheduledThreadPool(1);
    private ScheduledThreadPoolExecutor excBackground = (ScheduledThreadPoolExecutor)
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> periodicFuture;
    private ScheduledFuture<?> backPeriodic;

    private int activityState;  // 1 : OnCreate, 2: onResume, 3 : onStop;
    private String mErrID;
    private int count = 0;

    public Activity getCurrentActivity() {
        return topActivity;
    }

    /**
     * Intialise the SessionHelper
     *
     * @param context
     */
    private SessionHelper(Context context) {
        this.context = context;
        //  telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //initialize session Uploader in sessionHelper Constructor
        sessionUploader = new SessionUploader();
    }

    /**
     * creating the singleton class for sessionHelper
     *
     * @param application
     * @return
     */
    public static SessionHelper getInstance(Application application) {
        if (sessionHelper == null)
            sessionHelper = new SessionHelper(application);
        return sessionHelper;
    }

    /**
     * intailse the session helper call using static method
     *
     * @param context
     * @return
     */
    public static SessionHelper getInstance(Context context) {
        if (sessionHelper == null)
            sessionHelper = new SessionHelper(context);
        return sessionHelper;
    }


    /**
     * method to get the session id using combination of device id and current time in epochvalues
     *
     * @param sessionStartTime
     * @return
     */
    public String getSessionId(long sessionStartTime) {
        return Utiltity.getUniqueId(getDeviceID() + "-" + sessionStartTime);
    }

    /**
     * method to get the session id using combination of device id and current time in epochvalues
     *
     * @param packetStartTime
     * @return
     */
    public String getPacketId(long packetStartTime) {
        return Utiltity.getUniqueId(packetStartTime + "-" + getDeviceID());
    }

    /**
     * method to get the platform of the device
     *
     * @return
     */
    public String getPlatform() {
        return "android";
    }

    /**
     * method to get the country code (iso)
     *
     * @return
     */
    public String getCountry() {
        return telephonyManager.getNetworkCountryIso();
    }

    /*
    @return "android : 4.1.1 ( JELLY_BEAN ) ( sdk=16 )"
     */
    public String getOSSystem() {
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("Android OS : ").append(Build.VERSION.RELEASE);
            Field[] fields = Build.VERSION_CODES.class.getFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                int fieldValue = -1;
                fieldValue = field.getInt(new Object());
                if (fieldValue == Build.VERSION.SDK_INT) {
                    builder.append(" ( ").append(fieldName).append(" ) ");
                    builder.append(" ( ").append("SDK Level = ").append(fieldValue).append(" )");
                }
            }
        } catch (Exception e) {

        } finally {
            return builder.toString();
        }
    }

    /**
     * Returns the unique identifier for the device
     *
     * @return unique identifier for the device
     */
    public String getDeviceID() {
        String deviceUniqueIdentifier = null;
        deviceUniqueIdentifier = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return deviceUniqueIdentifier;
    }

    /**
     * method to call the configuration api to get the configuration values and store the values in sugar orm
     */
    public void getConfig() {
        Intent intent = new Intent(context, ConfigurationHelper.class);
        intent.putExtra("deviceId", getDeviceID());
        context.startService(intent);
    }

    /**
     * method to get the configuration values from the local orm
     *
     * @return
     */
    public LAConfiguration getConfigurationrRecord() {
        try {
            List<LAConfiguration> LAConfigurations = DBHelper.getConfigurationDetails();
            return LAConfigurations.get(0);
        } catch (Exception e) {
            Log.e(TAG, "getConfigurationrRecord: " + e.getMessage());
        }
        return null;
    }

    /**
     * method will return projectId
     *
     * @return
     */
    public String getProjectId() {
        String projectId = "";
        LAConfiguration laConfiguration = getConfigurationrRecord();
        if (laConfiguration != null) {
            projectId = laConfiguration.getProjectId();
        }
        return projectId;
    }

    /**
     * method will return the project key
     *
     * @return
     */
    public String getProjectKey() {
        String projectKey = "";
        LAConfiguration laConfiguration = getConfigurationrRecord();
        if (laConfiguration != null) {
            projectKey = laConfiguration.getKey();
        }
        return projectKey;
    }

    /**
     * method to get the screenwidth in pixels
     *
     * @return int
     */
    public int getScreenWidthInPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    /**
     * method to get the screenheight in pixels
     *
     * @param
     * @return int
     */
    public int getScreenHeightInPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        return height;
    }


    /**
     * method will return appVersion
     *
     * @return string
     */
    public String getAppVersion() {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * method will return string value
     *
     * @return
     */
    public String t() {
        Date d = new Date();
        int i = 0;
        while (d.getTime() == new Date().getTime()) {
            i++;
        }
        String value1 = d.toString();
        String value2 = String.valueOf(i);
        return (value1.length() > 16 ? value1.substring(0, 16) : value1)
                + (value2.length() > 16 ? value2.substring(0, 16) : value2);
    }

    /**
     * method will return String
     *
     * @return
     */
    public String r() {
        String returnValue = String.valueOf(Math.random()).replace('.', ' ');
        return returnValue.length() > 16 ? returnValue.substring(0, 16)
                : returnValue;
    }

    /**
     * method will return screen resolution in string
     *
     * @return
     */
    public String s() {
        return String.valueOf(Resources.getSystem().getDisplayMetrics().widthPixels *
                Resources.getSystem().getDisplayMetrics().heightPixels).toString();
    }


    /**
     * method to start session
     */
    public void startSession() {
        // check is Initialized
        // if initialized check currentSession is created or not
        // if not created create session
        // create packet
        // get configuration
        //

        if (currentLASession == null) {
            insertToDb();
        } else {
            if (stopSession) {  // restart the session
                insertToDb();
            } else {
                if (currentLASession.getIdentifier().contentEquals(getCurrentSessionId())) {
                    currentLASession.startTime = Utiltity.getCurrentTime();
                    currentLASession.save();
                    List<LASession> laSession = DBHelper.getLstSessionId();
                    for (LASession l : laSession) {
                        Log.d(TAG, "sesioId update " + l.getIdentifier());
                    }
                }
            }

        }
    }

    /**
     * Inserting startTime, projId in to db of currentLASession orm
     */
    private void insertToDb() {
        currentLASession = new LASession();
        currentLASession.startTime = Utiltity.getCurrentTime();
        currentLASession.projectId = 1;
        String id = Utiltity.getUniqueId(getDeviceID());
        currentLASession.identifier = id;
        currentLASession.save();

        List<LASession> laSession = DBHelper.getLstSessionId();
        for (LASession l : laSession) {
            Log.d(TAG, "sesioId insert " + l.getIdentifier());
        }

        createPacket();
    }

    /**
     * to stop a  session
     */
    public void stopSession() {
        // create LASessionEvent it is session end event
        LASessionEvent endEvent = new LASessionEvent();
        endEvent.eventType = 12;
        endEvent.timestamp = Utiltity.getCurrentTime();
        if (currentLAPacket != null)
            endEvent.packetId = currentLAPacket.getIdentifier();
        endEvent.save();

/*
        if (currentLASession.getIdentifier().contentEquals(getCurrentSessionId())) {
            List<LASession> laSession = LASession.findById(LASession.class, currentLASession.getIdentifier());
            if (laSession.size() > 0) {
                int size = laSession.size() - 1;

                LASession laSession1 = laSession.get(size);
                laSession1.endTime = Utiltity.getCurrentTime();
                laSession1.save();
*/
        if (currentLASession != null) {
            currentLASession.endTime = Utiltity.getCurrentTime();
            currentLASession.save();  // even for updating the values use save
        }
        List<LASession> laSession = DBHelper.getLstSessionId();
        if (laSession != null && laSession.size() > 0) {
            for (LASession l : laSession) {
                Log.d(TAG, "insertToDb: sesioId " + l.getIdentifier());
            }
        }
    }

    /**
     * creating a packet and saving in to currentLAPacket orm
     */
    public void createPacket() {

        if (currentLAPacket == null) {
            currentLAPacket = new LAPacket();
            currentLAPacket.timeStamp = Utiltity.getCurrentTime();
            currentLAPacket.identifier = Utiltity.getUniqueId(this.getDeviceID());
            currentLAPacket.sessionIdentifier = currentLASession.getIdentifier();
            currentLAPacket.projectId = getProjectId();
            currentLAPacket.save();
        }
    }

    //this method will be called every 10/20 or configured time to upload packets
    public void uploadPackets() {

        //Block1
        // get 3 packets
        List<LAPacket> laPackets = DBHelper.getPackets("3");
        // insert current packet in LAPackets
        laPackets.add(currentLAPacket);
        for (LAPacket laPacket : laPackets) {
            sessionUploader.uploadPacket(laPacket, context, sessionHelper);
        }

        // createPacket
        currentLAPacket = null;
        this.createPacket();


    }

    private void createZipFile() {
        //Block2
        // get 1 video which is not created by query using DBHelper
//        SugarRecord.getEvaNotCreated(LAErrorVideoAsset.class);
        LAErrorVideoAsset eva1 = DBHelper.getErrorVideoAssetNotCreated();
        if (eva1 != null) {
            createZipFile(eva1);
        }
    }

    private void uploadZipFile() {
        //Block3
        // get 1 video which is created and not uploaded using DBHelper
        LAErrorVideoAsset eva2 = DBHelper.getErrorVideoAssetCreatedNotUploaded();
        Log.d("CheckServ", "1st step");
        if (eva2 != null) {
            sessionUploader.uploadZipFile(context, eva2, getProjectId());
        }


        // block 4 when app goes to background
        if (activityState == 3)
            startGarbageCleaner();
    }

    /**
     * startGarbageCleaner method will start a service to clean up all the sessions
     */
    private void startGarbageCleaner() {
        context.startService(new Intent(context, GarbageCleaner.class));
    }

    /**
     * @param eva starting zip service to create zip file
     */
    private void createZipFile(LAErrorVideoAsset eva) {
        Intent intent = new Intent(context, ZipCreator.class);
        intent.putExtra(LibConstants.EVA, eva);
        if (eva.identifier != null)
            context.startService(intent);
    }

    // start screenshot recording

    /**
     * method to start the screenshot capture
     *
     * @param mErrID
     */
    public void startRecording(String mErrID) {
            LAConfiguration laConfiguration = getLaConfig();
        if (laConfiguration != null && laConfiguration.isVideoEnabled()) {
            ScreenRecorder.canCapture = true;
            // intialise the screenShot fps is configurable by server
            ScreenRecorder.getFps(3);
            Intent intent = new Intent(context, ScreenRecorder.class);
            intent.putExtra(LibConstants.LA_SESSION, currentLASession.identifier);
            intent.putExtra(LibConstants.FPS, 3);
            intent.putExtra(LibConstants.SCALE, 3);
            intent.putExtra(LibConstants.ERROR_ID, mErrID);
            context.startService(intent);
        }
    }

    /**
     * method to get the configuration record
     *
     * @return
     */
    private LAConfiguration getLaConfig() {
        LAConfiguration laConfiguration = new LAConfiguration();
        laConfiguration.setId(Utiltity.getCurrentTime());
        return getConfigurationrRecord() != null ? getConfigurationrRecord() :
                laConfiguration;
    }

    // stop screenshot recording
    public void stopRecording() {
        ScreenRecorder.canCapture = false;
        context.stopService(new Intent(context, ScreenRecorder.class));
    }

    /**
     * @return
     */
    private String generateErrorId() {
        return Utiltity.getUniqueId(getDeviceID());
    }

    /**
     * @param exw
     */
    public void addExceptionLog(Throwable exw) {  // eventName is AppCrash
//        this.addCrashLog(e.get);
        LAConfiguration laConfiguration = getLaConfig();
      //  if (laConfiguration != null && laConfiguration.isCrashLog()) {
            Exception ex = new Exception(exw);
            // call addcrashlog
            try {
                Writer stringBuffSync = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringBuffSync);
                ex.printStackTrace(printWriter);
                String stackTrace = stringBuffSync.toString();
                printWriter.close();

                String methodName = ex.getStackTrace()[0].getMethodName();
                String className = ex.getStackTrace()[0].getClassName();

                String reason = ex.getCause().getMessage();
                int i = ex.getCause().toString().indexOf(":");
                String cause = ex.getCause().toString().substring(0, i);
                addCrashLog(cause,
                        reason, methodName
                        , className, stackTrace, 61);
            } catch (Exception e) {
                Log.e(TAG, "addExceptionLog: " + e);
            }
       // }
    }

    /**
     * @param ex
     * @param methodName
     * @param className
     */
    public void addExceptionLog(Exception ex, String methodName, String className) {  // eventName developer bug
//        this.addCrashLog(e.get);
        // call addcrashlog
        LAConfiguration laConfiguration = getLaConfig();
        if (laConfiguration != null && laConfiguration.isDevEnabled()) {
            StringBuffer stringBuffer = new StringBuffer();
            StackTraceElement[] stackTrace = ex.getStackTrace();
            for (int i = 0; i < stackTrace.length; i++) {
                stringBuffer.append(stackTrace[i] + "\n");
            }
            String reason = ex.getMessage();

            addCrashLog(Constant.DEV_BUG,
                    reason, methodName
                    , className, stringBuffer.toString(), 62);
        }
    }

    /**
     * @param ex
     */
    public void addErrorLog(Error ex) {
        //call add error log
        // call addcrashlog
        LAConfiguration laConfiguration = getLaConfig();
        if (laConfiguration != null && laConfiguration.isErrorLog()) {
            StringBuffer stringBuffer = new StringBuffer();
            StackTraceElement[] stackTrace = ex.getStackTrace();
            for (int i = 0; i < stackTrace.length; i++) {
                stringBuffer.append(stackTrace[i]);
            }
            String reason = ex.getMessage();
            addCrashLog(Constant.APPCRASH,
                    reason, "methodName"
                    , "className", stringBuffer.toString(), 62);
        }
    }

    /**
     * @param log
     */
    public void addWarningLog(String log) {
        LAConfiguration laConfiguration = getLaConfig();
        if (laConfiguration != null && laConfiguration.isWarningLog())
            this.addErrorLog(log, 63);
    }

    /**
     * @param log
     */
    public void addInfoLog(String log) {
        LAConfiguration laConfiguration = getLaConfig();
        if (laConfiguration != null && laConfiguration.isInfoLog())
            this.addErrorLog(log, 64);
    }

//    // call this method only if app goes in background
//    public void checkAndDeleteSessions() {
//        // using DBHelper fetch session where errorVideoAsset count = 0
//        // delete sessionIdentifier folder from app directory
//    }

    /**
     * @param log
     * @param eventType
     */
    private void addErrorLog(String log, int eventType) {
        LACrashEvent event = new LACrashEvent();
        event.errorId = Utiltity.getUniqueId(this.getDeviceID());
        event.eventType = eventType;
        event.text = log;
        event.packetId = currentLAPacket.getIdentifier();
        event.save();
    }

    /**
     * @param name
     * @param reason
     * @param methodName
     * @param className
     * @param stack
     * @param eventType
     */
    private void addCrashLog(String name,
                             String reason, String methodName,
                             String className, String stack,
                             int eventType) {

        try {
            String errorId = ScreenRecorder.getErrorId();
            LACrashEvent event = new LACrashEvent();
            event.errorId = errorId;
            event.eventType = eventType;
            event.methodName = methodName;
            event.className = className;
            event.reason = reason;
            event.text = stack;
            event.eventName = name;
            event.timeStamp = Utiltity.getCurrentTime();
            event.packetId = currentLAPacket.getIdentifier();
            event.save();

//            String errorId = generateErrorId();
            this.createErrorVideoAsset(errorId);
            if (eventType != 61) {
                stopRecording();
                String newErrorID = generateErrorId();
                ScreenRecorder.reset(newErrorID);
                startRecording(newErrorID);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param errorId
     */
    private void createErrorVideoAsset(String errorId) {
        try {
            LAErrorVideoAsset eva = new LAErrorVideoAsset();
            eva.identifier = errorId;
            eva.sessionIdentifier = currentLASession.getIdentifier();
            eva.projectId = getProjectId();
            eva.isCreated = 0;
            eva.isUploaded = 0;
            eva.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //reset sessionrecorder frame
    }

    /**
     * @return
     */
    public String getCurrentSessionId() {
        return currentLASession.getIdentifier();
    }

    public boolean bgTimer(boolean stopSession) {
        this.stopSession = stopSession;
        return ScreenRecorder.stopSession = stopSession;
    }

    /**
     * Application Life cycle method
     * activityCreated
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void createActivity() {

    }

    /**
     * Application Life cycle method
     * startActivity
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startActivity() {

    }

    /**
     * Application Life cycle method
     * background call back
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pauseActivity() {

    }

    /**
     * Application Life cycle method
     * foreground callback
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resumeActivity() {
      /*  periodicFuture = sch.scheduleAtFixedRate(TenSecondsTimer, 0L, 10, TimeUnit.SECONDS);
        if (backPeriodic != null)
            backPeriodic.cancel(true);

        if (!getProjectKey().isEmpty()) {
            startSession();     // started Session
            startRecording(generateErrorId());  // started Recording
        } else {
            stopSession();      //stop session
            stopRecording();    //stop recording
        }
*/

        if (!getProjectKey().isEmpty()) {
            checkSDK();
        } else {
            SharedPref.getInstance().save(LibConstants.IS_FIRST, true);
            periodicFuture = sch.scheduleAtFixedRate(TenSecondsTimer, 0L,
                    10, TimeUnit.SECONDS);
        }
    }

    private void checkSDK() {

        startSDK();
    }

    /**
     * sdk setup for recording the screenshots
     */
    private void startSDK() {
    /*    if (!getProjectKey().isEmpty())  // firstTime
        {*/
        Log.d(TAG, "startSDK: key is not empty");
        periodicFuture = sch.scheduleAtFixedRate(TenSecondsTimer, 0L,
                10, TimeUnit.SECONDS);
        if (backPeriodic != null)
            backPeriodic.cancel(true);

        startSession();     // started Session
        startRecording(generateErrorId());  // started Recording
    }

    /**
     * Application Life cycle method
     * background call back
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {

        backPeriodic = excBackground.scheduleAtFixedRate(backGround, 0L,
                2, TimeUnit.MINUTES);
        stopRecording();
    }

    /**
     * Application Life cycle method
     * actvity killed state
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        stopSession();
        bgTimer(true);
        if (sch != null && excBackground != null) {
            sch.shutdown();
            excBackground.shutdown();
        }
        System.gc();
    }


    /**
     * Application Life cycle method with paramters
     *
     * @param activity
     * @param bundle   onActivity created
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        activityState = 1;
//        context.startService(new Intent(context, ZipCreator.class));
        topActivity = activity;
        if (!isSessionEnabled) {
            return;
        }
    }

    /**
     * Application Life cycle method with paramters
     *
     * @param activity life cycle started
     */
    @Override
    public void onActivityStarted(Activity activity) {
        if (!isSessionEnabled) {
            return;
        }
    }

    /**
     * Application Life cycle method with paramters
     *
     * @param activity user interacting state
     */
    @Override
    public void onActivityResumed(Activity activity) {
        activityState = 2;
        if (!isSessionEnabled) {
            return;
        }

        activeActivity = activity;
        topActivity = activity;
//        addGestureEvents(activity);
        String className = activity.getLocalClassName();
        if (isViewStart) {

            isViewStart = false;
        } else {

        }
    }

    /**
     * Application Life cycle method with paramters
     *
     * @param activity background state
     */
    @Override
    public void onActivityPaused(Activity activity) {

        if (!isSessionEnabled) {
            return;
        }
    }

    /**
     * Application Life cycle method with paramters
     *
     * @param activity
     */
    @Override
    public void onActivityStopped(Activity activity) {
        activityState = 3;

        startGarbageCleaner();
        if (!isSessionEnabled) {
            return;
        }
    }

    /**
     * Application Life cycle method with paramters
     *
     * @param activity
     * @param bundle   orientation changes
     */
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (!isSessionEnabled) {
            return;
        }
    }

    /**
     * Application Life cycle method with paramters
     *
     * @param activity destroyed
     */
    @Override
    public void onActivityDestroyed(Activity activity) {
        activityState = 4;
        if (!isSessionEnabled) {
            return;
        }

    }

    /**
     * Application Life cycle method with paramters
     *
     * @param level less Memory
     */
    @Override
    public void onTrimMemory(int level) {

    }

    /**
     * Application Life cycle method with paramters
     *
     * @param newConfig landScape or portrait
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    /**
     * Application Life cycle method with paramters
     * lowMemory
     */
    @Override
    public void onLowMemory() {

    }

    /**
     * 10 seconds timer for uploadingPackets to server
     */
    Runnable TenSecondsTimer = new Runnable() {
        @Override
        public void run() {
            if (!getProjectId().isEmpty()) {
                if (SharedPref.getInstance().get(LibConstants.IS_FIRST, false)) {
                    checkSDK();
                }
                uploadPackets();
                createZipFile();
                uploadZipFile();
                SharedPref.getInstance().save(LibConstants.IS_FIRST, false);
            } else {
                if (Utiltity.isOnline(context)) {
                    getConfig();
                }
            }
        }
    };

    /**
     * 160 seconds timer
     */
    Runnable backGround = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "background 160seconds");
            if (!isFirst) {
                // after 160 seconds stop the ten seconds timer
                periodicFuture.cancel(true);
                stopSession();
                sessionHelper.bgTimer(true);
            }
            isFirst = false;
        }
    };

}
