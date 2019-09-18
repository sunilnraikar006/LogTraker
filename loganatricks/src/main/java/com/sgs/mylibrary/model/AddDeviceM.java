package com.sgs.mylibrary.model;

/**
 * AddDeviceM model class is used to for capturing projectId,deviceId,deviceModel,os and deviceVersion
 * and sending the data to  server.
 */
public class AddDeviceM {

    private String projectId;
    private String deviceId;
    private String dM;
    private String os;
    private String dV;

    /**
     * @return projectId (String)
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * @param  projectId
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * @return deviceId
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
     * @return dM
     */
    public String getdM() {
        return dM;
    }

    /**
     * @param dM
     */
    public void setdM(String dM) {
        this.dM = dM;
    }

    /**
     * @return os
     */
    public String getOs() {
        return os;
    }

    /**
     * @param os
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @return dV
     */
    public String getdV() {
        return dV;
    }

    /**
     * @param dV
     */
    public void setdV(String dV) {
        this.dV = dV;
    }
}
