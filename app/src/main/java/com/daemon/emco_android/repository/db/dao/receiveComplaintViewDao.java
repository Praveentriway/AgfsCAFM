package com.daemon.emco_android.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;

import java.util.List;

@Dao
public interface receiveComplaintViewDao {

  @Query("SELECT * FROM ReceiveComplaintViewEntity WHERE mode =:mode")
  List<ReceiveComplaintViewEntity> getAll(int mode);

  @Query(
      "SELECT * FROM ReceiveComplaintViewEntity WHERE opco LIKE :mopco AND complaintSite LIKE :mcomplaintSite AND complaintNumber LIKE :mcomplaintNumber")
  List<ReceiveComplaintViewEntity> getReceiveComplaintViewEntity(
      String mopco, String mcomplaintSite, String mcomplaintNumber);

  // @Query("SELECT * FROM ReceiveComplaintViewEntity where complaintNumber LIKE :complaintNumber")
  // List<ReceiveComplaintViewEntity> findBycomplaintNumber(String complaintNumber);

  @Query("SELECT COUNT(*) from ReceiveComplaintViewEntity")
  int count();

  @Query(
      "UPDATE ReceiveComplaintViewEntity SET complaintStatus = 'R', responseDetails =:red WHERE opco =:opco AND complaintNumber =:cNo AND complaintSite =:scode")
  void updateStatus(String opco, String cNo, String scode, String red);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<ReceiveComplaintViewEntity> receiveComplaintViewEntities);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertSingle(ReceiveComplaintViewEntity receiveComplaintViewEntities);

  @Delete
  void delete(ReceiveComplaintViewEntity receiveComplaintViewEntity);

  @Delete
  void delete(List<ReceiveComplaintViewEntity> receiveComplaintViewEntity);

  @Query("DELETE FROM ReceiveComplaintViewEntity")
  void deleteAll();
}
