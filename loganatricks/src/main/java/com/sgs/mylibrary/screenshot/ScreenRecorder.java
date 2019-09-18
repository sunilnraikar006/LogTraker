package com.sgs.mylibrary.screenshot;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sgs.mylibrary.SessionHelper;
import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.util.Constant;
import com.sgs.mylibrary.util.LibConstants;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

/**
 * Service Class to used to Capture the sceenshots ,to track the user interaction with app ,
 * and this sceenshots is used to create zip fie
 */
public final class ScreenRecorder extends Service {

    private Handler delayHandler = new Handler();
    private boolean shutDown = false;
    public int count = 0;
    ScreenRecordingHelper screenRecordingHelper;
    private long delta = 200;
    private String sessionId;
    private RecordAsyncTask recordAsyncTask;

    private SerialExecutor serialExecutor;
    private static int fps /*= Constant.VIDEO_QUALITY.DEFAULT_FPS*/;
    private static int startIndex = 1;
    private int scale;
    private static int currentIndex;
    public static boolean canCapture = false;
    public static boolean stopSession = false;
    private SessionHelper sessionHelper = SessionHelper.getInstance(this);
    private String jpgImg = ".JPEG";
    private final String TAG = "ScreenRecorder";
    private ScheduledThreadPoolExecutor timeExc = (ScheduledThreadPoolExecutor)
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> backPeriodic;
    private LAErrorVideoAsset errorVideoAsset;
    private static String mErrorId;

    /**
     * Serial Excecutor For calling the ScreenShot Taker method sequentially
     */
    private final class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } catch (Exception e) {
                        if (Constant.SDK_MODE.contentEquals(Constant.Build.TYPE.DEV.name())) {
                            e.printStackTrace();
                        }
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        /**
         * method to schedule the next task that will run sequentially
         */
        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }

    /**
     * method used to invoke the thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {

        public void run() {
            recordAsyncTask = new RecordAsyncTask();
            recordAsyncTask.executeOnExecutor(serialExecutor);
//            new RecordAsyncTask().execute();
            if (!shutDown) {
                delayHandler.postDelayed(this, delta);
            }
            if (shutDown) {
                count = 0;
            }
        }
    };

    /**
     * method used to call the asynchronously by using AsyncTask Class
     */
    private final class RecordAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... strings) {
            // saving the screen shots for creating vedio
            if (!isCancelled())
                saveScreenShots();

            return null;
        }
    }

    /**
     * method to intialise the screen shot functionality
     */
    private void initScreenShot() {

        serialExecutor = new SerialExecutor();
        shutDown = false;
        screenRecordingHelper = ScreenRecordingHelper.getInstance(getApplication());
        canCapture = true;
        currentIndex = 1;
        this.delta = 1000 / (this.fps);

        mUpdateTimeTask.run();
    }

    /**
     * on create method
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // inserting the startIndex and
    }


    /**
     * this method is called first as the service is called from the another class
     * this method will intialise the session id and error id and scale
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        sessionId = intent.getStringExtra(LibConstants.LA_SESSION);
        scale = intent.getIntExtra(LibConstants.SCALE, 3);
        mErrorId = intent.getStringExtra(LibConstants.ERROR_ID);
        start();
        return Service.START_NOT_STICKY;
    }

    /**
     * this method is called once the task is removed from the service
     *
     * @param rootIntent
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        resetInx();
    }

    /**
     * method used to reset the index value when app goes to background state or destroyed state
     */
    private void resetInx() {
        if (stopSession) {
            currentIndex = 1;
            startIndex = currentIndex;
        } else {
            startIndex = currentIndex;
        }
        canCapture = false;    // in background or destroyed
        shutDown = true;
    }

    /**
     * method used to reset the error id once the device is capable to take screen shots
     *
     * @param newErrorId
     */
    public static void reset(String newErrorId) {
        Log.d(SessionHelper.TAG, "reset: " + newErrorId);
        mErrorId = newErrorId;
        currentIndex = 1;
        startIndex = currentIndex;
    }

    /**
     * method is used to trim memory in terms of level
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        resetInx();
    }

    /**
     * method is called once the app goes to background
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        resetInx();
        stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * start capturing screenshot
     */
    public void start() {
        this.canCapture = true;
        initScreenShot();
    }

    /**
     * stop capturing screenshot
     */
    public void stop() {
        this.canCapture = false;
        recordAsyncTask.cancel(true);
    }


    /**
     * should except fileName(currentIndex) sessionidentifier
     */
    private void saveScreenShots() {
        if (canCapture) {
            WeakReference<Activity> activityWeakReference =
                    new WeakReference<Activity>(
                            SessionHelper
                                    .getInstance(getApplication())
                                    .getCurrentActivity());

            Log.d(TAG, "saveScreenShots: sessionId " + sessionId);
            Activity activity = activityWeakReference.get();

            String curIndx = String.valueOf(currentIndex);

            if (activity != null && sessionId != null) {

                File file = screenRecordingHelper.createDirectory(this);
                File sessionDir = new File(getFilesDir() + File.separator + LibConstants.SNAP_SHOT_DIRECTORY, sessionId);
                if (!sessionDir.exists())
                    sessionDir.mkdir();

                File errFile = new File(sessionDir, mErrorId);
                if (!errFile.exists())
                    errFile.mkdirs();

                String fileName = curIndx + jpgImg;
                String filePath = errFile.getPath() + File.separator + fileName;
                if (currentIndex > 100) {
                    int delNo = currentIndex - 100;
                    File delFile = new File(errFile.getPath(), File.separator + delNo + ".JPEG");
                    boolean deleted = delFile.delete();
                }
                boolean wasScreenCaptured =
                        screenRecordingHelper.takeScreenShot(activity, filePath, scale);// capturing the screenShot

                File imageFile = new File(filePath);
                boolean isFileExists = imageFile.exists() && imageFile.isFile() &&
                        imageFile.canRead();

                if (wasScreenCaptured &&
                        isFileExists) {
                    if (imageFile.exists()) {
                        currentIndex++;    // incrementing the count value
                    }
                }
            }

        }
    }

    /**
     * method to get the error id for error video assets sugar record,when new sesion is created
     * @return
     */
    public static String getErrorId() {
        return mErrorId;
    }

    /**
     * method to get the frames per second for screens capturing
     * @param framePerSecond
     */
    public static void getFps(int framePerSecond) {
        fps = framePerSecond;
    }
}



