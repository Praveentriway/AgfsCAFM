package com.daemon.emco_android.repository.db.Tdata;

import androidx.paging.TiledDataSource;
import android.util.Log;

import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.repository.remote.restapi.ApiConstant;
import com.daemon.emco_android.repository.remote.restapi.ApiInterface;
import com.daemon.emco_android.model.response.ReceiveComplaintResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by Daemonsoft on 10/13/2017.
 */

public class ReciveComplaint extends TiledDataSource<ReceiveComplaintItemEntity> {
    private static final String TAG = ReciveComplaint.class.getSimpleName();
    private ApiInterface mInterface;
    private String strEmpid, status;

    public ReciveComplaint(String strEmpid, String status) {
        this.strEmpid = strEmpid;
        this.status = status;
       // mInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public int countItems() {
        return 0;
    }

    @Override
    public List<ReceiveComplaintItemEntity> loadRange(int startIndex, int count) {
        List<ReceiveComplaintItemEntity> entityList = new ArrayList();
        try {
            Response<ReceiveComplaintResponse> response = mInterface.getReceiveComplaintListResult(strEmpid, status, startIndex, count).execute();
            Log.d("Bug12","Bug_Track_1");
            if (response.isSuccessful() && response.code() == 200) {
                if (response.body().getStatus().equalsIgnoreCase(ApiConstant.SUCCESS)) {
                    Log.d(TAG, "onResponse success");
                    entityList.addAll(response.body().getReceiveComplaintItem());
                }
            } else {
                Log.e("API CALL", response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entityList;
    }

}
