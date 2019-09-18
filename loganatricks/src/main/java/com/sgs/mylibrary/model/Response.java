package com.sgs.mylibrary.model;

/**
 * Response class is the parent for all the api response
 */
public class Response {
    private String msg;

    /**
     * @return
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
