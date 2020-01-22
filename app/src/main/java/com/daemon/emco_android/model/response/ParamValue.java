package com.daemon.emco_android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParamValue {

@SerializedName("insDetlCode")
@Expose
private String insDetlCode;
@SerializedName("parameterOrder")
@Expose
private String parameterOrder;
@SerializedName("parameterValue")
@Expose
private String parameterValue;

public String getInsDetlCode() {
return insDetlCode;
}

public void setInsDetlCode(String insDetlCode) {
this.insDetlCode = insDetlCode;
}

public String getParameterOrder() {
return parameterOrder;
}

public void setParameterOrder(String parameterOrder) {
this.parameterOrder = parameterOrder;
}

public String getParameterValue() {
return parameterValue;
}

public void setParameterValue(String parameterValue) {
this.parameterValue = parameterValue;
}

}