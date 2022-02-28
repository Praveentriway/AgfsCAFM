package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;

import java.util.List;

@Dao
public interface SiteAreaDao {

    @Query("SELECT * FROM siteAreaEntity")
    List<SiteAreaEntity> getAllSites();

    @Query("SELECT COUNT(*) from siteAreaEntity")
    int countSiteArea();

    @Query("SELECT COUNT(*) FROM siteAreaEntity where siteCode LIKE  :siteCode")
    int countBySiteCode(String siteCode);

    @Query("SELECT * FROM siteAreaEntity where siteCode LIKE  :siteCode")
    SiteAreaEntity findSiteBySiteCode(String siteCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSites(List<SiteAreaEntity> siteAreaEntityList);

    @Delete
    void deleteSite(SiteAreaEntity siteAreaEntity);

    @Query("DELETE FROM siteAreaEntity")
    void deleteAll();
}
