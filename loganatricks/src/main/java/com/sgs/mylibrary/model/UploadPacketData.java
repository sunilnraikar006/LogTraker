package com.sgs.mylibrary.model;

import java.util.List;

/**
 * UploadPacketData class contains below parameters
 *      * @param sessionId
 *      * @param projectId
 *      * @param deviceId
 *      * @param packetTime
 *      * @param appVersion
 *      * @param platform
 *      * @param packetData
 */
public class UploadPacketData {

    private String sessionId;
    private String projectId;
    private String deviceId;
    private String packetTime;
    private String appVersion;
    private String platform;
    private List<PacketData> packetData;

    /**
     * @return
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * @param projectId
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * @return
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return
     */
    public String getPacketTime() {
        return packetTime;
    }

    /**
     * @param packetTime
     */
    public void setPacketTime(String packetTime) {
        this.packetTime = packetTime;
    }

    /**
     * @return
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * @param appVersion
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * @return
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * @param platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * @return
     */
    public List<PacketData> getPacketData() {
        return packetData;
    }

    /**
     * @param packetData
     */
    public void setPacketData(List<PacketData> packetData) {
        this.packetData = packetData;
    }
}
