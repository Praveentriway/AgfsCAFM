package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;

import java.util.List;

@Dao
public interface RCRespondDao {

    @Query("SELECT * FROM ReceiveComplaintRespondEntity")
    List<ReceiveComplaintRespondEntity> getAll();

    @Query("SELECT COUNT(*) from ReceiveComplaintRespondEntity")
    int count();

    @Query("SELECT COUNT(*) from ReceiveComplaintRespondEntity where complaintNumber LIKE  :complaintNumber")
    int countByWebNumber(String complaintNumber);

    @Query("SELECT * FROM ReceiveComplaintRespondEntity where complaintNumber LIKE  :complaintNumber")
    ReceiveComplaintRespondEntity findByWebNo(String complaintNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComplaint(ReceiveComplaintRespondEntity... logComplaintEntities);

    @Update
    void updateLogComplaint(ReceiveComplaintRespondEntity... logComplaintEntities);

    @Delete
    void delete(ReceiveComplaintRespondEntity categoryEntity);

    @Query("DELETE FROM ReceiveComplaintRespondEntity where complaintNumber LIKE  :complaintNumber")
    void deleteSingleComplaint(String complaintNumber);

    @Query("DELETE FROM ReceiveComplaintRespondEntity")
    void deleteAll();
}
