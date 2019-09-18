package com.sgs.mylibrary.video;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.widget.Toast;


import com.sgs.mylibrary.mpaeg.ExecuteBinaryResponseHandler;
import com.sgs.mylibrary.mpaeg.FFmpegExecuteResponseHandler;
import com.sgs.mylibrary.screenshot.ScreenRecordingHelper;
import com.sgs.mylibrary.util.Constant;

import java.io.File;
import java.util.List;



public final class CreateVideoCallback extends ExecuteBinaryResponseHandler implements FFmpegExecuteResponseHandler {

    public Context context;
    public String sessionId;
    public String filename;
    int sessionDurationIndex;
    long sessionDurationId;
    JobService jobService;
    JobParameters jobParameters;
    ScreenRecordingHelper screenRecordingHelper;

    public CreateVideoCallback(Context context,
                               String sessionId,
                               String fileName,
                               int sessionDurationIndex,
                               long sessionDurationId) {
        this.context = context;
        this.sessionId = sessionId;
        this.filename = fileName;
        this.sessionDurationId = sessionDurationId;
        this.sessionDurationIndex = sessionDurationIndex;
        screenRecordingHelper =
                ScreenRecordingHelper.getInstance(context);
    }

    public CreateVideoCallback(Context context,
                               String sessionId,
                               String fileName,
                               int sessionDurationIndex,
                               long sessionDurationId,
                               JobService jobService,
                               JobParameters jobParameters
                               ) {
        this.context = context;
        this.sessionId = sessionId;
        this.filename = fileName;
        this.sessionDurationId = sessionDurationId;
        this.sessionDurationIndex = sessionDurationIndex;
        this.jobService = jobService;
        this.jobParameters = jobParameters;
        screenRecordingHelper =
                ScreenRecordingHelper.getInstance(context);
    }

    @Override
    public void onStart() {
     }

    @Override
    public void onProgress(String message) {
        Toast.makeText(context, "onProgress" + message, Toast.LENGTH_SHORT).show();
     }

    @Override
    public void onFailure(String message) {
        Toast.makeText(context, "fauked" + message, Toast.LENGTH_SHORT).show();
     }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(context, "vedi creatd sucess" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {

     /*   // schedule file for upload :: start
        SessionRecordDuration sessionDuration =
                SessionRecordDuration.find(SessionRecordDuration.class,
                        "id = ?",
                        new String[]{sessionDurationId+""}).get(0);
        String fileName = sessionDuration.startTime+"_"+sessionDuration.endTime+".mp4";
        File replayFile = new File(screenRecordingHelper.getBaseVideoStorageDirectory()+
                "/"+fileName);
        int videoLength = screenRecordingHelper.getVideoLength(context,replayFile);

        eventTrackerHelper.trackSessionInternalPropertiesForVideoEvent(sessionId,
                sessionDuration.endTime,
                replayFile.length(),
                sessionDuration.endTime - sessionDuration.startTime);
        List<SessionFileTracker> fileTrackers =
                SessionFileTracker.find(SessionFileTracker.class,
                        "session_id = ? and session_duration_index = ?",
                        new String[]{sessionId,sessionDurationIndex+""});
        SessionFileTracker sessionFileTracker = fileTrackers.get(0);
        sessionFileTracker.status = Constant.EVENT.STATUS.READY_TO_UPLOAD.name();
        sessionFileTracker.save();
        ScreenShotTracker.deleteAll(ScreenShotTracker.class,
                "session_id = ? and duration_index = ?",
                new String[]{sessionId, sessionDurationIndex + ""});
         // schedule file for upload :: end
        if(jobService!=null)
            jobService.jobFinished(jobParameters,false);*/
    }


}
