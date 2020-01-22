package com.daemon.emco_android.listeners;

/**
 * Created by vikram on 12/3/18.
 */

public interface CheckCustomerSignListener
{
    void onCheckSucess(String s);

    void onCategoryReceivedFailure(String strErr);
}
