package com.daemon.emco_android.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vikram on 17/7/17.
 */

public class FileSizeRequest {

    public FileSizeRequest(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @SerializedName("fileSize")
    private String fileSize;





}
