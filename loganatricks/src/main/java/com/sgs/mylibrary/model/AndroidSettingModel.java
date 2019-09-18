package com.sgs.mylibrary.model;

/**
 * AndroidSettingModel class(child class of ConfigModel) is used for capturing
 * * @param infoLog
 * * @param crashLog
 * * @param errorLog
 * * @param warningLog
 * * @param sessionTime
 * * @param videoQuality
 * * @param eventsEnabled
 * * @param eventsDuration
 * * @param hideSensitiveInfo
 * * @param maxPacketUploadCount
 * * @param videoRecordingEnabled
 */

public class AndroidSettingModel {
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
    private boolean isGestureEnabled;
    private boolean isVideoEnabled;

    public boolean isVideoEnabled() {
        return isVideoEnabled;
    }

    public void setVideoEnabled(boolean videoEnabled) {
        isVideoEnabled = videoEnabled;
    }

    public boolean isDevEnabled() {
        return isDevEnabled;
    }

    public void setDevEnabled(boolean devEnabled) {
        isDevEnabled = devEnabled;
    }

    public boolean isGestureEnabled() {
        return isGestureEnabled;
    }

    public void setGestureEnabled(boolean gestureEnabled) {
        isGestureEnabled = gestureEnabled;
    }

    public boolean isInfoLog() {
        return infoLog;
    }

    /**
     * @param infoLog
     */
    public void setInfoLog(boolean infoLog) {
        this.infoLog = infoLog;
    }

    /**
     * @return boolean
     */
    public boolean isCrashLog() {
        return crashLog;
    }

    /**
     * @param crashLog
     */
    public void setCrashLog(boolean crashLog) {
        this.crashLog = crashLog;
    }

    /**
     * @return boolean
     */
    public boolean isErrorLog() {
        return errorLog;
    }

    /**
     * @param errorLog
     */
    public void setErrorLog(boolean errorLog) {
        this.errorLog = errorLog;
    }

    /**
     * @return boolean
     */
    public boolean isWarningLog() {
        return warningLog;
    }

    /**
     * @param warningLog
     */
    public void setWarningLog(boolean warningLog) {
        this.warningLog = warningLog;
    }

    /**
     * @return sessionTime
     */
    public int getSessionTime() {
        return sessionTime;
    }

    /**
     * @param sessionTime
     */
    public void setSessionTime(int sessionTime) {
        this.sessionTime = sessionTime;
    }

    /**
     * @return videoQuality
     */
    public String getVideoQuality() {
        return videoQuality;
    }

    /**
     * @param videoQuality
     */
    public void setVideoQuality(String videoQuality) {
        this.videoQuality = videoQuality;
    }

    /**
     * @return eventsEnabled
     */
    public boolean isEventsEnabled() {
        return eventsEnabled;
    }

    /**
     * @param eventsEnabled
     */
    public void setEventsEnabled(boolean eventsEnabled) {
        this.eventsEnabled = eventsEnabled;
    }

    /**
     * @return eventsDuration
     */
    public int getEventsDuration() {
        return eventsDuration;
    }

    /**
     * @param eventsDuration
     */
    public void setEventsDuration(int eventsDuration) {
        this.eventsDuration = eventsDuration;
    }

    /**
     * @return hideSensitiveInfo
     */
    public boolean isHideSensitiveInfo() {
        return hideSensitiveInfo;
    }

    /**
     * @param hideSensitiveInfo
     */
    public void setHideSensitiveInfo(boolean hideSensitiveInfo) {
        this.hideSensitiveInfo = hideSensitiveInfo;
    }

    /**
     * @return maxPacketUploadCount
     */
    public int getMaxPacketUploadCount() {
        return maxPacketUploadCount;
    }

    /**
     * @param maxPacketUploadCount
     */
    public void setMaxPacketUploadCount(int maxPacketUploadCount) {
        this.maxPacketUploadCount = maxPacketUploadCount;
    }

    /**
     * @return videoRecordingEnabled
     */
    public boolean isVideoRecordingEnabled() {
        return videoRecordingEnabled;
    }

    /**
     * @param videoRecordingEnabled
     */
    public void setVideoRecordingEnabled(boolean videoRecordingEnabled) {
        this.videoRecordingEnabled = videoRecordingEnabled;
    }
}
