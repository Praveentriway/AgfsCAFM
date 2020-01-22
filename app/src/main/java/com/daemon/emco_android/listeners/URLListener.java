package com.daemon.emco_android.listeners;

import com.daemon.emco_android.model.response.CommonResponse;

public interface URLListener {

    void onUrlreceivedsucess(CommonResponse response);

    void onUrlreceivedFailure(String strErr);
}
