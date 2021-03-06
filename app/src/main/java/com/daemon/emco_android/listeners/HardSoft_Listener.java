package com.daemon.emco_android.listeners;

import com.daemon.emco_android.model.response.HardSoftView;

/** Created by Daemonsoft on 10/30/2017. */
public interface HardSoft_Listener {
  void onHardSoftReceivedSuccess(HardSoftView hardSoftView, int mode);

  void onHardSoftReceivedFailure(String strErr, int mode);

  void onHardSoftSaveSucess(String strErr);
}
