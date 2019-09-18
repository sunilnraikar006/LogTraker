package com.sgs.mylibrary.video;


import android.content.Context;


import com.sgs.mylibrary.mpaeg.FFmpeg;
import com.sgs.mylibrary.screenshot.ScreenRecordingHelper;
import com.sgs.mylibrary.util.Constant;

import org.json.JSONException;

import java.io.File;


public class CreateVideoTask {

    public static CreateVideoTask instance;
    private ScreenRecordingHelper screenRecordingHelper;

    private Context context;

    private CreateVideoTask(Context context) {
        this.context = context;
        this.screenRecordingHelper = ScreenRecordingHelper.getInstance(context);
    }

    public static CreateVideoTask getInstance(Context context) {
        if (instance == null)
            instance = new CreateVideoTask(context);
        return instance;
    }

    public int perform(String... params) throws Exception {
        String sessionId = params[0];
        String durationIndex = params[1];
        /*File replayFile = new File(screenRecordingHelper.getBaseVideoStorageDirectory() +
                "/" + fileName);
        int videoLength = screenRecordingHelper.getVideoLength(context, replayFile);
        List<SessionFileTracker> sesssionVideoFileTrackers =
                DBHelper.getVideoFiles(sessionId, durationIndex + "");
        if (replayFile.exists() && videoLength > 0) {

            SessionFileTracker sessionFileTracker;
            if (!sesssionVideoFileTrackers.isEmpty()) {
                sessionFileTracker = sesssionVideoFileTrackers.get(0);
                sessionFileTracker.status = Constant.EVENT.STATUS.READY_TO_UPLOAD.name();
                sessionFileTracker.save();
                DBHelper.deleteScreenshots(sessionId, durationIndex + "");
                return 1;
            }
        }
        boolean isFileScheduled = !sesssionVideoFileTrackers.isEmpty();
        if (!isFileScheduled &&
                sessionDuration.status.contentEquals(AbSession.SessionState.STOPPED.name())) {
            int fps =
                    getFpsForSession(sessionId);

            createVideo(sessionId, Integer.parseInt(durationIndex),
                    sessionDuration.getID(), sessionDuration.startTime,
                    sessionDuration.endTime, fps, replayFile);
            return 2;
        }*/
        return 0;
    }

    private final int getFpsForSession(String sessionId) throws JSONException {
       /* SessionRecordProperty sessionProperty =
                DBHelper.getSessionProperty(sessionId);
        JSONObject configuration =
                new JSONObject(sessionProperty.sessionConfig);

        String videoQuality
                = (String) configureHelper
                .getConfiguration(configuration,
                        "replayConfig.videoQualityParams.name");
        JSONObject defaultVideoParamJsonObject =
                (JSONObject) ((JSONArray) configureHelper
                        .getConfiguration(configuration,
                                "replayConfig.videoQualityParams.params.default"))
                        .get(0);*/
        return 0;//defaultVideoParamJsonObject.getInt("fps");
    }


}
