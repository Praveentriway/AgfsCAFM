package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.ZoneEntity;

import java.util.List;

@Dao
public interface ZoneDao {

    @Query("SELECT * FROM zoneEntity")
    List<ZoneEntity> getAllZones();

    @Query("SELECT COUNT(*) from zoneEntity")
    int count();

    @Query("SELECT COUNT(*) FROM zoneEntity where zoneCode LIKE  :zoneCode")
    int countByZoneCode(String zoneCode);

    @Query("SELECT * FROM zoneEntity where opco LIKE  :opco AND contractNo LIKE :contractNo")
    List<ZoneEntity> findByZoneCode(String opco,String contractNo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ZoneEntity> zoneEntities);

    @Delete
    void delete(ZoneEntity zoneEntity);

    @Query("DELETE FROM zoneEntity")
    void deleteAll();
}
