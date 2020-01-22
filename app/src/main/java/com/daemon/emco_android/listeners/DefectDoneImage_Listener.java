package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.model.response.DefectDoneImageUploaded;
import com.daemon.emco_android.model.response.RCDownloadImage;

import java.util.List;

/**
 * Created by Daemonsoft on 8/21/2017.
 */

public interface DefectDoneImage_Listener {
    void onImageSaveReceivedSuccess1(RCDownloadImage imageEntity, int mode);

    void onImageSaveReceivedSuccess(DefectDoneImageUploaded mImageEntity, int mode);

    void onImageReceivedSuccess(RCDownloadImage imageEntity, int mode);

    void onImageSaveReceivedFailure(String strErr, int mode);
    void onImageReceivedFailure(String strErr, int mode);

    void onAllImagesReceived(List<DFoundWDoneImageEntity> mImageEntities, int mode);
}
