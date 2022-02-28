package com.daemon.emco_android.listeners;

import java.util.List;

public interface PendingReasonsListner {

        void onPendingReasonsReceived(List<String> technicalRemarks);
        void onPendingReasonsReceivedError(String strErr);

}
