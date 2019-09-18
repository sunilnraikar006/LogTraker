package com.sgs.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.sgs.mylibrary.model.PacketData;
import com.sgs.mylibrary.ormmodel.LAErrorVideoAsset;
import com.sgs.mylibrary.ormmodel.LAPacket;
import com.sgs.mylibrary.service.UploadDataService;
import com.sgs.mylibrary.service.UploadZipService;
import com.sgs.mylibrary.util.LibConstants;
import com.sgs.mylibrary.util.Utiltity;

import java.util.ArrayList;
import java.util.List;

/**
 * SessionUploader will upload the packets to server
 */
public class SessionUploader {


    /**
     * This method will help to update the packet data to server
     *
     * @param packet
     * @param context
     * @param sessionHelper
     */
    public void uploadPacket(LAPacket packet, Context context, SessionHelper sessionHelper) {

        //check internet connection
        //get packet data
        //if packet data length > 0 then upload else delete the packet
        // change packet.isUploadingStatus = 1 and save
        //upload data
        // if upload success, change packet.isUPloadingStatus =2, save, delete packet
        // if upload failure, change packet.isUploadingStatus = 0


        try {
            List<PacketData> packetData = packet.getPacketData();
            if (Utiltity.isOnline(context)) {
                if (packetData != null && packetData.size() > 0) {
                    packet.setUploadStatus(1);
                    Intent intent = new Intent(context, UploadDataService.class);
                    intent.putParcelableArrayListExtra("packetData", (ArrayList<? extends Parcelable>) packetData);
                    intent.putExtra("AppVersion", sessionHelper.getAppVersion());
                    intent.putExtra("sessionId", packet.getSessionIdentifier());
                    intent.putExtra("packagetime", Utiltity.getCurrentTime());
                    intent.putExtra("platform", sessionHelper.getPlatform());
                    intent.putExtra("deviceid", sessionHelper.getDeviceID());
                    intent.putExtra("projectId", packet.getProjectId());
                    context.startService(intent);
                } else {
                    packet.deleteMe();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * method will upload zip files to server
     *
     * @param context
     * @param eva
     * @param projectId
     */
    public void uploadZipFile(Context context, LAErrorVideoAsset eva, String projectId) {
        // eva.isuploading = 1 and save
        // start uploading
        // if success, delete file from path and eva.deleteMe()
        // if failed, eva.isuploading = 0 and save
        try {
            if (Utiltity.isOnline(context)) {
                Intent intent = new Intent(context, UploadZipService.class);
                intent.putExtra("sessionId", eva.sessionIdentifier);
                intent.putExtra("projectId", eva.getProjectId());
                intent.putExtra("errorId", eva.getIdentifier());
                intent.putExtra("fps", 3);
                intent.putExtra(LibConstants.EVA, eva);
                context.startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
