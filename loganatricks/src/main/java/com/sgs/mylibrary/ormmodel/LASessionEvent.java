package com.sgs.mylibrary.ormmodel;


import com.google.gson.annotations.SerializedName;
import com.sgs.mylibrary.model.PacketData;
import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * LASessionEvent orm of SugarRecord
 */
public class LASessionEvent extends SugarRecord {
    @SerializedName("et")
    public int eventType;
    @SerializedName("t")
    public long timestamp;
    @SerializedName("packetid")
    public String packetId;

    public LASessionEvent() {

    }

    /**
     * @param eventType
     * @param timestamp
     */
    public LASessionEvent(int eventType,
                          long timestamp) {
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    /**
     * getting the json data values
     * @return
     */
    public JSONObject getData() {
        JSONObject object = new JSONObject();
        try {
            object.put(Constant.evenType, this.eventType);
            object.put(Constant.evenTime, timestamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * method for getting the SessionPacketData
     * @return  PacketData
     */
    public PacketData getSessionPacketData() {
        PacketData object = new PacketData();
        object.setEt(eventType);
        object.setT(String.valueOf(timestamp));
        return object;
    }

}
