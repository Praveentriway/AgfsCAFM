package com.daemon.emco_android.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RaComment {

@SerializedName("raComments")
@Expose
private String raComments;

public String getRaComments() {
return raComments;
}

public void setRaComments(String raComments) {
this.raComments = raComments;
}

}