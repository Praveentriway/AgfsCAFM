package com.daemon.emco_android.repository.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categoryEntity")
    List<CategoryEntity> getAll();

    @Query("SELECT COUNT(*) from categoryEntity")
    int count();

    @Query("SELECT COUNT(*) from categoryEntity where natureCode LIKE  :natureCode")
    int countByNatureCode(String natureCode);

    @Query("SELECT * FROM categoryEntity where natureCode LIKE  :natureCode")
    CategoryEntity findByCategoryCode(String natureCode);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryEntity> categoryEntities);

    @Delete
    void delete(CategoryEntity categoryEntity);

    @Query("DELETE FROM categoryEntity")
    void deleteAll();
}
