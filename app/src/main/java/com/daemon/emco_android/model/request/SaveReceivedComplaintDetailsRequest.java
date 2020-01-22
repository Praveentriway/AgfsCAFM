
package com.daemon.emco_android.model.request;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SaveReceivedComplaintDetailsRequest {

    @SerializedName("complaintNumber")
    private String mComplaintNumber;
    @SerializedName("complaintSite")
    private String mComplaintSite;
    @SerializedName("complaintStatus")
    private String mComplaintStatus;
    @SerializedName("defect")
    private String mDefect;
    @SerializedName("modifiedBy")
    private String mModifiedBy;
    @SerializedName("opco")
    private String mOpco;
    @SerializedName("otherDefects")
    private Object mOtherDefects;
    @SerializedName("otherPendingRemarks")
    private Object mOtherPendingRemarks;
    @SerializedName("otherWorkDone")
    private Object mOtherWorkDone;
    @SerializedName("pendingResponseCode")
    private String mPendingResponseCode;
    @SerializedName("tentativeDate")
    private String mTentativeDate;
    @SerializedName("workDone")
    private Object mWorkDone;
    @SerializedName("workStatus")
    private String mWorkStatus;

    public SaveReceivedComplaintDetailsRequest(String mComplaintNumber, String mComplaintSite, String mComplaintStatus, String mDefect, String mModifiedBy, String mOpco, Object mOtherDefects, Object mOtherPendingRemarks, Object mOtherWorkDone, String mPendingResponseCode, String mTentativeDate, Object mWorkDone, String mWorkStatus) {
        this.mComplaintNumber = mComplaintNumber;
        this.mComplaintSite = mComplaintSite;
        this.mComplaintStatus = mComplaintStatus;
        this.mDefect = mDefect;
        this.mModifiedBy = mModifiedBy;
        this.mOpco = mOpco;
        this.mOtherDefects = mOtherDefects;
        this.mOtherPendingRemarks = mOtherPendingRemarks;
        this.mOtherWorkDone = mOtherWorkDone;
        this.mPendingResponseCode = mPendingResponseCode;
        this.mTentativeDate = mTentativeDate;
        this.mWorkDone = mWorkDone;
        this.mWorkStatus = mWorkStatus;
    }

    public String getComplaintNumber() {
        return mComplaintNumber;
    }

    public void setComplaintNumber(String complaintNumber) {
        mComplaintNumber = complaintNumber;
    }

    public String getComplaintSite() {
        return mComplaintSite;
    }

    public void setComplaintSite(String complaintSite) {
        mComplaintSite = complaintSite;
    }

    public String getComplaintStatus() {
        return mComplaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        mComplaintStatus = complaintStatus;
    }

    public String getDefect() {
        return mDefect;
    }

    public void setDefect(String defect) {
        mDefect = defect;
    }

    public String getModifiedBy() {
        return mModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        mModifiedBy = modifiedBy;
    }

    public String getOpco() {
        return mOpco;
    }

    public void setOpco(String opco) {
        mOpco = opco;
    }

    public Object getOtherDefects() {
        return mOtherDefects;
    }

    public void setOtherDefects(Object otherDefects) {
        mOtherDefects = otherDefects;
    }

    public Object getOtherPendingRemarks() {
        return mOtherPendingRemarks;
    }

    public void setOtherPendingRemarks(Object otherPendingRemarks) {
        mOtherPendingRemarks = otherPendingRemarks;
    }

    public Object getOtherWorkDone() {
        return mOtherWorkDone;
    }

    public void setOtherWorkDone(Object otherWorkDone) {
        mOtherWorkDone = otherWorkDone;
    }

    public String getPendingResponseCode() {
        return mPendingResponseCode;
    }

    public void setPendingResponseCode(String pendingResponseCode) {
        mPendingResponseCode = pendingResponseCode;
    }

    public String getTentativeDate() {
        return mTentativeDate;
    }

    public void setTentativeDate(String tentativeDate) {
        mTentativeDate = tentativeDate;
    }

    public Object getWorkDone() {
        return mWorkDone;
    }

    public void setWorkDone(Object workDone) {
        mWorkDone = workDone;
    }

    public String getWorkStatus() {
        return mWorkStatus;
    }

    public void setWorkStatus(String workStatus) {
        mWorkStatus = workStatus;
    }

}
