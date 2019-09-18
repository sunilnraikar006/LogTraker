package com.sgs.mylibrary.ormmodel;


import com.sgs.mylibrary.orm.SugarRecord;

/**
 *  LADeviceInfo class is orm of sugarRecord
 */
public class LADeviceInfo extends SugarRecord {
    public String sessionIdentifier;
    public String eventName;
    public int eventId;
    public String text;
    public int eventIndex;
    public long timeStamp;
    public String status;
    public String eventType;
    public String reason;
    public String packetId;
    public double width;
    public String deviceType;
    public String deviceModel;
    public String deviceOperatingSystem;
    public double height;
    public String deviceIdentifier;


    public LADeviceInfo() {

    }

    public LADeviceInfo(String sessionIdentifier,
                        String eventName, int eventId,
                        String text, int eventIndex,
                        long timeStamp, String status,
                        String eventType, String reason,
                        String packetId, double width,
                        String deviceType, String deviceModel,
                        String deviceOperatingSystem,
                        double height, String deviceIdentifier) {
        this.sessionIdentifier = sessionIdentifier;
        this.eventName = eventName;
        this.eventId = eventId;
        this.text = text;
        this.eventIndex = eventIndex;
        this.timeStamp = timeStamp;
        this.status = status;
        this.eventType = eventType;
        this.reason = reason;
        this.packetId = packetId;
        this.width = width;
        this.deviceType = deviceType;
        this.deviceModel = deviceModel;
        this.deviceOperatingSystem = deviceOperatingSystem;
        this.height = height;
        this.deviceIdentifier = deviceIdentifier;
    }
}
