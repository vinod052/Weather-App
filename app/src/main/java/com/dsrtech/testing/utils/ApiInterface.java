package com.dsrtech.testing.utils;

import com.dsrtech.testing.model.ByCityResponse;
import com.dsrtech.testing.model.ByIdResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/data/2.5/weather")
    Call<ByCityResponse> getCurrentCityWeatherData(@Query("q") String city, @Query("appid") String apiKey);

    @GET("/data/2.5/weather")
    Call<ByCityResponse> getCurrentCityDataByCoordinates(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("APPID") String apiKey
    );

    @GET("/data/2.5/group")
            Call<ResponseBody> getWeatherById(
                    @Query("id") String id,
                    @Query("units") String metric,
                    @Query("appid") String apikey

    );

    @GET("/data/2.5/group")
            Call<ByIdResponse> getResponseById(
            @Query("id") String id,
            @Query("units") String metric,
            @Query("appid") String apikey
    );

    String WEAHTER_BY_CITY_INDIA = "http://api.openweathermap.org/data/2.5/group?id=1273294,1270642,1275899,1258076,1269042,1269515,1275339,1264527,1256237,1271308,1274746,1269843,1254360,1275841,1279017,1278710,1264728,1270583,1270022";

}
