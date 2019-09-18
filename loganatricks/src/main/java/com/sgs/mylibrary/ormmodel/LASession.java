package com.sgs.mylibrary.ormmodel;


import android.os.Parcel;
import android.os.Parcelable;

import com.sgs.mylibrary.orm.SugarRecord;
import com.sgs.mylibrary.util.Constant;

import org.json.JSONObject;

/**
 * LaSession orm of sugarRecord implementing parcelable interface
 */
public class LASession extends SugarRecord implements Parcelable {
    public String identifier;
    public long endTime;

    public long startTime;
    public int projectId;

    public LASession() {

    }

    public LASession(String identifier, String userId) {
        this.identifier = identifier;
    }

    public LASession(String identifier, String packetId, long endTime,
                     int isCompleted, long startTime,
                     int projectId) {
        this.identifier = identifier;
        this.endTime = endTime;
        this.startTime = startTime;
        this.projectId = projectId;
    }


    protected LASession ( Parcel in ) {
        identifier = in.readString();
        endTime = in.readLong();
        startTime = in.readLong();
        projectId = in.readInt();
    }

    public static final Creator<LASession> CREATOR = new Creator<LASession>() {
        @Override
        public LASession createFromParcel ( Parcel in ) {
            return new LASession(in);
        }

        @Override
        public LASession[] newArray ( int size ) {
            return new LASession[size];
        }
    };

    /**
     * method for getting the identifier value
     * @return
     */
    public String getIdentifier() {
        return identifier != null ? identifier : "";
    }

    @Override
    public String toString() {
        return "LASession{" +
                "identifier='" + identifier + '\'' +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                ", projectId=" + projectId +
                '}';
    }

    @Override
    public int describeContents ( ) {
        return 0;
    }

    @Override
    public void writeToParcel ( Parcel dest, int flags ) {
        dest.writeString(identifier);
        dest.writeLong(endTime);
        dest.writeLong(startTime);
        dest.writeInt(projectId);
    }
}
