package com.sgs.mylibrary.video;

import android.content.Context;

import com.sgs.mylibrary.mpaeg.FFmpeg;


public final class FFMPEGHelper {

    public static FFMPEGHelper instance;
    public Context context;
    public FFmpeg ffmpeg;

    private FFMPEGHelper(Context context) {
        this.context= context;
        ffmpeg = FFmpeg.getInstance(context);
    }

    public static FFMPEGHelper getInstance(Context context) {
        if(instance == null)
            instance = new FFMPEGHelper(context);
        return instance;
    }

    public FFmpeg getFfmpeg() {
        return ffmpeg;
    }


}
