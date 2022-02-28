package com.daemon.emco_android.repository.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "EmployeeDetailsEntity")
public class EmployeeDetailsEntity {

  @SerializedName("opco")
  @ColumnInfo(name = "opco")
  private String opco;

  @SerializedName("contractNo")
  @ColumnInfo(name = "contractNo")
  private String contractNo;

  @SerializedName("workCategory")
  @ColumnInfo(name = "workCategory")
  private String workCategory;

  @SerializedName("employeeId")
  @ColumnInfo(name = "employeeId")
  @NonNull
  @PrimaryKey
  private String employeeId;

  @SerializedName("employeeName")
  @ColumnInfo(name = "employeeName")
  private String employeeName;

  /** @return The opco */
  public String getOpco() {
    return opco;
  }

  /** @param opco The opco */
  public void setOpco(String opco) {
    this.opco = opco;
  }

  /** @return The contractNo */
  public String getContractNo() {
    return contractNo;
  }

  /** @param contractNo The contractNo */
  public void setContractNo(String contractNo) {
    this.contractNo = contractNo;
  }

  /** @return The workCategory */
  public String getWorkCategory() {
    return workCategory;
  }

  /** @param workCategory The workCategory */
  public void setWorkCategory(String workCategory) {
    this.workCategory = workCategory;
  }

  /** @return The employeeId */
  public String getEmployeeId() {
    return employeeId;
  }

  /** @param employeeId The employeeId */
  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }

  /** @return The employeeName */
  public String getEmployeeName() {
    return employeeName;
  }

  /** @param employeeName The employeeName */
  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }
}
