package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.WorkDoneEntity;

import java.util.List;

@Dao
public interface WorkDoneDao {

    @Query("SELECT * FROM workDoneEntity")
    List<WorkDoneEntity> getAll();

    @Query("SELECT COUNT(*) from workDoneEntity")
    int count();

    @Query("SELECT * FROM workDoneEntity where complaintCode LIKE  :complaintCode AND defectCode LIKE  :defectCode AND workCategory LIKE  :workCategory")
    List<WorkDoneEntity> findByDefectCode(String complaintCode, String defectCode, String workCategory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WorkDoneEntity> workDoneEntities);

    @Delete
    void delete(WorkDoneEntity workDoneEntity);

    @Query("DELETE FROM workDoneEntity")
    void deleteAll();
}
