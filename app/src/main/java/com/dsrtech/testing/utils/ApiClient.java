package com.dsrtech.testing.utils;

public class ApiClient {

    public static final String BASE_URL = "http://api.openweathermap.org";

    public static ApiInterface getApiInterface(){
        return RetrofitInstance.getClient(BASE_URL).create(ApiInterface.class);
    }
}
