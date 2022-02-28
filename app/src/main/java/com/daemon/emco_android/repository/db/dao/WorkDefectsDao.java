package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.WorkDefectEntity;

import java.util.List;

@Dao
public interface WorkDefectsDao {

    @Query("SELECT * FROM workDefectEntity")
    List<WorkDefectEntity> getAll();

    @Query("SELECT COUNT(*) from workDefectEntity")
    int count();

    @Query("SELECT * FROM workDefectEntity where complaintCode LIKE  :complaintCode AND workCategory LIKE  :workCategory")
    List<WorkDefectEntity> findByWorkDefectCode(String complaintCode, String workCategory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<WorkDefectEntity> workDoneEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WorkDefectEntity> workDoneEntities);

    @Delete
    void delete(WorkDefectEntity workDoneEntity);

    @Query("DELETE FROM workDefectEntity")
    void deleteAll();
}
