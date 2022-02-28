package com.daemon.emco_android.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;

import java.util.List;

@Dao
public interface RCViewAssetDetailsDao {

  @Query("SELECT * FROM AssetDetailsEntity")
  List<AssetDetailsEntity> getAll();

  @Query(
      "SELECT * FROM AssetDetailsEntity WHERE assetBarCode LIKE :assetBarCode AND zoneCode =:zoneco AND opco =:opco AND jobNo=:jobno AND buildingCode =:buildingco")
  List<AssetDetailsEntity> getReceiveComplaintViewAssetDetailsEntity(
      String assetBarCode, String opco, String jobno, String zoneco, String buildingco);

  @Query("SELECT COUNT(*) from AssetDetailsEntity")
  int count();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<AssetDetailsEntity> receiveComplaintViewEntities);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertSingle(AssetDetailsEntity receiveComplaintViewEntities);

  @Delete
  void delete(AssetDetailsEntity receiveComplaintViewEntity);

  @Delete
  void delete(List<AssetDetailsEntity> receiveComplaintViewEntity);

  @Query("DELETE FROM AssetDetailsEntity")
  void deleteAll();
}
