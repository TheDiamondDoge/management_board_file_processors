package com.aiksanov.data;


public class Requirements {
    private String reqId;
    private String headline;
    private String status;

    public Requirements() {
    }

    public Requirements(String reqId, String headline, String status) {
        this.reqId = reqId;
        this.headline = headline;
        this.status = status;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
