package com.daemon.emco_android.repository.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;

import java.util.List;

/** Created by Daemonsoft on 10/10/2017. */
@Dao
public interface SaveFeedbackDao {

  @Query("SELECT * FROM SaveFeedbackEntity where mode LIKE :mode")
  List<SaveFeedbackEntity> getAll(int mode);

  @Query("SELECT COUNT(*) from SaveFeedbackEntity")
  int count();

  @Query(
      "SELECT * FROM SaveFeedbackEntity WHERE opco LIKE :mopco AND complaintSite LIKE :mcomplaintSite AND  complaintNumber LIKE :complaintNumber LIMIT 1")
  SaveFeedbackEntity findBycomplaintNumber(
      String mopco, String mcomplaintSite, String complaintNumber);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<SaveFeedbackEntity> saveFeedbackEntities);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(SaveFeedbackEntity saveFeedbackEntity);

  @Delete
  void delete(SaveFeedbackEntity saveFeedbackEntity);

  @Query("DELETE FROM SaveFeedbackEntity")
  void deleteAll();
}
