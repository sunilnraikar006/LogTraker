package com.sgs.mylibrary.ormmodel;


import com.google.gson.annotations.SerializedName;
import com.sgs.mylibrary.model.PacketData;
import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * LACrashEvent orm model
 */
public class LACrashEvent extends SugarRecord {
    @SerializedName("en")
    public String eventName;
    @SerializedName("details")
    public String text;
    @SerializedName("t")
    public long timeStamp;
    @SerializedName("et")
    public int eventType;
    @SerializedName("r")
    public String reason;
    @SerializedName("pid")
    public String packetId;
    @SerializedName("mn")
    public String methodName;
    @SerializedName("cn")
    public String className;
    @SerializedName("eid")
    public String errorId;

    public LACrashEvent() {

    }

    /**
     * @param sessionId
     * @param packetID
     * @param eventName
     * @param eventId
     * @param eventDetails
     * @param eventTimestamp
     * @param eventType
     * @param reason
     * @param status
     * @param errorId
     */
    public LACrashEvent(String sessionId, String packetID, String eventName,
                        int eventId, String eventDetails,
                        long eventTimestamp,
                        int eventType,
                        String reason,
                        String status, String errorId) {
        this.eventName = eventName;
        this.text = eventDetails;
        this.timeStamp = eventTimestamp;
        this.eventType = eventType;
        this.reason = reason;
        this.packetId = packetID;
        this.errorId = errorId;
    }

    /**
     * @param sessionId
     * @param packetID
     * @param eventName
     * @param eventId
     * @param eventDetails
     * @param eventTimestamp
     * @param eventType
     * @param reason
     * @param status
     * @param methodName
     * @param className
     * @param errorId
     */
    public LACrashEvent(String sessionId, String packetID, String eventName,
                        int eventId, String eventDetails,
                        long eventTimestamp,
                        int eventType,
                        String reason,
                        String status, String methodName, String className, String errorId) {
        this.eventName = eventName;
        this.text = eventDetails;
        this.timeStamp = eventTimestamp;
        this.eventType = eventType;
        this.reason = reason;
        this.packetId = packetID;
        this.methodName = methodName;
        this.className = className;
        this.errorId = errorId;
    }

    /**
     * @param sessionIdentifier
     * @param eventName
     * @param eventId
     * @param text
     * @param timeStamp
     * @param status
     * @param eventType
     * @param reason
     * @param packetId
     * @param errorId
     */
    public LACrashEvent(String sessionIdentifier,
                        String eventName, int eventId,
                        String text,
                        long timeStamp, String status,
                        int eventType, String reason,
                        String packetId, String errorId) {
        this.eventName = eventName;
        this.text = text;
        this.timeStamp = timeStamp;
        this.eventType = eventType;
        this.reason = reason;
        this.packetId = packetId;
        this.errorId = errorId;
    }

    public JSONObject getData() {
        JSONObject object = new JSONObject();
        try {
            object.put(Constant.evenType, this.eventType);
            object.put(Constant.evenTime, this.timeStamp);
            object.put("details", this.getText());
            object.put("methodName", this.getMethodName());
            object.put("className", this.getClassName());
            object.put("name", this.getEventName());
            object.put("r", this.getReason());
            object.put("id", this.getErrorId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * @return
     */
    public PacketData getCrashPacketData() {
        PacketData object = new PacketData();
        object.setEt(eventType);
        object.setDetails(this.getText());
        object.setT(String.valueOf(timeStamp));
        object.setName(this.getEventName());
        object.setR(this.getReason());
        object.setId(this.getErrorId());
        object.setP("android");
        return object;
    }
    //private methods

    /**
     * method will eventName
     * @return
     */
    public String getEventName() {
        return eventName != null ? eventName : "";
    }

    /**
     * method is used for setting the eventName
     * @param eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * method will return text value
     * @return
     */
    public String getText() {
        return text != null ? text : "";
    }

    /**
     * method is used for setting the text value
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * method is used for getting the reason
     * @return
     */
    public String getReason() {
        return reason != null ? reason : "";
    }

    /**
     * method is used for setting the reason
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * method is used for getting the method name
     * @return
     */
    public String getMethodName() {
        return methodName != null ? methodName : "";
    }

    /**
     * method is used for setting the method name
     * @param methodName
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * method is used to get the className
     * @return
     */
    public String getClassName() {
        return className != null ? className : "";
    }

    /**
     * method is used to set the className
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * method is used for getting the errorId
     * @return
     */
    public String getErrorId() {
        return errorId != null ? errorId : "";
    }

    /**
     * method is used for setting the errorId
     * @param errorId
     */
    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }
}
