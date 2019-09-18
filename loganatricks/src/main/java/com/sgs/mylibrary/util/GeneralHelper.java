package com.sgs.mylibrary.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;


public final class GeneralHelper {

    Context context;
    private static GeneralHelper instance;

    private GeneralHelper(Context context) {
        this.context = context;
    }

    public static GeneralHelper getInstance(Context context) {
        if (instance == null)
            instance = new GeneralHelper(context);
        return instance;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        // check for null is mandatory as in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public String getNetworkType() {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            if (wifiInfo.getNetworkId() != -1) {
                return "wifi";
            }
        }
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "cellular";
            default:
                return "wifi";
        }
    }

    public String getNetworkConnectionType() {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "cellular";
            default:
                return "wifi";

        }
    }

    public String getAppVersion() {
        String version = Constant.DEFAULT_APP_VERSION;
        try {
            PackageInfo pInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            if (Constant.SDK_MODE.contentEquals(Constant.Build.TYPE.DEV.name())) {
                e.printStackTrace();
            }
        }
        return version;
    }

    public String getSDKVersion() {
        String version = Constant.DEFAULT_SDK_VERSION;
        try {
            PackageInfo pInfo = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            if (Constant.SDK_MODE.contentEquals(Constant.Build.TYPE.DEV.name())) {
                e.printStackTrace();
            }
        }
        return version;
    }


    public String generateURL(String url, String[] urlValues) {
        String[] splits = url.split("#");
        String genUrl = new String();
        if (urlValues.length == splits.length) {
            for (int i = 0; i < splits.length; i++) {
                if (Constant.SDK_MODE.contentEquals(Constant.Build.TYPE.DEV.name())) {
                    System.out.println(splits[i]);
                }
                genUrl += splits[i] + urlValues[i];
            }
            return genUrl;
        } else {
            return null;
        }
    }

}
