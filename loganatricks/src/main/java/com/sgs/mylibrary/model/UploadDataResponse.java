package com.sgs.mylibrary.model;

/**
 * UploadDataResponse class is uploadPacket api response
 */
public class UploadDataResponse {
    private Response result;

    /**
     * @return
     */
    public Response getResponse() {
        return result;
    }

    /**
     * @param response
     */
    public void setResponse(Response response) {
        this.result = response;
    }
}
