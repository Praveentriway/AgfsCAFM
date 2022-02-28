package com.daemon.emco_android.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PPMFilterRequest implements Serializable  {

    @SerializedName("employeeId")
    @Expose
    private String employeeId;
    @SerializedName("contractNo")
    @Expose
    private String contractNo;
    @SerializedName("compCode")
    @Expose
    private String compCode;

    @SerializedName("ppmNo")
    @Expose
    private String ppmNo;
    @SerializedName("assetBarcode")
    @Expose
    private String assetBarcode;

    @SerializedName("startDate")
    @Expose
    private ArrayList<String>  startDate;
    @SerializedName("endDate")
    @Expose
    private ArrayList<String> endDate;
    @SerializedName("zoneBuilding")
    @Expose
    private String zoneBuilding;
    @SerializedName("natureDescription")
    @Expose
    private String natureDescription;

    @SerializedName("completedDate")
    @Expose
    private ArrayList<String> completedDate;

    @SerializedName("dueDate")
    @Expose
    private String dueDate;

    @SerializedName("pendingPPM")
    @Expose
    private boolean pendingPPM;



    public PPMFilterRequest(   String employeeId,
             String contractNo,
             String compCode,
             String ppmNo,
             String assetBarcode,
             ArrayList<String>  startDate,
             ArrayList<String> endDate,
             String zoneBuilding,
             String natureDescription,ArrayList<String> completedDate,String dueDate ,boolean pendingPPM){

        this.employeeId=employeeId;
        this.contractNo=contractNo;
        this.compCode=compCode;
        this.ppmNo=ppmNo;
        this.assetBarcode=assetBarcode;
        this.startDate=startDate;
        this.endDate=endDate;
        this.zoneBuilding=zoneBuilding;
        this.natureDescription=natureDescription;
        this.completedDate=completedDate;
        this.dueDate=dueDate;
        this.pendingPPM=pendingPPM;
    }
    public PPMFilterRequest(){

        this.employeeId="";
        this.contractNo="";
        this.compCode="";
        this.ppmNo="";
        this.assetBarcode="";
        this.startDate=new ArrayList<>();
        this.completedDate=new ArrayList<>();
        this.endDate=new ArrayList<>();
        this.zoneBuilding="";
        this.natureDescription="";
        this.dueDate="";
        this.pendingPPM=false;

    }

    public boolean isPendingPPM() {
        return pendingPPM;
    }

    public void setPendingPPM(boolean pendingPPM) {
        this.pendingPPM = pendingPPM;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public ArrayList<String> getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(ArrayList<String> completedDate) {
        this.completedDate = completedDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }


    public String getPpmNo() {
        return ppmNo;
    }

    public void setPpmNo(String ppmNo) {
        this.ppmNo = ppmNo;
    }

    public String getAssetBarcode() {
        return assetBarcode;
    }

    public void setAssetBarcode(String assetBarcode) {
        this.assetBarcode = assetBarcode;
    }

    public ArrayList<String> getStartDate() {
        return startDate;
    }

    public void setStartDate(ArrayList<String> startDate) {
        this.startDate = startDate;
    }

    public ArrayList<String> getEndDate() {
        return endDate;
    }

    public void setEndDate(ArrayList<String> endDate) {
        this.endDate = endDate;
    }

    public String getZoneBuilding() {
        return zoneBuilding;
    }

    public void setZoneBuilding(String zoneBuilding) {
        this.zoneBuilding = zoneBuilding;
    }

    public String getNatureDescription() {
        return natureDescription;
    }

    public void setNatureDescription(String natureDescription) {
        this.natureDescription = natureDescription;
    }
}
