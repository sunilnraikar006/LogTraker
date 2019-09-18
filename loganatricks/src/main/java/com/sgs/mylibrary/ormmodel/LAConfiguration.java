package com.sgs.mylibrary.ormmodel;


import com.sgs.mylibrary.orm.SugarRecord;

/**
 * LAConfiguration orm model
 */
public class LAConfiguration extends SugarRecord {

    private int id;
    private String projectId;
    private String key;
    private boolean isGestureEnabled;
    private String schemaName;
    private boolean infoLog;
    private boolean crashLog;
    private boolean errorLog;
    private boolean warningLog;
    private int sessionTime;
    private String videoQuality;
    private boolean eventsEnabled;
    private int eventsDuration;
    private boolean hideSensitiveInfo;
    private int maxPacketUploadCount;
    private boolean videoRecordingEnabled;
    private boolean isDevEnabled;
    private boolean isVideoEnabled;

    public boolean isVideoEnabled() {
        return isVideoEnabled;
    }

    public boolean isDevEnabled() {
        return isDevEnabled;
    }

    public LAConfiguration() {

    }


    /**
     * @param projectId
     * @param key
     * @param schemaName
     * @param infoLog
     * @param crashLog
     * @param errorLog
     * @param warningLog
     * @param sessionTime
     * @param videoQuality
     * @param eventsEnabled
     * @param eventsDuration
     * @param hideSensitiveInfo
     * @param maxPacketUploadCount
     */
    public LAConfiguration(String projectId, String key,
                           String schemaName, boolean isVideoEnabled,
                           boolean infoLog, boolean crashLog,
                           boolean errorLog, boolean warningLog,
                           int sessionTime, String videoQuality,
                           boolean eventsEnabled, int eventsDuration,
                           boolean hideSensitiveInfo, int maxPacketUploadCount,
                           boolean isGestureEnable,
                           boolean isDevEnabled) {

        this.projectId = projectId;
        this.key = key;
        this.isVideoEnabled = isVideoEnabled;
        this.schemaName = schemaName;
        this.infoLog = infoLog;
        this.crashLog = crashLog;
        this.errorLog = errorLog;
        this.warningLog = warningLog;
        this.sessionTime = sessionTime;
        this.videoQuality = videoQuality;
        this.eventsEnabled = eventsEnabled;
        this.eventsDuration = eventsDuration;
        this.hideSensitiveInfo = hideSensitiveInfo;
        this.maxPacketUploadCount = maxPacketUploadCount;
        this.videoRecordingEnabled = videoRecordingEnabled;
        this.isGestureEnabled = isGestureEnable;
        this.isDevEnabled = isDevEnabled;
    }

    /**
     * method will return the key value
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * method will return projectId
     *
     * @return
     */
    public String getProjectId() {
        return this.projectId != null ? this.projectId : "";
    }


    public boolean isGestureEnabled() {
        return isGestureEnabled;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public boolean isInfoLog() {
        return infoLog;
    }

    public boolean isCrashLog() {
        return crashLog;
    }

    public boolean isErrorLog() {
        return errorLog;
    }

    public boolean isWarningLog() {
        return warningLog;
    }

    public int getSessionTime() {
        return sessionTime;
    }

    public String getVideoQuality() {
        return videoQuality;
    }

    public boolean isEventsEnabled() {
        return eventsEnabled;
    }

    public int getEventsDuration() {
        return eventsDuration;
    }

    public boolean isHideSensitiveInfo() {
        return hideSensitiveInfo;
    }

    public int getMaxPacketUploadCount() {
        return maxPacketUploadCount;
    }

    public boolean isVideoRecordingEnabled() {
        return videoRecordingEnabled;
    }
}
