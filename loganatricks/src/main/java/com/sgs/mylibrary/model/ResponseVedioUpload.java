package com.sgs.mylibrary.model;

/**
 * ResponseVedioUpload class is uploadApi response
 */
public class ResponseVedioUpload {

    private UploadFile uploadFile;

    /**
     * @return
     */
    public UploadFile getUploadFile() {
        return uploadFile;
    }

    /**
     * @param uploadFile
     */
    public void setUploadFile(UploadFile uploadFile) {
        this.uploadFile = uploadFile;
    }

    @Override
    public String toString() {
        return "ResponseVedioUpload{" +
                "uploadFile=" + uploadFile +
                '}';
    }
}
