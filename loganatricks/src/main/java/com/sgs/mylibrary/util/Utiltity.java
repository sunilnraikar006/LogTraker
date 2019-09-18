package com.sgs.mylibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility
 */
public class Utiltity {

    /**
     * method will return string encoded in md5 format
     * @param s
     * @return
     */
    public static String getUniqueId( String s) {
        String var = s + getCurrentTime();
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(var.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }

    /**
     * method will return currentTime in timestamp format
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        // check for null is mandatory as in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

}
