package com.daemon.emco_android.model.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSavedCheckListResponse {

@SerializedName("status")
@Expose
private String status;
@SerializedName("message")
@Expose
private String message;
@SerializedName("object")
@Expose
private List<ObjectSavedCheckListResponse> object = null;

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<ObjectSavedCheckListResponse> getObject() {
return object;
}

public void setObject(List<ObjectSavedCheckListResponse> object) {
this.object = object;
}

}