package com.daemon.emco_android.model.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.daemon.emco_android.model.response.ParamValue;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class PpmScheduleDocBy implements Parcelable {

  public static final Parcelable.Creator<PpmScheduleDocBy> CREATOR =
      new Creator<PpmScheduleDocBy>() {

        @SuppressWarnings({"unchecked"})
        public PpmScheduleDocBy createFromParcel(Parcel in) {
          PpmScheduleDocBy instance = new PpmScheduleDocBy();
          instance.ppmNo = ((String) in.readValue((String.class.getClassLoader())));
          instance.opco = ((String) in.readValue((String.class.getClassLoader())));
          instance.batchCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.batchSRL = ((String) in.readValue((String.class.getClassLoader())));
          instance.ppmDate = ((String) in.readValue((String.class.getClassLoader())));
          instance.actStart = ((String) in.readValue((String.class.getClassLoader())));

          instance.regCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.jobNo = ((String) in.readValue((String.class.getClassLoader())));
          instance.siteName = ((String) in.readValue((String.class.getClassLoader())));
          instance.groupCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.buildTag = ((String) in.readValue((String.class.getClassLoader())));
          instance.equipCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.chkCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.natureWork = ((String) in.readValue((String.class.getClassLoader())));
          instance.natureDescription = ((String) in.readValue((String.class.getClassLoader())));
          instance.teamCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.checkedBy = ((String) in.readValue((String.class.getClassLoader())));
          instance.attendedBy = ((String) in.readValue((String.class.getClassLoader())));
          instance.supId = ((String) in.readValue((String.class.getClassLoader())));
          instance.engId = ((String) in.readValue((String.class.getClassLoader())));
          instance.startDate = ((String) in.readValue((String.class.getClassLoader())));
          instance.endDate = ((String) in.readValue((String.class.getClassLoader())));
          instance.closeStatus = ((String) in.readValue((String.class.getClassLoader())));
          instance.printed = ((String) in.readValue((String.class.getClassLoader())));
          instance.subbatch = ((String) in.readValue((String.class.getClassLoader())));
          instance.type = ((String) in.readValue((String.class.getClassLoader())));
          instance.user = ((String) in.readValue((String.class.getClassLoader())));
          instance.crDate = ((String) in.readValue((String.class.getClassLoader())));
          instance.reactiveTransfer = ((String) in.readValue((String.class.getClassLoader())));
          instance.programSource = ((String) in.readValue((String.class.getClassLoader())));
          instance.companyCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.zoneCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.equipType = ((String) in.readValue((String.class.getClassLoader())));
          instance.ppmHours = ((String) in.readValue((String.class.getClassLoader())));
          instance.ppmStatus = ((String) in.readValue((String.class.getClassLoader())));
          instance.location = ((String) in.readValue((String.class.getClassLoader())));
          instance.zoneDescription = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetTypeDesc = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetMake = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetBarCode = ((String) in.readValue((String.class.getClassLoader())));
          instance.assetModel = ((String) in.readValue((String.class.getClassLoader())));
          instance.clientBarcode = ((String) in.readValue((String.class.getClassLoader())));
          return instance;
        }

        public PpmScheduleDocBy[] newArray(int size) {
          return (new PpmScheduleDocBy[size]);
        }
      };

  @SerializedName("ppmNo")
  @Expose
  private String ppmNo;

  @SerializedName("batchCode")
  @Expose
  private String batchCode;

  @SerializedName("zoneDescription")
  @Expose
  private String zoneDescription;

  @SerializedName("assetBarCode")
  @Expose
  private String assetBarCode;

  @SerializedName("assetModel")
  @Expose
  private String assetModel;

  @SerializedName("assetMake")
  @Expose
  private String assetMake;

  @SerializedName("assetTypeDesc")
  @Expose
  private String assetTypeDesc;

  @SerializedName("batchSRL")
  @Expose
  private String batchSRL;

  @SerializedName("ppmDate")
  @Expose
  private String ppmDate;

  @SerializedName("opco")
  @Expose
  private String opco;

  @SerializedName("regCode")
  @Expose
  private String regCode;

  @SerializedName("jobNo")
  @Expose
  private String jobNo;

  @SerializedName("siteName")
  @Expose
  private String siteName;

  @SerializedName("groupCode")
  @Expose
  private String groupCode;

  @SerializedName("buildTag")
  @Expose
  private String buildTag;

  @SerializedName("equipCode")
  @Expose
  private String equipCode;

  @SerializedName("chkCode")
  @Expose
  private String chkCode;

  @SerializedName("natureWork")
  @Expose
  private String natureWork;

  @SerializedName("natureDescription")
  @Expose
  private String natureDescription;

  @SerializedName("teamCode")
  @Expose
  private String teamCode;

  @SerializedName("checkedBy")
  @Expose
  private String checkedBy;

  @SerializedName("attendedBy")
  @Expose
  private String attendedBy;

  @SerializedName("supId")
  @Expose
  private String supId;

  @SerializedName("engId")
  @Expose
  private String engId;

  @SerializedName("startDate")
  @Expose
  private String startDate;

  @SerializedName("endDate")
  @Expose
  private String endDate;

  @SerializedName("closeStatus")
  @Expose
  private String closeStatus;

  @SerializedName("workOrderNo")
  @Expose
  private String workOrderNo;

  @SerializedName("printed")
  @Expose
  private String printed;

  @SerializedName("subbatch")
  @Expose
  private String subbatch;

  @SerializedName("type")
  @Expose
  private String type;

  @SerializedName("assetCode")
  @Expose
  private String assetCode;

  @SerializedName("user")
  @Expose
  private String user;

  @SerializedName("crDate")
  @Expose
  private String crDate;

  @SerializedName("reactiveTransfer")
  @Expose
  private String reactiveTransfer;

  @SerializedName("programSource")
  @Expose
  private String programSource;

  @SerializedName("companyCode")
  @Expose
  private String companyCode;

  @SerializedName("zoneCode")
  @Expose
  private String zoneCode;

  @SerializedName("clinsCode")
  @Expose
  private String clinsCode;

  @SerializedName("cldetlCode")
  @Expose
  private String cldetlCode;

  @SerializedName("clientBarcode")
  @Expose
  private String clientBarcode;

  @SerializedName("actStart")
  @Expose
  private String actStart;


  public String getClientBarcode() {
    return clientBarcode;
  }

  public void setClientBarcode(String clientBarcode) {
    this.clientBarcode = clientBarcode;
  }

  private List<PpmScheduleDocBy>ppmRefNoList;

  public List<PpmScheduleDocBy> getPpmnoList() {
    return ppmRefNoList;
  }

  public void setPpmnoList(List<PpmScheduleDocBy> ppmnoList) {
    this.ppmRefNoList = ppmnoList;
  }

  public String getWorkOrderNo() {
    return workOrderNo;
  }

  public void setWorkOrderNo(String workOrderNo) {
    this.workOrderNo = workOrderNo;
  }

  public String getAssetCode() {
    return assetCode;
  }

  public void setAssetCode(String assetCode) {
    this.assetCode = assetCode;
  }

  public String getClinsCode() {
    return clinsCode;
  }

  public void setClinsCode(String clinsCode) {
    this.clinsCode = clinsCode;
  }

  public String getCldetlCode() {
    return cldetlCode;
  }

  public String getActStart() {
    return actStart;
  }

  public void setActStart(String actStart) {
    this.actStart = actStart;
  }

  public void setCldetlCode(String cldetlCode) {
    this.cldetlCode = cldetlCode;
  }

  public String getParamValue() {
    return paramValue;
  }

  public void setParamValue(String paramValue) {
    this.paramValue = paramValue;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  @SerializedName("paramValue")
  @Expose

  private String paramValue;

  @SerializedName("paramValueList")
  @Expose

  private List<ParamValue> paramValueList;

  @SerializedName("modifiedBy")
  @Expose
  private String modifiedBy;

  @SerializedName("remarks")
  @Expose
  private String remarks;

  @SerializedName("equipType")
  @Expose
  private String equipType;

  @SerializedName("ppmHours")
  @Expose
  private String ppmHours;

  @SerializedName("ppmStatus")
  @Expose
  private String ppmStatus;

  @SerializedName("location")
  @Expose
  private String location;

  public List<ParamValue> getParamValueList() {
    return paramValueList;
  }

  public void setParamValueList(List<ParamValue> paramValueList) {
    this.paramValueList = paramValueList;
  }

  public String getOpco() {
    return opco;
  }

  public void setOpco(String opco) {
    this.opco = opco;
  }

  /** @return The ppmNo */
  public String getPpmNo() {
    return ppmNo;
  }

  /** @param ppmNo The ppmNo */
  public void setPpmNo(String ppmNo) {
    this.ppmNo = ppmNo;
  }

  /** @return The batchCode */
  public String getBatchCode() {
    return batchCode;
  }

  /** @param batchCode The batchCode */
  public void setBatchCode(String batchCode) {
    this.batchCode = batchCode;
  }

  /** @return The batchSRL */
  public String getBatchSRL() {
    return batchSRL;
  }

  /** @param batchSRL The batchSRL */
  public void setBatchSRL(String batchSRL) {
    this.batchSRL = batchSRL;
  }

  /** @return The ppmDate */
  public String getPpmDate() {
    return ppmDate;
  }

  /** @param ppmDate The ppmDate */
  public void setPpmDate(String ppmDate) {
    this.ppmDate = ppmDate;
  }

  public String getAssetBarCode() {
    return assetBarCode;
  }

  public void setAssetBarCode(String assetBarCode) {
    this.assetBarCode = assetBarCode;
  }

  /** @return The regCode */
  public String getRegCode() {
    return regCode;
  }

  /** @param regCode The regCode */
  public void setRegCode(String regCode) {
    this.regCode = regCode;
  }

  /** @return The jobNo */
  public String getJobNo() {
    return jobNo;
  }

  /** @param jobNo The jobNo */
  public void setJobNo(String jobNo) {
    this.jobNo = jobNo;
  }

  /** @return The siteName */
  public String getSiteName() {
    return siteName;
  }

  /** @param siteName The siteName */
  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  /** @return The groupCode */
  public String getGroupCode() {
    return groupCode;
  }

  /** @param groupCode The groupCode */
  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  /** @return The buildTag */
  public String getBuildTag() {
    return buildTag;
  }

  /** @param buildTag The buildTag */
  public void setBuildTag(String buildTag) {
    this.buildTag = buildTag;
  }

  /** @return The equipCode */
  public String getEquipCode() {
    return equipCode;
  }

  /** @param equipCode The equipCode */
  public void setEquipCode(String equipCode) {
    this.equipCode = equipCode;
  }

  /** @return The chkCode */
  public String getChkCode() {
    return chkCode;
  }

  /** @param chkCode The chkCode */
  public void setChkCode(String chkCode) {
    this.chkCode = chkCode;
  }

  /** @return The natureWork */
  public String getNatureWork() {
    return natureWork;
  }

  /** @param natureWork The natureWork */
  public void setNatureWork(String natureWork) {
    this.natureWork = natureWork;
  }

  /** @return The natureDescription */
  public String getNatureDescription() {
    return natureDescription;
  }

  /** @param natureDescription The natureDescription */
  public void setNatureDescription(String natureDescription) {
    this.natureDescription = natureDescription;
  }

  /** @return The teamCode */
  public String getTeamCode() {
    return teamCode;
  }

  /** @param teamCode The teamCode */
  public void setTeamCode(String teamCode) {
    this.teamCode = teamCode;
  }

  /** @return The checkedBy */
  public String getCheckedBy() {
    return checkedBy;
  }

  /** @param checkedBy The checkedBy */
  public void setCheckedBy(String checkedBy) {
    this.checkedBy = checkedBy;
  }

  /** @return The attendedBy */
  public String getAttendedBy() {
    return attendedBy;
  }

  /** @param attendedBy The attendedBy */
  public void setAttendedBy(String attendedBy) {
    this.attendedBy = attendedBy;
  }

  /** @return The supId */
  public String getSupId() {
    return supId;
  }

  /** @param supId The supId */
  public void setSupId(String supId) {
    this.supId = supId;
  }

  /** @return The engId */
  public String getEngId() {
    return engId;
  }

  /** @param engId The engId */
  public void setEngId(String engId) {
    this.engId = engId;
  }

  /** @return The startDate */
  public String getStartDate() {
    return startDate;
  }

  /** @param startDate The startDate */
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  /** @return The endDate */
  public String getEndDate() {
    return endDate;
  }

  /** @param endDate The endDate */
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  /** @return The closeStatus */
  public String getCloseStatus() {
    return closeStatus;
  }

  /** @param closeStatus The closeStatus */
  public void setCloseStatus(String closeStatus) {
    this.closeStatus = closeStatus;
  }

  /** @return The printed */
  public String getPrinted() {
    return printed;
  }

  /** @param printed The printed */
  public void setPrinted(String printed) {
    this.printed = printed;
  }

  /** @return The subbatch */
  public String getSubbatch() {
    return subbatch;
  }

  /** @param subbatch The subbatch */
  public void setSubbatch(String subbatch) {
    this.subbatch = subbatch;
  }

  /** @return The type */
  public String getType() {
    return type;
  }

  /** @param type The type */
  public void setType(String type) {
    this.type = type;
  }

  /** @return The user */
  public String getUser() {
    return user;
  }

  /** @param user The user */
  public void setUser(String user) {
    this.user = user;
  }

  /** @return The crDate */
  public String getCrDate() {
    return crDate;
  }

  /** @param crDate The crDate */
  public void setCrDate(String crDate) {
    this.crDate = crDate;
  }

  /** @return The reactiveTransfer */
  public String getReactiveTransfer() {
    return reactiveTransfer;
  }

  /** @param reactiveTransfer The reactiveTransfer */
  public void setReactiveTransfer(String reactiveTransfer) {
    this.reactiveTransfer = reactiveTransfer;
  }

  /** @return The programSource */
  public String getProgramSource() {
    return programSource;
  }

  /** @param programSource The programSource */
  public void setProgramSource(String programSource) {
    this.programSource = programSource;
  }

  /** @return The companyCode */
  public String getCompanyCode() {
    return companyCode;
  }

  /** @param companyCode The companyCode */
  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }

  /** @return The zoneCode */
  public String getZoneCode() {
    return zoneCode;
  }

  /** @param zoneCode The zoneCode */
  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  /** @return The equipType */
  public String getEquipType() {
    return equipType;
  }

  /** @param equipType The equipType */
  public void setEquipType(String equipType) {
    this.equipType = equipType;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  /** @return The ppmHours */
  public String getPpmHours() {
    return ppmHours;
  }

  /** @param ppmHours The ppmHours */
  public void setPpmHours(String ppmHours) {
    this.ppmHours = ppmHours;
  }

  /** @return The ppmStatus */
  public String getPpmStatus() {
    return ppmStatus;
  }

  /** @param ppmStatus The ppmStatus */
  public void setPpmStatus(String ppmStatus) {
    this.ppmStatus = ppmStatus;
  }

  public String getZoneDescription() {
    return zoneDescription;
  }

  public void setZoneDescription(String zoneDescription) {
    this.zoneDescription = zoneDescription;
  }

  public String getAssetTypeDesc() {
    return assetTypeDesc;
  }

  public void setAssetTypeDesc(String assetTypeDesc) {
    this.assetTypeDesc = assetTypeDesc;
  }

  public String getAssetModel() {
    return assetModel;
  }

  public void setAssetModel(String assetModel) {
    this.assetModel = assetModel;
  }

  public String getAssetMake() {
    return assetMake;
  }

  public void setAssetMake(String assetMake) {
    this.assetMake = assetMake;
  }

  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(ppmNo);
    dest.writeValue(opco);
    dest.writeValue(batchCode);
    dest.writeValue(batchSRL);
    dest.writeValue(ppmDate);
    dest.writeValue(actStart);
    dest.writeValue(regCode);
    dest.writeValue(jobNo);
    dest.writeValue(siteName);
    dest.writeValue(groupCode);
    dest.writeValue(buildTag);
    dest.writeValue(equipCode);
    dest.writeValue(chkCode);
    dest.writeValue(natureWork);
    dest.writeValue(natureDescription);
    dest.writeValue(teamCode);
    dest.writeValue(checkedBy);
    dest.writeValue(attendedBy);
    dest.writeValue(supId);
    dest.writeValue(engId);
    dest.writeValue(startDate);
    dest.writeValue(endDate);
    dest.writeValue(closeStatus);
    dest.writeValue(printed);
    dest.writeValue(subbatch);
    dest.writeValue(type);
    dest.writeValue(user);
    dest.writeValue(crDate);
    dest.writeValue(reactiveTransfer);
    dest.writeValue(programSource);
    dest.writeValue(companyCode);
    dest.writeValue(zoneCode);
    dest.writeValue(equipType);
    dest.writeValue(ppmHours);
    dest.writeValue(ppmStatus);
    dest.writeValue(location);
    dest.writeValue(zoneDescription);
    dest.writeValue(assetMake);
    dest.writeValue(assetModel);
    dest.writeValue(assetTypeDesc);
    dest.writeValue(assetBarCode);
    dest.writeValue(clientBarcode);
  }

  public int describeContents() {
    return 0;
  }
}
