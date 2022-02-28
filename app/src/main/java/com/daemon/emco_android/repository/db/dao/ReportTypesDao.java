package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.ReportTypesEntity;

import java.util.List;

@Dao
public interface ReportTypesDao {

    @Query("SELECT * FROM ReportTypesEntity")
    List<ReportTypesEntity> getAllReportTypes();

    @Query("SELECT COUNT(*) from ReportTypesEntity")
    int count();

    @Query("SELECT COUNT(*) FROM ReportTypesEntity where reportSRL LIKE  :reportSRL")
    int countByreportSRL(String reportSRL);

    @Query("SELECT * FROM ReportTypesEntity where reportSRL LIKE  :reportSRL")
    ReportTypesEntity findReportTypesByreportSRL(String reportSRL);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllReportTypes(List<ReportTypesEntity> reportTypesEntities);

    @Delete
    void deleteReportTypes(ReportTypesEntity reportTypesEntity);

    @Query("DELETE FROM ReportTypesEntity")
    void deleteAll();
}
