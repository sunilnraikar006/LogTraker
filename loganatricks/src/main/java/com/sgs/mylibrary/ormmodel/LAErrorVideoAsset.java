package com.sgs.mylibrary.ormmodel;


import android.os.Parcel;
import android.os.Parcelable;

import com.sgs.mylibrary.orm.SugarRecord;

/**
 * LAErrorVideoAsset orm sugarRecord
 */
public class LAErrorVideoAsset extends SugarRecord implements Parcelable {
    public String fileName;
    public long startTime;
    public long endTime;
    public int isUploaded;
    public String sessionIdentifier;
    public String identifier;
    public String fileUrl;
    public int endFileIndex;
    public int startFileIndex;
    public int uploadingStatus;
    public int isCreated;
    public double viewHeight;
    public double viewWidth;
    public double scale;
    public String projectId;

    public LAErrorVideoAsset() {

    }

    /**
     * @param projectId
     * @param fileName
     * @param startTime
     * @param endTime
     * @param isSaved
     * @param isUploaded
     * @param isUploading
     * @param sessionIdentifier
     * @param fileUrl
     */
    public LAErrorVideoAsset(String projectId, String fileName, long startTime,
                             long endTime, int isSaved,
                             int isUploaded, int isUploading,
                             String sessionIdentifier, String fileUrl) {
        this.projectId = projectId;
        this.fileName = fileName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isUploaded = isUploaded;
        this.sessionIdentifier = sessionIdentifier;
        this.fileUrl = fileUrl;
    }

    /**
     * @param sessionIdentifier
     * @param endFileIndex
     * @param startFileIndex
     * @param uploadingStatus
     * @param isCreated
     * @param viewHeight
     * @param viewWidth
     * @param scale
     */
    public LAErrorVideoAsset(String sessionIdentifier, int endFileIndex,
                             int startFileIndex, int uploadingStatus,
                             int isCreated, double viewHeight,
                             double viewWidth, double scale) {
        this.sessionIdentifier = sessionIdentifier;
        this.endFileIndex = endFileIndex;
        this.startFileIndex = startFileIndex;
        this.uploadingStatus = uploadingStatus;
        this.isCreated = isCreated;
        this.viewHeight = viewHeight;
        this.viewWidth = viewWidth;
        this.scale = scale;
    }

    /**
     * @param in
     */
    protected LAErrorVideoAsset(Parcel in) {
        projectId = in.readString();
        fileName = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        isUploaded = in.readInt();
        sessionIdentifier = in.readString();
        identifier = in.readString();
        fileUrl = in.readString();
        endFileIndex = in.readInt();
        startFileIndex = in.readInt();
        uploadingStatus = in.readInt();
        isCreated = in.readInt();
        viewHeight = in.readDouble();
        viewWidth = in.readDouble();
        scale = in.readDouble();
    }

    public static final Creator<LAErrorVideoAsset> CREATOR = new Creator<LAErrorVideoAsset>() {
        @Override
        public LAErrorVideoAsset createFromParcel(Parcel in) {
            return new LAErrorVideoAsset(in);
        }

        @Override
        public LAErrorVideoAsset[] newArray(int size) {
            return new LAErrorVideoAsset[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(projectId);
        dest.writeString(fileName);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeInt(isUploaded);
        dest.writeString(sessionIdentifier);
        dest.writeString(identifier);
        dest.writeString(fileUrl);
        dest.writeInt(endFileIndex);
        dest.writeInt(startFileIndex);
        dest.writeInt(uploadingStatus);
        dest.writeInt(isCreated);
        dest.writeDouble(viewHeight);
        dest.writeDouble(viewWidth);
        dest.writeDouble(scale);
    }

    /**
     * method is used for getting the identifier value
     *
     * @return
     */
    public String getIdentifier() {
        return this.identifier != null ? this.identifier : "";
    }

    /**
     * method to get the project id
     *
     * @return
     */
    public String getProjectId() {
        return this.projectId != null ? this.projectId : "";
    }

    public int getIsUploaded() {
        return isUploaded;
    }

    public int getIsCreated() {
        return isCreated;
    }
}
