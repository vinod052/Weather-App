package com.dsrtech.testing.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsrtech.testing.R;
import com.dsrtech.testing.adapter.CustomRecyclerViewAdapter;
import com.dsrtech.testing.adapter.CustomViewPagerAdapter;
import com.dsrtech.testing.model.ByCityResponse;
import com.dsrtech.testing.model.ByIdResponse;
import com.dsrtech.testing.presenter.LocationTrack;
import com.dsrtech.testing.presenter.OnItemClick;
import com.dsrtech.testing.utils.ApiClient;
import com.dsrtech.testing.utils.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClick {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationTrack mLocationTrack;
    private ApiInterface mApiInterface;
    private Double mLatitude, mLongitude;
    private CustomRecyclerViewAdapter customRecyclerViewAdapter;
    private CustomViewPagerAdapter mCustomViewPagerAdapter;
    private RecyclerView mRecyclerView;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiInterface = ApiClient.getApiInterface();
        mViewPager = findViewById(R.id.viewpager_mainActivity);

        mRecyclerView = findViewById(R.id.rv_mainActivity);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //check permission provided by user(Location Permission for coordinates)
        checkPermission();

        //Loading for recyclerview
        loadDataByIds();

    }

    private void showHelpDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_help_dialog);
        dialog.setCancelable(true);
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationTrack = new LocationTrack(this);
                if (mLocationTrack.isCanGetLocation()) {
                    mLatitude = mLocationTrack.getLatitude();
                    mLongitude = mLocationTrack.getLongitude();
                    loadDataByCoordinates(mLatitude.toString(),mLongitude.toString());
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                loadData(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                showHelpDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationTrack.stopListener();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                mLocationTrack = new LocationTrack(this);
                if (mLocationTrack.isCanGetLocation()) {
                    mLatitude = mLocationTrack.getLatitude();
                    mLongitude = mLocationTrack.getLongitude();
                    loadDataByCoordinates(mLatitude.toString(), mLongitude.toString());
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                loadData("Delhi");

            }

        }
    }

    /*
    * Load data by city name (this method will work when coordinates not found or with searchview)
    * */
    public void loadData(String cityName) {
        mApiInterface.getCurrentCityWeatherData(cityName, getResources().getString(R.string.api_key))
                .enqueue(new Callback<ByCityResponse>() {

                             @Override
                             public void onResponse(Call<ByCityResponse> call, Response<ByCityResponse> response) {
                                 if (response.body() != null) {
                                     ByCityResponse byCityResponse = response.body();
                                     mCustomViewPagerAdapter = new CustomViewPagerAdapter(MainActivity.this, this, response);
                                     mViewPager.setAdapter(mCustomViewPagerAdapter);
                                 }

                             }

                             @Override
                             public void onFailure(Call<ByCityResponse> call, Throwable t) {

                                 Toast.makeText(MainActivity.this, "No Any Result Found", Toast.LENGTH_SHORT).show();
                             }
                         }

                );
    }


    /*
    * Load data by Coordinates(Latitude, Longitude)
    * */
    private void loadDataByCoordinates(String lat, String lon) {

        mApiInterface.getCurrentCityDataByCoordinates(lat, lon, getResources().getString(R.string.api_key)).enqueue(new Callback<ByCityResponse>() {
            @Override
            public void onResponse(Call<ByCityResponse> call, Response<ByCityResponse> response) {
                if (response.body() != null) {
                    ByCityResponse byCityResponse = response.body();
                    mCustomViewPagerAdapter = new CustomViewPagerAdapter(MainActivity.this, this, response);
                    mViewPager.setAdapter(mCustomViewPagerAdapter);
                }
            }

            @Override
            public void onFailure(Call<ByCityResponse> call, Throwable t) {

            }
        });


    }


    /*
    * Loading data according to city id provided by OPENWEATHERMAP API
    * */
    private void loadDataByIds(){
        mApiInterface.getResponseById("1273294,1270642,1275899,1258076,1269042,1269515,1275339,1264527,1256237,1271308,1274746,1269843,1254360,1275841,1279017,1278710,1264728,1270583,1270022","metric",getResources().getString(R.string.api_key))
        .enqueue(new Callback<ByIdResponse>() {
            @Override
            public void onResponse(Call<ByIdResponse> call, Response<ByIdResponse> response) {
                if (response != null){

                    customRecyclerViewAdapter = new CustomRecyclerViewAdapter(MainActivity.this,response,MainActivity.this);
                    mRecyclerView.setAdapter(customRecyclerViewAdapter);
                }

            }

            @Override
            public void onFailure(Call<ByIdResponse> call, Throwable t) {

                Toast.makeText(mLocationTrack, "Sorry, No result found", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
    * Functionality for press long on recyclerview item using interface
    * */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(int i, ByIdResponse mBbyIdResponse) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCancelable(true);


        ImageView imageView = dialog.findViewById(R.id.iv_icon);
        TextView temprature = dialog.findViewById(R.id.tv_temp);
        TextView cityname = dialog.findViewById(R.id.tv_cityName);
        TextView humidity = dialog.findViewById(R.id.tv_humidity);
        TextView description = dialog.findViewById(R.id.tv_description);
        TextView visibility = dialog.findViewById(R.id.tv_visibility);
        TextView min_temp = dialog.findViewById(R.id.tv_temp_min_dialog);
        TextView max_temp = dialog.findViewById(R.id.tv_temp_max_dialog);
        TextView wind_speed = dialog.findViewById(R.id.tv_windSpeed);
        TextView pressure = dialog.findViewById(R.id.tv_pressure);
        TextView sunrise = dialog.findViewById(R.id.tv_sunrise);
        TextView sunset = dialog.findViewById(R.id.tv_sunset);


        String iconCode = mBbyIdResponse.getResponseList().get(i).getWeatherList().get(0).getIcon();
        String icon_url = "http://openweathermap.org/img/w/" + iconCode + ".png";
        Picasso.get().load(icon_url).into(imageView);

        String temprature_text = String.valueOf(Math.round( mBbyIdResponse.getResponseList().get(i).getMain().getTemp()));
        temprature.setText(temprature_text+"°C");

        String name = mBbyIdResponse.getResponseList().get(i).getCityName();
        cityname.setText(name);

        int minTemp = (int) Math.round(mBbyIdResponse.getResponseList().get(i).getMain().getTempMin());
        int maxTemp = (int) Math.round(mBbyIdResponse.getResponseList().get(i).getMain().getTemp_max());


        String mHumidity = String.valueOf(mBbyIdResponse.getResponseList().get(i).getMain().getHumidity());
        String mDescription = mBbyIdResponse.getResponseList().get(i).getWeatherList().get(0).getDescription();
        String mVisibility = String.valueOf(mBbyIdResponse.getResponseList().get(i).getVisibility()) + " meter";
        String mMin_temp = String.valueOf(minTemp);
        String mMax_temp = String.valueOf(maxTemp);
        String mWindSpeed = mBbyIdResponse.getResponseList().get(i).getWind().getSpeed()+" miles/hr";
        String mPressure = String.valueOf(mBbyIdResponse.getResponseList().get(i).getMain().getPressure())+" hPa";
        String mSunrise = "Sun Rise: "+getDate( mBbyIdResponse.getResponseList().get(i).getSys().getSunrise());
        String mSunset =  "Sun Set: "+getDate( mBbyIdResponse.getResponseList().get(i).getSys().getSunset());


        humidity.setText("Humidity  "+mHumidity+"%");
        description.setText("Weather Type "+mDescription);
        visibility.setText("Visibility "+mVisibility);
        min_temp.setText("Min Temp : "+mMin_temp);
        max_temp.setText("Max Temp : "+mMax_temp);
        wind_speed.setText("Wind Speed : "+mWindSpeed);
        pressure.setText("Atmospheric Pressure : "+mPressure);
        sunrise.setText(mSunrise+"°C");
        sunset.setText(mSunset+"°C");

        dialog.show();

    }

    private String getDate(long time) {
        Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));

        return sdf.format(date);
    }

}
