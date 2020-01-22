package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.WorkStatusEntity;

import java.util.List;

@Dao
public interface WorkStatusDao {

    @Query("SELECT * FROM workStatusEntity")
    List<WorkStatusEntity> getAll();

    @Query("SELECT COUNT(*) from workStatusEntity")
    int count();

    @Query("SELECT COUNT(*) FROM workStatusEntity where pendingCode LIKE  :pendingCode")
    int countByPendingCode(String pendingCode);

    @Query("SELECT * FROM workStatusEntity where pendingCode LIKE  :pendingCode")
    WorkStatusEntity findByWorkPendingCode(String pendingCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WorkStatusEntity> workStatusEntities);

    @Delete
    void delete(WorkStatusEntity workStatusEntity);

    @Query("DELETE FROM workStatusEntity")
    void deleteAll();
}
