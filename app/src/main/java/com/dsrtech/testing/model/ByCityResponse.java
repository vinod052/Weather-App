package com.dsrtech.testing.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ByCityResponse {

    @SerializedName("coord")
    @Expose
    private Coord mCoord;

    @SerializedName("weather")
    @Expose
    private List<Weather> mWeatherList = null;

    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("main")
    @Expose
    private Main mMain;

    @SerializedName("visibility")
    @Expose
    private int visibility;

    @SerializedName("wind")
    @Expose
    private Wind mWind;

    @SerializedName("clouds")
    @Expose
    private Clouds mClouds;

    @SerializedName("dt")
    @Expose
    private long dt;

    @SerializedName("sys")
    @Expose
    private Sys mSys;

    @SerializedName("timezone")
    @Expose
    private long timezone;

    @SerializedName("id")
    @Expose
    private long id;


    @SerializedName("name")
    @Expose
    private String cityName;

    @SerializedName("cod")
    @Expose
    private int cod;

    public Coord getCoord() {
        return mCoord;
    }

    public void setCoord(Coord coord) {
        mCoord = coord;
    }

    public List<Weather> getWeatherList() {
        return mWeatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        mWeatherList = weatherList;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return mMain;
    }

    public void setMain(Main main) {
        mMain = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return mWind;
    }

    public void setWind(Wind wind) {
        mWind = wind;
    }

    public Clouds getClouds() {
        return mClouds;
    }

    public void setClouds(Clouds clouds) {
        mClouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return mSys;
    }

    public void setSys(Sys sys) {
        mSys = sys;
    }

    public long getTimezone() {
        return timezone;
    }

    public void setTimezone(long timezone) {
        this.timezone = timezone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
