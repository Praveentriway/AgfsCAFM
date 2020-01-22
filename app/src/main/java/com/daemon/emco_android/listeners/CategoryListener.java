package com.daemon.emco_android.listeners;

import com.daemon.emco_android.repository.db.entity.CategoryEntity;

import java.util.List;

/** Created by vikram on 17/7/17. */
public interface CategoryListener {
  void onCategoryReceivedSuccess(List<CategoryEntity> categoryList, int mode);

  void onCategoryReceivedFailure(String strErr, int mode);
}
