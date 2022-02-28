package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.request.DocumentTransaction;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DocumentDownloadResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("object")
    @Expose
    private DownloadDoc docs;

    public DownloadDoc getDocs() {
        return docs;
    }

    public void setDocs(DownloadDoc docs) {
        this.docs = docs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
