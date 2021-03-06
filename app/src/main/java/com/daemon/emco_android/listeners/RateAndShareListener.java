package com.daemon.emco_android.listeners;

import java.util.List;

/**
 * Created by vikram on 28/7/17.
 */

public interface RateAndShareListener {
    void onCustomerRemarksReceived(List<String> customerRemarks);
    void onRateShareReceivedError(String strErr);
    void onSaveRateShareReceived(String strErr);
}
