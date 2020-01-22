package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/** Created by vikram on 23/7/17. */
@Entity(tableName = "receiveComplaintsStatusUpdate")
public class ComplaintStatusEntity {
    @SerializedName("newComplaint")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "newComplaint")
    private String newComplaint;
    @SerializedName("oldComplaint")
    @Expose
    @ColumnInfo(name = "oldComplaint")
    private String oldComplaint;

    public String getNewComplaint() {
      return newComplaint;
    }

    public void setNewComplaint(String newComplaint) {
      this.newComplaint = newComplaint;
    }

    public String getOldComplaint() {
      return oldComplaint;
    }

    public void setOldComplaint(String oldComplaint) {
      this.oldComplaint = oldComplaint;
    }

}
