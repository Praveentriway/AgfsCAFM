package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.ContractEntity;

import java.util.List;

@Dao
public interface ContractDao {

    @Query("SELECT * FROM contractEntity")
    List<ContractEntity> getAll();

    @Query("SELECT COUNT(*) from contractEntity")
    int count();

    @Query("SELECT COUNT(*) from contractEntity where jobNo LIKE  :jobNo")
    int countByJobNo(String jobNo);

    @Query("SELECT * FROM contractEntity where jobNo LIKE  :jobNo")
    ContractEntity findByJobCode(String jobNo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ContractEntity> contractEntities);

    @Delete
    void delete(ContractEntity contractEntity);

    @Query("DELETE FROM contractEntity")
    void deleteAll();
}
