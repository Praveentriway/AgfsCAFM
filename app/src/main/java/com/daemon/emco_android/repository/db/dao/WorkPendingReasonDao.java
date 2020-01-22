package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.WorkPendingReasonEntity;

import java.util.List;

@Dao
public interface WorkPendingReasonDao {

    @Query("SELECT * FROM workPendingReasonEntity")
    List<WorkPendingReasonEntity> getAll();

    @Query("SELECT COUNT(*) from workPendingReasonEntity")
    int count();

    @Query("SELECT COUNT(*) FROM workPendingReasonEntity where reasonCode LIKE  :reasonCode")
    int countByWorkReasonCode(String reasonCode);

    @Query("SELECT * FROM workPendingReasonEntity where reasonCode LIKE  :reasonCode")
    WorkPendingReasonEntity findByWorkReasonCode(String reasonCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WorkPendingReasonEntity> workPendingReasonEntities);

    @Delete
    void delete(WorkPendingReasonEntity workPendingReasonEntity);

    @Query("DELETE FROM workPendingReasonEntity")
    void deleteAll();
}
