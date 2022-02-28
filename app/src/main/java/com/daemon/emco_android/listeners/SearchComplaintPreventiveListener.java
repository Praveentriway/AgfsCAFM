package com.daemon.emco_android.listeners;


import com.daemon.emco_android.model.common.PPMDetails;

import java.util.List;

/**
 * Created by vikram on 17/7/17.
 */

public interface SearchComplaintPreventiveListener {
    void onComplaintReceivedSuccess(List<PPMDetails> searchComplaintEntityList);
    void onComplaintReceivedFailure(String strErr);
}
