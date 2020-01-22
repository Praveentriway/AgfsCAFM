package com.daemon.emco_android.listeners;

import java.util.List;

/**
 * Created by vikram on 28/7/17.
 */

public interface TechRemarksListener {
    void onTechRemarksReceived(List<String> technicalRemarks);
    void onechRemarksReceivedError(String strErr);
}
