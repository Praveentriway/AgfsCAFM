package com.daemon.emco_android.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.repository.db.entity.SaveRatedServiceEntity;

import java.util.List;

@Dao
public interface receiveComplaintItemDao {

  @Query("SELECT * FROM ReceiveComplaintItemEntity")
  List<ReceiveComplaintItemEntity> getAll();

  @Query("SELECT * FROM ReceiveComplaintItemEntity LIMIT 20 OFFSET :startIndex")
  List<ReceiveComplaintItemEntity> getList(int startIndex);

  @Query("SELECT COUNT(*) from ReceiveComplaintItemEntity")
  int count();

  @Query(
      "UPDATE ReceiveComplaintItemEntity SET status = 'R', statusDesription = 'Responded' WHERE opco =:opco AND complaintNumber =:cNo AND siteCode =:scode")
  void updateStatus(String opco, String cNo, String scode);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(List<ReceiveComplaintItemEntity> receiveComplaintItemEntities);

  @Delete
  void delete(ReceiveComplaintItemEntity receiveComplaintItemEntity);

  @Query("DELETE FROM ReceiveComplaintItemEntity")
  void deleteAll();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(SaveRatedServiceEntity entity);

  @Query("SELECT * FROM SaveRatedServiceEntity")
  List<SaveRatedServiceEntity> getAllSaveRatedServiceEntity();

  @Query(
      "DELETE FROM ReceiveComplaintItemEntity where  opco =:opco AND complaintNumber =:cNo AND siteCode =:scode")
  void deleteSingleComplaint(String opco, String cNo, String scode);

  @Delete
  void delete(SaveRatedServiceEntity receiveComplaintItemEntity);
}
