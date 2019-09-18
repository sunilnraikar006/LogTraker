package com.sgs.mylibrary.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * PacketData class used for capturing below parameters
 *      * @param et
 *      * @param t
 *      * @param title
 *      * @param x
 *      * @param y
 *      * @param cn
 *      * @param ct
 *      * @param dt
 *      * @param dm
 *      * @param dos
 *      * @param width
 *      * @param height
 *      * @param text
 *      * @param r
 *      * @param name
 *      * @param details
 *      * @param id
 *      * @param p
 *      * @param av
 *      and used for uploading packet data to server using UploadDataService class
 */
public class PacketData implements Parcelable {
    private int et;
    private String t;
    private String title;
    private double x;
    private double y;
    private String cn;
    private String ct;
    private String dt;
    private String dm;
    private String dos;
    private double width;
    private double height;
    private String text;
    private String r;
    private String name;
    private String details;
    private String id;
    private String p;
    private String av;

    public PacketData(Parcel in) {
        et = in.readInt();
        t = in.readString();
        title = in.readString();
        x = in.readDouble();
        y = in.readDouble();
        cn = in.readString();
        ct = in.readString();
        dt = in.readString();
        dm = in.readString();
        dos = in.readString();
        width = in.readDouble();
        height = in.readDouble();
        text = in.readString();
        r = in.readString();
        name = in.readString();
        details = in.readString();
        id = in.readString();
        p = in.readString();
        av = in.readString();
    }

    public static final Creator<PacketData> CREATOR = new Creator<PacketData>() {
        @Override
        public PacketData createFromParcel(Parcel in) {
            return new PacketData(in);
        }

        @Override
        public PacketData[] newArray(int size) {
            return new PacketData[size];
        }
    };

    public PacketData() {

    }

    /**
     * @return
     */
    public int getEt() {
        return et;
    }

    /**
     * @param et
     */
    public void setEt(int et) {
        this.et = et;
    }

    /**
     * @return
     */
    public String getT() {
        return t;
    }

    /**
     * @param t
     */
    public void setT(String t) {
        this.t = t;
    }

    /**
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return
     */
    public double getX() {
        return x;
    }

    /**
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return
     */
    public double getY() {
        return y;
    }

    /**
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return
     */
    public String getCn() {
        return cn;
    }

    /**
     * @param cn
     */
    public void setCn(String cn) {
        this.cn = cn;
    }

    /**
     * @return
     */
    public String getCt() {
        return ct;
    }

    /**
     * @param ct
     */
    public void setCt(String ct) {
        this.ct = ct;
    }

    /**
     * @return
     */
    public String getDt() {
        return dt;
    }

    /**
     * @param dt
     */
    public void setDt(String dt) {
        this.dt = dt;
    }

    /**
     * @return
     */
    public String getDm() {
        return dm;
    }

    /**
     * @param dm
     */
    public void setDm(String dm) {
        this.dm = dm;
    }

    /**
     * @return
     */
    public String getDos() {
        return dos;
    }

    /**
     * @param dos
     */
    public void setDos(String dos) {
        this.dos = dos;
    }

    /**
     * @return
     */
    public double getWidth() {
        return width;
    }

    /**
     * @param width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * @return
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return
     */
    public String getR() {
        return r;
    }

    /**
     * @param r
     */
    public void setR(String r) {
        this.r = r;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getP() {
        return p;
    }

    /**
     * @param p
     */
    public void setP(String p) {
        this.p = p;
    }

    /**
     * @return
     */
    public String getAv() {
        return av;
    }

    /**
     * @param av
     */
    public void setAv(String av) {
        this.av = av;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(et);
        parcel.writeString(t);
        parcel.writeString(title);
        parcel.writeDouble(x);
        parcel.writeDouble(y);
        parcel.writeString(cn);
        parcel.writeString(ct);
        parcel.writeString(dt);
        parcel.writeString(dm);
        parcel.writeString(dos);
        parcel.writeDouble(width);
        parcel.writeDouble(height);
        parcel.writeString(text);
        parcel.writeString(r);
        parcel.writeString(name);
        parcel.writeString(details);
        parcel.writeString(id);
        parcel.writeString(p);
        parcel.writeString(av);
    }
}
