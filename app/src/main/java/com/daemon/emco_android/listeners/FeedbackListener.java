package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.daemon.emco_android.model.response.PpmEmployeeFeedResponse;

import java.util.List;

/**
 * Created by vikram on 17/7/17.
 */

public interface FeedbackListener {
    void onFeedbackEmployeeDetailsReceivedSuccess(List<EmployeeDetailsEntity> employeeDetailsEntities, int mode);
    void onFeedbackDetailsReceivedSuccess(SaveFeedbackEntity saveFeedbackEntity, int mode);
    void onAllFeedbackDetailsReceivedSuccess(List<SaveFeedbackEntity> saveFeedbackEntities, int mode);
    void onFeedbackDetailsReceivedSuccessPPMAtten(PpmEmployeeFeedResponse saveFeedbackEntity);
    void onFeedbackEmployeeDetailsSaveSuccess (String strMsg, int mode);
    void onFeedbackPpmStatusSucess (List<String> strMsg, int mode);
    void onFeedbackEmployeeDetailsReceivedFailure(String strErr, int mode);
}
