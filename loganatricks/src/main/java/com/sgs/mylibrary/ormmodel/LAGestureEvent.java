package com.sgs.mylibrary.ormmodel;


import com.google.gson.annotations.SerializedName;
import com.sgs.mylibrary.model.PacketData;
import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * LAGestureEvent orm for sugarRecord
 */
public class LAGestureEvent extends SugarRecord {
    @SerializedName("sid")
    public String sessionIdentifier;
    @SerializedName("en")
    public String eventName;
    @SerializedName("et")
    public int eventId;
    @SerializedName("text")
    public String text;
    @SerializedName("ei")
    public int eventIndex;
    @SerializedName("x")
    public int positionX;
    @SerializedName("y")
    public int positionY;
    @SerializedName("cn")
    public String componentName;
    @SerializedName("ct")
    public String componentTitle;
    @SerializedName("t")
    public long timeStamp;
    @SerializedName("st")
    public String status;
    @SerializedName("et")
    public int eventType;
    @SerializedName("r")
    public String reason;
    @SerializedName("pid")
    public String packetId;
    @SerializedName("width")
    public double width;
    @SerializedName("dt")
    public String deviceType;
    @SerializedName("dm")
    public String deviceModel;
    @SerializedName("dos")
    public String deviceOperatingSystem;
    @SerializedName("height")
    public double height;
    @SerializedName("did")
    public String deviceIdentifier;


    public LAGestureEvent() {

    }

    /**
     * @param sessionIdentifier
     * @param eventName
     * @param eventId
     * @param text
     * @param eventIndex
     * @param positionX
     * @param positionY
     * @param componentName
     * @param componentTitle
     * @param timeStamp
     * @param status
     * @param eventType
     * @param reason
     * @param packetId
     * @param width
     * @param deviceType
     * @param deviceModel
     * @param deviceOperatingSystem
     * @param height
     * @param deviceIdentifier
     */
    public LAGestureEvent(String sessionIdentifier,
                          String eventName, int eventId,
                          String text, int eventIndex,
                          int positionX, int positionY,
                          String componentName, String componentTitle,
                          long timeStamp, String status, int eventType,
                          String reason, String packetId, double width,
                          String deviceType, String deviceModel,
                          String deviceOperatingSystem, double height,
                          String deviceIdentifier) {
        this.sessionIdentifier = sessionIdentifier;
        this.eventName = eventName;
        this.eventId = eventId;
        this.text = text;
        this.eventIndex = eventIndex;
        this.positionX = positionX;
        this.positionY = positionY;
        this.componentName = componentName;
        this.componentTitle = componentTitle;
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

    public JSONObject getData() {
        JSONObject object = new JSONObject();
        try {
            object.put(Constant.evenType, this.eventType);
            object.put(Constant.evenTime, this.timeStamp);
            object.put("x", this.positionX);
            object.put("y", this.positionY);
            object.put("cn", this.componentName);
            object.put("ct", this.componentTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**s
     * method is used for getting the gesturePacketData
     * @return PacketData
     */
    public PacketData getGesturePacketData() {
        PacketData object = new PacketData();
        object.setEt(eventType);
        object.setT(String.valueOf(timeStamp));
        return object;
    }
}
