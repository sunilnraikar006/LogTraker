package com.sgs.mylibrary.ormmodel;


import com.google.gson.annotations.SerializedName;
import com.sgs.mylibrary.model.PacketData;
import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * LAViewEvent orm of sugarRecord
 */
public class LAViewEvent extends SugarRecord
{
    @SerializedName("t")
    public long timeStamp;
    @SerializedName("et")
    public int eventType;
    @SerializedName("sessionid")
    public String sessionIdentifier;
    @SerializedName("pid")
    public String packetId;

    public LAViewEvent() {

    }

    /**
     * @param timeStamp
     * @param eventType
     * @param sessionIdentifier
     * @param packetId
     */
    public LAViewEvent(long timeStamp, int eventType, String sessionIdentifier, String packetId) {
        this.timeStamp = timeStamp;
        this.eventType = eventType;
        this.sessionIdentifier = sessionIdentifier;
        this.packetId = packetId;
    }

    /**
     * @return
     */
    public JSONObject getData() {
        JSONObject object = new JSONObject();
        try {
            object.put(Constant.evenType,this.eventType);
            object.put(Constant.evenTime, this.timeStamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * method for gettting the viewPacketData
     * @return
     */
    public PacketData getViewPacketData() {
        PacketData object = new PacketData();
        object.setEt(eventType);
        object.setT(String.valueOf(timeStamp));
        return object;
    }
}
