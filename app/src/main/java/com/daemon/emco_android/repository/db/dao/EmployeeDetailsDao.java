package com.daemon.emco_android.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;

import java.util.List;

/**
 * Created by Daemonsoft on 10/10/2017.
 */
@Dao
public interface EmployeeDetailsDao {

    @Query("SELECT * FROM EmployeeDetailsEntity")
    List<EmployeeDetailsEntity> getAll();

    @Query("SELECT COUNT(*) from EmployeeDetailsEntity")
    int count();

    @Query("SELECT * FROM EmployeeDetailsEntity where opco LIKE :opco AND contractNo LIKE :contractNo AND workCategory LIKE :workCategory")
    List<EmployeeDetailsEntity> findByEmployeeDetailsCode(String opco, String contractNo, String workCategory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EmployeeDetailsEntity> employeeDetailsEntities);

    @Delete
    void delete(EmployeeDetailsEntity employeeDetailsEntity);

    @Query("DELETE FROM EmployeeDetailsEntity")
    void deleteAll();
}
