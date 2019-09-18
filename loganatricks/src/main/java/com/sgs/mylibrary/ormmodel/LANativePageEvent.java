package com.sgs.mylibrary.ormmodel;


import com.google.gson.annotations.SerializedName;
import com.sgs.mylibrary.model.PacketData;
import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * LANativePageEvent class is orm of sugarRecord
 */
public class LANativePageEvent extends SugarRecord
{
    @SerializedName("sid")
    public String sessionIdentifier;
    @SerializedName("title")
    public String title;
    @SerializedName("t")
    public long timeStamp;
    @SerializedName("pid")
    public String packetId;
    @SerializedName("et")
    public int eventType;


    public LANativePageEvent() {

    }

    public LANativePageEvent(String sessionIdentifier,
                             String title, long timeStamp,
                             String packetId, int eventType) {
        this.sessionIdentifier = sessionIdentifier;
        this.title = title;
        this.timeStamp = timeStamp;
        this.packetId = packetId;
        this.eventType = eventType;
    }

    public JSONObject getData() {
        JSONObject object = new JSONObject();
        try {
            object.put(Constant.evenType,this.eventType);
            object.put(Constant.evenTime, this.timeStamp);
            object.put("title", this.title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * method is used for getting the getNativePagePacketData
     * @return
     */
    public PacketData getNativePagePacketData() {
        PacketData object = new PacketData();
        object.setEt(eventType);
        object.setT(String.valueOf(timeStamp));
        return object;
    }
}
