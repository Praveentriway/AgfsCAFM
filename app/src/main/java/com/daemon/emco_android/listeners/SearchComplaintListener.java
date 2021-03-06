package com.daemon.emco_android.listeners;


import com.daemon.emco_android.repository.db.entity.MultiSearchComplaintEntity;
import com.daemon.emco_android.repository.db.entity.SingleSearchComplaintEntity;

import java.util.ArrayList;

/**
 * Created by vikram on 17/7/17.
 */

public interface SearchComplaintListener {
    void onComplaintItemClicked(int position);
    void onComplaintReceivedSuccess(ArrayList<MultiSearchComplaintEntity> searchComplaintEntityList, String noOfRows);
    void onSingleComplaintReceivedSuccess(SingleSearchComplaintEntity searchComplaintEntity);
    void onComplaintReceivedFailure(String strErr);
}
