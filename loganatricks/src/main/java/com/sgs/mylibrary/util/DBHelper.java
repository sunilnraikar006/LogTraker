package com.sgs.mylibrary.util;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sgs.mylibrary.ormmodel.LAConfiguration;
import com.sgs.mylibrary.ormmodel.LACrashEvent;
import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.ormmodel.LAGestureEvent;
import com.sgs.mylibrary.ormmodel.LANativePageEvent;
import com.sgs.mylibrary.ormmodel.LAPacket;
import com.sgs.mylibrary.ormmodel.LASession;
import com.sgs.mylibrary.ormmodel.LASessionEvent;
import com.sgs.mylibrary.ormmodel.LAViewEvent;

import java.util.ArrayList;
import java.util.List;


public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    /**
     * method will return list of LASessionEvent
     *
     * @param status
     * @return
     */
    public static List<LASessionEvent> getEventsByStatus(final String status) {
        return LASessionEvent.find(
                LASessionEvent.class,
                "STATUS = ?",
                new String[]{status},
                null,
                null,
                null
        );
    }

    /**
     * method will return list of LAPackets with help of limit parameter
     *
     * @param limit
     * @return
     */
    public static List<LAPacket> getPackets(String limit) {
        try {
            return LAPacket.find(LAPacket.class,
                    "upload_status=?", new String[]{"0"}, null,
                    "session_identifier ASC", limit);
        } catch (Exception e) {
            Log.d(TAG, "getPackets: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * method will return list of LASessionEvent
     *
     * @param packetId
     * @return
     */
    public static List<LASessionEvent> getSessionEventDataByPacketId(String packetId) {
        try {
            return LASessionEvent.find(LASessionEvent.class, "packet_id=?", new String[]{packetId}, null, null, null);
        } catch (Exception e) {
            Log.e(TAG, "getSessionEventDataByPacketId By packet Id: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * method will return list of LAViewEvent based on the packetId
     *
     * @param packetId
     * @return
     */
    public static List<LAViewEvent> getViewEventDataByPacketId(String packetId) {
        try {
            return LAViewEvent.find(LAViewEvent.class, "packet_id=?", new String[]{packetId}, null, null, null);
        } catch (Exception e) {
            Log.e(TAG, "getViewEventDataByPacketId By packet Id: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * method will return list of LANativePageEvent based on the packetId
     *
     * @param packetId
     * @return
     */
    public static List<LANativePageEvent> getNativePageEventDataByPacketId(String packetId) {
        try {
            return LANativePageEvent.find(LANativePageEvent.class, "packet_id=?", new String[]{packetId}, null, null, null);
        } catch (Exception e) {
            Log.e(TAG, "getNativePageEventDataByPacketId By packet Id: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * method will return list of LAGestureEvent based on the packetId
     *
     * @param packetId
     * @return
     */
    public static List<LAGestureEvent> getGestureEventDataByPacketId(String packetId) {
        try {
            return LAGestureEvent.find(LAGestureEvent.class, "packet_id=?", new String[]{packetId}, null, null, null);
        } catch (Exception e) {
            Log.e(TAG, "getGestureEventDataByPacketId By packet Id: " + e.getMessage());
        }

        return new ArrayList<>();
    }


    /**
     * method will return the list LACrashEvent based on the packetId
     *
     * @param packetId
     * @return
     */
    public static List<LACrashEvent> getCrashEventDataByPacketId(String packetId) {
        try {
            return LACrashEvent.find(LACrashEvent.class, "packet_id=?", new String[]{packetId}, null, null, null);
        } catch (Exception e) {
            Log.e(TAG, "getCrashEventDataByPacketId: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    /**
     * method will return the list of LAConfiguration based on the below parameter
     *
     * @return
     */
    public static List<LAConfiguration> getConfigurationDetails() {
        return LAConfiguration.listAll(LAConfiguration.class);
    }


    /**
     * method will delete the session based on the parameter
     *
     * @param sessionId
     */
    public static void deleteScreenshots(final String sessionId) {
        LAErrorVideoAsset.deleteAll(LAErrorVideoAsset.class,
                "identifier = ?",
                new String[]{sessionId});
    }


    /**
     * method to delete the session data by session id
     *
     * @param sessionId
     */
    public static void deleteSessionData(String sessionId) {
        try {
            int i = LASession.deleteAll(LASession.class,
                    "identifier = ?",
                    new String[]{sessionId});
            Log.d(TAG, "deleteSessionData: "+i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * method will return the LAErrorVideoAsset only if vedio is created
     *
     * @return
     */
    public static LAErrorVideoAsset getErrorVideoAssetNotCreated() {
        try {
            return LAErrorVideoAsset.findLimit(LAErrorVideoAsset.class,
                    "is_created=?", "1", "id DESC", new String[]{"0"}).get(0);
        } catch (Exception e) {
            Log.e(TAG, "getErrorVideoAssetNotCreated: " + e.getMessage());
        }
        return null;
    }

    /**
     * method will return LAErrorVideoAsset where vedio is created and not uploaded
     *
     * @return
     */
    public static LAErrorVideoAsset getErrorVideoAssetCreatedNotUploaded() {
        try {
            return LAErrorVideoAsset.findLimit(LAErrorVideoAsset.class,
                    "is_created=? and is_uploaded=?", "1", "session_identifier DESC", new String[]{"1", "0"}).get(0);
        } catch (Exception e) {
            Log.e(TAG, "getErrorVideoAssetCreatedNotUploaded: " + e.getMessage());
        }
        return null;
    }

    /**
     * method will return list of LASession
     *
     * @return
     */
    public static List<LASession> getLstSessionId() {
        return LASession.lstSessionRecord(LASession.class);
    }

    /**
     * method will return list of LASession
     *
     * @return
     */
    public static List<LASession> getAllSession() {
        return LASession.listAll(LASession.class);
    }

    /**
     * method will return LAErrorVideoAsset based on the sessionId
     *
     * @param sessionId
     * @return
     */
    public static LAErrorVideoAsset getErrorVideoAssetBySessionId(String sessionId) {
        List<LAErrorVideoAsset> list = LAErrorVideoAsset.findLimit(LAErrorVideoAsset.class,
                "session_identifier=?", "1", "session_identifier ", new String[]{sessionId});
        if (list.size() > 0)
            return list.get(0);
        else
            return null;
    }

    /**
     * method will return list of LAErrorVideoAsset
     *
     * @return
     */
    public static List<LAErrorVideoAsset> getListEVA() {
        List<LAErrorVideoAsset> list = LAErrorVideoAsset.findLimit(LAErrorVideoAsset.class,
                null, null, null, null);
        if (list.size() > 0)
            return list;
        else
            return null;
    }

    /**
     * method will return
     *
     * @return
     */
    public static LAErrorVideoAsset getErrorVideoAsset() {
        return LAErrorVideoAsset.findLimit(LAErrorVideoAsset.class,
                "is_created=?", "1", "session_identifier DESC", new String[]{"0"}).get(0);
    }

}
