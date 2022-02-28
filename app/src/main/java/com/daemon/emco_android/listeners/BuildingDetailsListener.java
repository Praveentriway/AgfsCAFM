package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.BuildingDetailsEntity;

import java.util.List;

/**
 * Created by vikram on 19/7/17.
 */

public interface BuildingDetailsListener {
    void onBuildingDetailsReceivedSuccess(List<BuildingDetailsEntity> buildingDetailsEntities, int mode);
    void onBuildingDetailsReceivedFailure(String strErr, int mode);
}
