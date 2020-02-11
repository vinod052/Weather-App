package com.dsrtech.testing.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ByIdResponse {

    @SerializedName("list")
    @Expose
    private List<ByCityResponse> responseList;


    public List<ByCityResponse> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<ByCityResponse> responseList) {
        this.responseList = responseList;
    }
}
