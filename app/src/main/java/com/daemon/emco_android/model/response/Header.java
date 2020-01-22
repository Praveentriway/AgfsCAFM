package com.daemon.emco_android.model.response;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Header {

@SerializedName("header")
@Expose
private List<String> header = null;

public List<String> getHeader() {
return header;
}

public void setHeader(List<String> header) {
this.header = header;
}

}

