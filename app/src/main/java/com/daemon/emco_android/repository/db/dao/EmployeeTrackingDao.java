package com.daemon.emco_android.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.daemon.emco_android.model.common.EmployeeTrackingDetail;
import java.util.List;


@Dao
public interface EmployeeTrackingDao{

    @Query("SELECT * FROM employeeTrackingDetail")
    List<EmployeeTrackingDetail> getAllEmployeeTracking();

    @Query("SELECT COUNT(*) from employeeTrackingDetail")
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EmployeeTrackingDetail> entities);

    @Query("DELETE FROM employeeTrackingDetail")
    void deleteAll();

}
