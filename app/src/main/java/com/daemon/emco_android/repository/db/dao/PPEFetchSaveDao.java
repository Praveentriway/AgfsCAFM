package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.utils.AppUtils;

import java.util.List;

@Dao
public interface PPEFetchSaveDao {

    @Query("SELECT * FROM PPEFetchSaveEntity where mode LIKE :mode")
    List<PPEFetchSaveEntity> getAll(int mode);

    @Query("SELECT COUNT(*) from PPEFetchSaveEntity")
    int count();

    @Query("SELECT COUNT(*) from PPEFetchSaveEntity where complainNumber LIKE  :complainNumber")
    int countBycomplainNumber(String complainNumber);

    @Query("SELECT * FROM PPEFetchSaveEntity where complainNumber LIKE  :complainNumber")
    List<PPEFetchSaveEntity> findBycomplainNumber(String complainNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPPE(List<PPEFetchSaveEntity> ppeFetchSaveEntities);

    @Query("UPDATE PPEFetchSaveEntity SET mode = '" + AppUtils.MODE_SERVER + "' WHERE mode = " + AppUtils.MODE_LOCAL)
    void updateAll();

    @Delete
    void delete(List<PPEFetchSaveEntity> ppeFetchSaveEntities);

    @Query("DELETE FROM PPEFetchSaveEntity where complainNumber LIKE  :complainNumber")
    void deleteSinglePPE(String complainNumber);

    @Query("DELETE FROM PPEFetchSaveEntity")
    void deleteAll();
}