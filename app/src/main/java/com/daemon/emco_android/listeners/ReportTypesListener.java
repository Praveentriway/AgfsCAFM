package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.ReportTypesEntity;

import java.util.List;

/**
 * Created by vikram on 17/8/17.
 */

public interface ReportTypesListener {
    void onReportTypesReceivedSuccess(List<ReportTypesEntity> reportTypesEntities, int mode);

    void onReportTypesDbDataSingleByreportSRL(ReportTypesEntity reportTypesEntity);

    void onReportTypesReceivedFailure(String strErr, int mode);
}
