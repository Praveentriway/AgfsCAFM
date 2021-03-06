package com.daemon.emco_android.listeners;


import com.daemon.emco_android.model.response.DashboardPieData;

import java.util.ArrayList;

/**
 * Created by vikram on 17/8/17.
 */

public interface ReactiveDashboard_Listener {

    void onDashboardDataReceivedSuccess(ArrayList<DashboardPieData> responses, int mode);

    void onDashboardDataReceivedFailure(String strErr, int mode);
}
