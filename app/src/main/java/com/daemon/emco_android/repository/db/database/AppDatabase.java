package com.daemon.emco_android.repository.db.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import android.content.Context;

import com.daemon.emco_android.repository.db.dao.CategoryDao;
import com.daemon.emco_android.repository.db.dao.ContractDao;
import com.daemon.emco_android.repository.db.dao.EmployeeDetailsDao;
import com.daemon.emco_android.repository.db.dao.ImageUploadDao;
import com.daemon.emco_android.repository.db.dao.LogComplaintDao;
import com.daemon.emco_android.repository.db.dao.PPEFetchSaveDao;
import com.daemon.emco_android.repository.db.dao.PpeNameDao;
import com.daemon.emco_android.repository.db.dao.PriorityDao;
import com.daemon.emco_android.repository.db.dao.RCMaterialDao;
import com.daemon.emco_android.repository.db.dao.RCRespondDao;
import com.daemon.emco_android.repository.db.dao.RCSavedMaterialDao;
import com.daemon.emco_android.repository.db.dao.RCViewAssetDetailsDao;
import com.daemon.emco_android.repository.db.dao.ReportTypesDao;
import com.daemon.emco_android.repository.db.dao.SaveFeedbackDao;
import com.daemon.emco_android.repository.db.dao.SiteAreaDao;
import com.daemon.emco_android.repository.db.dao.WorkDefectsDao;
import com.daemon.emco_android.repository.db.dao.WorkDoneDao;
import com.daemon.emco_android.repository.db.dao.WorkPendingReasonDao;
import com.daemon.emco_android.repository.db.dao.WorkStatusDao;
import com.daemon.emco_android.repository.db.dao.WorkTypeDao;
import com.daemon.emco_android.repository.db.dao.ZoneDao;
import com.daemon.emco_android.repository.db.dao.receiveComplaintItemDao;
import com.daemon.emco_android.repository.db.dao.receiveComplaintViewDao;
import com.daemon.emco_android.repository.db.dao.receiveComplaintViewDaoComplaint;
import com.daemon.emco_android.repository.db.entity.AssetDetailsEntity;
import com.daemon.emco_android.repository.db.entity.CategoryEntity;
import com.daemon.emco_android.repository.db.entity.ComplaintStatusEntity;
import com.daemon.emco_android.repository.db.entity.ContractEntity;
import com.daemon.emco_android.repository.db.entity.DFoundWDoneImageEntity;
import com.daemon.emco_android.repository.db.entity.EmployeeDetailsEntity;
import com.daemon.emco_android.repository.db.entity.LogComplaintEntity;
import com.daemon.emco_android.repository.db.entity.MaterialMasterEntity;
import com.daemon.emco_android.repository.db.entity.PPEFetchSaveEntity;
import com.daemon.emco_android.repository.db.entity.PPENameEntity;
import com.daemon.emco_android.repository.db.entity.PriorityEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintItemEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintRespondEntity;
import com.daemon.emco_android.repository.db.entity.ReceiveComplaintViewEntity;
import com.daemon.emco_android.repository.db.entity.ReportTypesEntity;
import com.daemon.emco_android.repository.db.entity.SaveFeedbackEntity;
import com.daemon.emco_android.repository.db.entity.SaveMaterialEntity;
import com.daemon.emco_android.repository.db.entity.SaveRatedServiceEntity;
import com.daemon.emco_android.repository.db.entity.SiteAreaEntity;
import com.daemon.emco_android.repository.db.entity.WorkDefectEntity;
import com.daemon.emco_android.repository.db.entity.WorkDoneEntity;
import com.daemon.emco_android.repository.db.entity.WorkPendingReasonEntity;
import com.daemon.emco_android.repository.db.entity.WorkStatusEntity;
import com.daemon.emco_android.repository.db.entity.WorkTypeEntity;
import com.daemon.emco_android.repository.db.entity.ZoneEntity;
import com.daemon.emco_android.utils.AppUtils;

@Database(
    entities = {
      LogComplaintEntity.class,
      ContractEntity.class,
      CategoryEntity.class,
      AssetDetailsEntity.class,
      ReceiveComplaintRespondEntity.class,
      ReceiveComplaintItemEntity.class,
      ReceiveComplaintViewEntity.class,
      ComplaintStatusEntity.class,
      WorkDefectEntity.class,
      WorkStatusEntity.class,
      MaterialMasterEntity.class,
      SaveMaterialEntity.class,
      WorkPendingReasonEntity.class,
      ZoneEntity.class,
      WorkDoneEntity.class,
      PPENameEntity.class,
      PriorityEntity.class,
      PPEFetchSaveEntity.class,
      DFoundWDoneImageEntity.class,
      WorkTypeEntity.class,
      SiteAreaEntity.class,
      ReportTypesEntity.class,
      EmployeeDetailsEntity.class,
      SaveFeedbackEntity.class,
      SaveRatedServiceEntity.class
    },
    version = 7)
@TypeConverters(SaveFeedbackEntity.class)
public abstract class AppDatabase extends RoomDatabase {

  static final Migration MIGRATION_4_5 =
      new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
          database.execSQL("DROP TABLE DFoundWDoneImageEntity");
        }
      };
  private static AppDatabase INSTANCE;

  public static AppDatabase getAppDatabase(Context context) {
    if (INSTANCE == null) {
      INSTANCE =
          Room.databaseBuilder(
                  context.getApplicationContext(), AppDatabase.class, AppUtils.EMCO_DATABASE)
              // allow queries on the main thread.
              // Don't do this on a real app! See PersistenceBasicSample for an example.
                  //this to remove all the existing data on migration
              .fallbackToDestructiveMigration()
//              .addMigrations(MIGRATION_4_5)
              .build();
    }
    return INSTANCE;
  }

  public static void destroyInstance() {
    INSTANCE = null;
  }

  public abstract receiveComplaintItemDao receiveComplaintItemDao();

  public abstract receiveComplaintViewDao receiveComplaintViewDao();

  public abstract receiveComplaintViewDaoComplaint receiveComplaintViewDaoComplaint();

  public abstract RCMaterialDao rcMaterialDao();

  public abstract RCSavedMaterialDao rcSavedMaterialDao();

  public abstract RCViewAssetDetailsDao rcViewAssetDetailsDao();

  public abstract SiteAreaDao siteAreaDao();

  public abstract WorkTypeDao workTypeDao();

  public abstract PriorityDao priorityDao();

  public abstract ZoneDao zoneDao();

  public abstract CategoryDao categoryDao();

  public abstract ContractDao contractDao();

  public abstract WorkDefectsDao workDefectsDao();

  public abstract WorkDoneDao workDoneDao();

  public abstract WorkStatusDao workStatusDao();

  public abstract WorkPendingReasonDao workPendingReasonDao();

  public abstract PpeNameDao ppeNameDao();

  public abstract LogComplaintDao logComplaintDao();

  public abstract RCRespondDao rcRespondDao();

  public abstract ReportTypesDao reportTypesDao();

  public abstract ImageUploadDao imageUploadDao();

  public abstract PPEFetchSaveDao ppeFetchSaveDao();

  public abstract EmployeeDetailsDao employeeDetailsDao();

  public abstract SaveFeedbackDao saveFeedbackDao();
}
