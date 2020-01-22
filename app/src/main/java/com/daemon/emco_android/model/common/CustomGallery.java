package com.daemon.emco_android.model.common;

/**
 * Created by Daemonsoft on 8/7/2017.
 */

public class CustomGallery {
    private String sdcardPath;
    private boolean isSeleted;

    public String getSdcardPath() {
        return sdcardPath;
    }

    public void setSdcardPath(String sdcardPath) {
        this.sdcardPath = sdcardPath;
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }
}
