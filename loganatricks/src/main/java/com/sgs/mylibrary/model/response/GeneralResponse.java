package com.sgs.mylibrary.model.response;

/**
 * GeneralResponse is used for the api success response.
 */
public class GeneralResponse {

    private String message;

    /**
     * @return method will return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * method is used for setting the string
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
