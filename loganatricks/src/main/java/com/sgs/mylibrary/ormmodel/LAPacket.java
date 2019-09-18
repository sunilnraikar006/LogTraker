package com.sgs.mylibrary.ormmodel;


import com.sgs.mylibrary.model.PacketData;
import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.util.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sgs.mylibrary.util.Constant.evenTime;
import static com.sgs.mylibrary.util.Constant.evenType;
import static com.sgs.mylibrary.util.Constant.sessionEventStartCode;

/**
 * LApacket orm of sugarReord
 */
public class LAPacket extends SugarRecord {

    public long timeStamp;
    public String identifier;
    public String sessionIdentifier;
    public int uploadStatus;
    public int eventsCount;
    public String projectId;


    public LAPacket() {

    }

    /**
     * @param projectId
     * @param packetId
     * @param timeStamp
     * @param sessionIdentifier
     * @param uploadStatus
     */
    public LAPacket(String projectId, String packetId, long timeStamp, String sessionIdentifier, int uploadStatus) {
        this.projectId = projectId;
        this.timeStamp = timeStamp;
        this.sessionIdentifier = sessionIdentifier;
        this.uploadStatus = uploadStatus;

    }

    /**
     * method to getSessionStartJsonObject
     *
     * @param sugarRecord
     */
    public void getSessionStartJsonObject(LASession sugarRecord) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(evenTime, sugarRecord.startTime);
            jsonObject.put(evenType, sessionEventStartCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * method to getPacketData
     *
     * @return List<PacketData>
     */
    public List<PacketData> getPacketData()
    {
        try {
            List<PacketData> packetData = new ArrayList<>();

            //query session events tracker
            List<LASessionEvent> LASessionEvents = DBHelper.getSessionEventDataByPacketId(this.identifier);

            //query view events

            List<LAViewEvent> LAViewEvents = DBHelper.getViewEventDataByPacketId(this.identifier);

            //query native page events
            List<LANativePageEvent> LANativePageEvents = DBHelper.getNativePageEventDataByPacketId(this.identifier);

            //query guesture events
            List<LAGestureEvent> LAGestureEvents = DBHelper.getGestureEventDataByPacketId(this.identifier);

            //query crash events
            List<LACrashEvent> LACrashEvents = DBHelper.getCrashEventDataByPacketId(this.identifier);

            //query error logs //TODO:


            //loop through session events and convert each session to json array and append to packetData
            for (LASessionEvent event :
                    LASessionEvents) {
                packetData.add(event.getSessionPacketData());
            }
            //loop through view events and convert each session to json array and append to packetData
            for (LAViewEvent event :
                    LAViewEvents) {
                packetData.add(event.getViewPacketData());
            }

            //loop through native page events and convert each session to json array and append to packetData
            for (LANativePageEvent event :
                    LANativePageEvents) {
                packetData.add(event.getNativePagePacketData());
            }

            //loop through guesture events and convert each session to json array and append to packetData
            for (LAGestureEvent event :
                    LAGestureEvents) {
                packetData.add(event.getGesturePacketData());
            }

            //loop through crash events and convert each session to json array and append to packetData
            for (LACrashEvent event :
                    LACrashEvents) {
                packetData.add(event.getCrashPacketData());
            }
            //loop through error logs events and convert each session to json array and append to packetData
            return packetData;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param newStatus
     */
    public void setUploadStatus(int newStatus) {
        this.uploadStatus = newStatus;
        this.save();
    }

    public void deleteMe() {
        this.delete();
    }

    /**
     * method to get the current packet identifier
     *
     * @return
     */
    public String getIdentifier() {
        return this.identifier != null ? this.identifier : "";
    }


    /**
     * method to get the current packet session identifier
     *
     * @return
     */
    public String getSessionIdentifier() {
        return this.sessionIdentifier != null ? this.sessionIdentifier : "";
    }

    public String getProjectId() {
        return this.projectId != null ? this.projectId : "";
    }
}
