package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.daemon.emco_android.repository.db.entity.LogComplaintEntity;

import java.util.List;

@Dao
public interface LogComplaintDao {

    @Query("SELECT * FROM logComplaintEntity")
    List<LogComplaintEntity> getAll();

    @Query("SELECT COUNT(*) from logComplaintEntity")
    int count();

    @Query("SELECT COUNT(*) from logComplaintEntity where complainWebNumber LIKE  :complainWebNumber")
    int countByWebNumber(String complainWebNumber);

    @Query("SELECT * FROM logComplaintEntity where complainWebNumber LIKE  :complainWebNumber")
    LogComplaintEntity findByWebNo(String complainWebNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComplaint(LogComplaintEntity... logComplaintEntities);

    @Update
    void updateLogComplaint(LogComplaintEntity... logComplaintEntities);

    @Delete
    void delete(LogComplaintEntity categoryEntity);

    @Query("DELETE FROM logComplaintEntity where complainWebNumber LIKE  :uniqeWebNo")
    void deleteSingleComplaint(String uniqeWebNo);
}
