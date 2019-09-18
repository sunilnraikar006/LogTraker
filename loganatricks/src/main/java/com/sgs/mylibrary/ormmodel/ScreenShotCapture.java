package com.sgs.mylibrary.ormmodel;

import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.screenshot.ScreenRecorder;

public class ScreenShotCapture extends SugarRecord {

    private String sessionId;
    private int currentIdx;
    private String filePath;

    public ScreenShotCapture() {

    }

    public ScreenShotCapture(String sessionId, int currentIdx, String filePath) {
        this.sessionId = sessionId;
        this.currentIdx = currentIdx;
        this.filePath = filePath;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getCurrentIdx() {
        return currentIdx;
    }

    public void setCurrentIdx(int currentIdx) {
        this.currentIdx = currentIdx;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
