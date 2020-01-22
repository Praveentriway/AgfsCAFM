package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.PPENameEntity;

import java.util.List;

@Dao
public interface PpeNameDao {

    @Query("SELECT * FROM ppeNameEntity")
    List<PPENameEntity> getAll();

    @Query("SELECT COUNT(*) from ppeNameEntity")
    int count();

    @Query("SELECT COUNT(*) FROM ppeNameEntity where code LIKE  :code")
    int countByCode(String code);

    @Query("SELECT * FROM ppeNameEntity where code LIKE  :code")
    PPENameEntity findByCode(String code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PPENameEntity> ppeNameEntities);

    @Delete
    void delete(PPENameEntity ppeNameEntity);

    @Query("DELETE FROM ppeNameEntity")
    void deleteAll();
}
