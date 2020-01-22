package com.daemon.emco_android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GENERAL {

@SerializedName("checkListCode")
@Expose
private String checkListCode;
@SerializedName("checkListDesc")
@Expose
private String checkListDesc;
@SerializedName("natureWork")
@Expose
private String natureWork;
@SerializedName("nature")
@Expose
private String nature;
@SerializedName("equipType")
@Expose
private String equipType;
@SerializedName("equipment")
@Expose
private String equipment;
@SerializedName("serviceType")
@Expose
private String serviceType;
@SerializedName("headerCode")
@Expose
private String headerCode;
@SerializedName("ppmServiceINS")
@Expose
private String ppmServiceINS;
@SerializedName("detailsCode")
@Expose
private String detailsCode;
@SerializedName("ppmServiceDetails")
@Expose
private String ppmServiceDetails;
@SerializedName("paramValue")
@Expose
private String paramValue;
@SerializedName("remarks")
@Expose
private String remarks;

public String getCheckListCode() {
return checkListCode;
}

public void setCheckListCode(String checkListCode) {
this.checkListCode = checkListCode;
}

public String getCheckListDesc() {
return checkListDesc;
}

public void setCheckListDesc(String checkListDesc) {
this.checkListDesc = checkListDesc;
}

public String getNatureWork() {
return natureWork;
}

public void setNatureWork(String natureWork) {
this.natureWork = natureWork;
}

public String getNature() {
return nature;
}

public void setNature(String nature) {
this.nature = nature;
}

public String getEquipType() {
return equipType;
}

public void setEquipType(String equipType) {
this.equipType = equipType;
}

public String getEquipment() {
return equipment;
}

public void setEquipment(String equipment) {
this.equipment = equipment;
}

public String getServiceType() {
return serviceType;
}

public void setServiceType(String serviceType) {
this.serviceType = serviceType;
}

public String getHeaderCode() {
return headerCode;
}

public void setHeaderCode(String headerCode) {
this.headerCode = headerCode;
}

public String getPpmServiceINS() {
return ppmServiceINS;
}

public void setPpmServiceINS(String ppmServiceINS) {
this.ppmServiceINS = ppmServiceINS;
}

public String getDetailsCode() {
return detailsCode;
}

public void setDetailsCode(String detailsCode) {
this.detailsCode = detailsCode;
}

public String getPpmServiceDetails() {
return ppmServiceDetails;
}

public void setPpmServiceDetails(String ppmServiceDetails) {
this.ppmServiceDetails = ppmServiceDetails;
}

public String getParamValue() {
return paramValue;
}

public void setParamValue(String paramValue) {
this.paramValue = paramValue;
}

public String getRemarks() {
return remarks;
}

public void setRemarks(String remarks) {
this.remarks = remarks;
}

}