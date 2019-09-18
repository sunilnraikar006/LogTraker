package com.sgs.mylibrary.log_interface;

import com.sgs.mylibrary.model.ResponseVedioUpload;

import retrofit2.Response;

/**
 * ZipI interface 
 */
public interface ZipI {
    void vedioUPloaded();
    void vedioUploading();
    void vedioUploadedFailed(Throwable t);
    void vedioUploadedError(Response<ResponseVedioUpload> response);
}
