package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.ComplaintStatusEntity;

import java.util.List;

@Dao
public interface receiveComplaintViewDaoComplaint {

    @Query("SELECT * FROM receiveComplaintsStatusUpdate")
    ComplaintStatusEntity getAll();

    /*@Query("SELECT * FROM ReceiveComplaintViewEntity WHERE opco LIKE :mopco AND complaintSite LIKE :mcomplaintSite AND complaintNumber LIKE :mcomplaintNumber")
    List<ReceiveComplaintViewEntity> getReceiveComplaintViewEntity(String mopco, String mcomplaintSite, String mcomplaintNumber);*/

  /*  @Query("SELECT * FROM ReceiveComplaintViewEntity where complaintNumber LIKE :complaintNumber")
    ReceiveComplaintViewEntity findBycomplaintNumber(String complaintNumber);*/

    @Query("SELECT COUNT(*) from receiveComplaintsStatusUpdate")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ComplaintStatusEntity ComplaintStatusEntity);

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingle(ReceiveComplaintViewEntity receiveComplaintViewEntities);*/

    @Delete
    void delete(ComplaintStatusEntity receiveComplaintViewEntity);

    @Delete
    void delete(List<ComplaintStatusEntity> receiveComplaintViewEntity);

    @Query("DELETE FROM receiveComplaintsStatusUpdate")
    void deleteAll();
}
