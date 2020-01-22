package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.PriorityEntity;

import java.util.List;

@Dao
public interface PriorityDao {

    @Query("SELECT * FROM PriorityEntity")
    List<PriorityEntity> getAll();

    @Query("SELECT COUNT(*) from PriorityEntity")
    int count();

    @Query("SELECT COUNT(*) from PriorityEntity where code LIKE  :code")
    int countByNatureCode(String code);

    @Query("SELECT * FROM PriorityEntity where code LIKE  :code")
    PriorityEntity findByCategoryCode(String code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PriorityEntity> priorityEntities);

    @Delete
    void delete(PriorityEntity priorityEntity);

    @Query("DELETE FROM PriorityEntity")
    void deleteAll();
}
