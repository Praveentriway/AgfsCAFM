package com.daemon.emco_android.listeners;

import com.daemon.emco_android.model.response.LCUserInput;

import java.util.List;

/** Created by vikram on 19/7/17. */
public interface LCUserInputListener {
  void onLCUserInputReceivedSuccess(List<LCUserInput> lcUserInput, int mode);

  void onLCUserInputReceivedFailure(String strErr, int mode);
}