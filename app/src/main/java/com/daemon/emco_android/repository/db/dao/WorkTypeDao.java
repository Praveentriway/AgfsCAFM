package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.WorkTypeEntity;

import java.util.List;

@Dao
public interface WorkTypeDao {

    @Query("SELECT * FROM WorkTypeEntity")
    List<WorkTypeEntity> getAll();

    @Query("SELECT COUNT(*) from WorkTypeEntity")
    int count();

    @Query("SELECT COUNT(*) from WorkTypeEntity where code LIKE  :code")
    int countByNatureCode(String code);

    @Query("SELECT * FROM WorkTypeEntity where code LIKE  :code")
    WorkTypeEntity findByCategoryCode(String code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WorkTypeEntity> typeEntities);

    @Delete
    void delete(WorkTypeEntity workTypeEntity);

    @Query("DELETE FROM WorkTypeEntity")
    void deleteAll();
}
