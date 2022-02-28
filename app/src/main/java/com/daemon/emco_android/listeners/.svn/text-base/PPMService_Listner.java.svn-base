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

    void onReceivedPPMParameterSucess(GetPpmParamValue customerRemarks);
    void onReceivedSucess(List<ObjectMonthly> customerRemarks);
    void onGetSavedDataSucess(List<ObjectSavedCheckListResponse> customerRemarks);
    void onReceivedSavedSucess(String customerRemarks);
    void onReceiveFailure(String strErr);
}
