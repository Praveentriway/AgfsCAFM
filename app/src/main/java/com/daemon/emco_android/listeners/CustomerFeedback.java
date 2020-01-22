package com.daemon.emco_android.listeners;

import com.daemon.emco_android.model.request.SaveRatedServiceRequest;

public interface CustomerFeedback {

    void onCustomerFeedbackReceived(SaveRatedServiceRequest customerRemarks);
    void onCustomerFeedbackReceivedError(String strErr);

}
