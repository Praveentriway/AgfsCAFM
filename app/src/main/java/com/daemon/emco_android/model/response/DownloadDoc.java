package com.daemon.emco_android.model.response;

import com.daemon.emco_android.model.request.DocumentTransaction;

import java.util.ArrayList;

public class DownloadDoc {

    private ArrayList<DocumentTransaction> base64Docs;
    private String type;
    private int docCount;

    public ArrayList<DocumentTransaction> getBase64Docs() {
        return base64Docs;
    }

    public void setBase64Docs(ArrayList<DocumentTransaction> base64Docs) {
        this.base64Docs = base64Docs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDocCount() {
        return docCount;
    }

    public void setDocCount(int docCount) {
        this.docCount = docCount;
    }
}
