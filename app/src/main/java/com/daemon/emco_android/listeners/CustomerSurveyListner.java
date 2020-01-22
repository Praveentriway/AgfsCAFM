package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.ServeyQuestionnaire;
import com.daemon.emco_android.repository.db.entity.SurveyContract;
import com.daemon.emco_android.repository.db.entity.SurveyCustomer;
import com.daemon.emco_android.repository.db.entity.SurveyMaster;

import java.util.List;

public interface CustomerSurveyListner {

    void onReceiveSurveyCustomer(List<SurveyCustomer> cutomers, int mode);

    void onReceiveFailureSurveyCustomer(String strErr);

    void onReceiveSurveyContract(List<SurveyContract> contracts, int mode);

    void onReceiveFailureSurveyContract(String strErr);


    void onReceiveSurveyRefernce(List<SurveyMaster> refernces, int mode);

    void onReceiveFailureSurveyRefernce(String strErr);

    void onReceiveSurveyQuestionnaire(List<ServeyQuestionnaire> questions, int mode);

    void onReceiveFailureSurveyQuestionnaire(String strErr);


}
