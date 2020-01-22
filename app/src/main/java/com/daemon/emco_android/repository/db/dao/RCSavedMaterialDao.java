package com.daemon.emco_android.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;

import java.util.List;

@Dao
public interface RCSavedMaterialDao {

  @Query("SELECT * FROM SaveMaterialEntity WHERE mode LIKE :mode")
  List<SaveMaterialEntity> getAll(int mode);

  @Query(
      "SELECT * FROM SaveMaterialEntity WHERE transactionType LIKE :transactionType AND complaintNumber LIKE :complainNumber AND complainSite LIKE :complainSite AND companyCode LIKE :companyCode LIMIT 20 OFFSET :startIndex")
  List<SaveMaterialEntity> getMaterialBY(
      String transactionType,
      String complainNumber,
      String complainSite,
      String companyCode,
      int startIndex);

  @Query(
      "SELECT COUNT(*) from SaveMaterialEntity where transactionType LIKE :transactionType AND complaintNumber AND complainSite LIKE :complainSite AND companyCode LIKE :companyCode LIKE :complainNumber")
  int countBY(
      String transactionType, String complainNumber, String complainSite, String companyCode);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<SaveMaterialEntity> entities);

  @Query("UPDATE SaveMaterialEntity SET mode =:mode WHERE mode LIKE :oldmode")
  void updateAll(int oldmode, int mode);

  @Delete
  void delete(SaveMaterialEntity entity);

  @Query("DELETE FROM SaveMaterialEntity")
  void deleteAll();
}
