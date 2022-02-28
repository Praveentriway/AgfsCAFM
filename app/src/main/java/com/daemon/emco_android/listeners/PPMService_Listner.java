package com.daemon.emco_android.listeners;

import com.daemon.emco_android.model.response.CustomerRemarksResponse;
import com.daemon.emco_android.model.response.GetPpmParamValue;
import com.daemon.emco_android.model.response.GetPpmParamValueObject;
import com.daemon.emco_android.model.response.ObjectMonthly;
import com.daemon.emco_android.model.response.ObjectPPMCheckList;
import com.daemon.emco_android.model.response.ObjectSavedCheckListResponse;

import java.util.List;

/**
 * Created by vikram on 11/3/18.
 */

public interface PPMService_Listner
{

    void onReceivedPPMParameterSuccess(GetPpmParamValue customerRemarks);
    void onReceivedSuccess(List<ObjectMonthly> customerRemarks);
    void onGetSavedDataSuccess(List<ObjectSavedCheckListResponse> customerRemarks);
    void onReceivedSavedSuccess(String customerRemarks);
    void onReceiveFailure(String strErr);

}
