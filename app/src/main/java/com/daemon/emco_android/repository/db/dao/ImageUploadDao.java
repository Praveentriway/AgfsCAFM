package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;

import java.util.List;

@Dao
public interface ImageUploadDao {

    @Query("SELECT * FROM DFoundWDoneImageEntity")
    List<DFoundWDoneImageEntity> getAll();

    @Query("SELECT COUNT(*) from DFoundWDoneImageEntity")
    int count();

    @Query("SELECT COUNT(*) from DFoundWDoneImageEntity where id LIKE  :id")
    int countBycomplaintNo(String id);

    @Query("SELECT * FROM DFoundWDoneImageEntity where id LIKE  :id")
    DFoundWDoneImageEntity findByWebNo(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComplaintImage(DFoundWDoneImageEntity... imageEntities);

    @Update
    void updateComplaintImage(DFoundWDoneImageEntity imageEntities);

    @Delete
    void delete(DFoundWDoneImageEntity imageEntity);

    @Query("DELETE FROM DFoundWDoneImageEntity where id LIKE  :id")
    void deleteSingleComplaint(String id);

    @Query("DELETE FROM DFoundWDoneImageEntity")
    void deleteAll();
}
