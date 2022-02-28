package com.daemon.emco_android.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.MaterialMasterEntity;

import java.util.List;

@Dao
public interface RCMaterialDao {

  @Query("SELECT * FROM MaterialMaster")
  List<MaterialMasterEntity> getAll();

  @Query(
      "SELECT * FROM MaterialMaster WHERE materialName LIKE :searchByName LIMIT 10 OFFSET :startIndex")
  List<MaterialMasterEntity> getByMaterialName(String searchByName, int startIndex);

  @Query("SELECT COUNT(*) from MaterialMaster where materialName LIKE :searchByName ")
  int count(String searchByName);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<MaterialMasterEntity> entities);

  @Delete
  void delete(MaterialMasterEntity entity);

  @Query("DELETE FROM MaterialMaster")
  void deleteAll();
}
