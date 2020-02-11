package com.dsrtech.testing.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.dsrtech.testing.R;
import com.dsrtech.testing.activity.MainActivity;
import com.dsrtech.testing.model.ByCityResponse;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Callback;
import retrofit2.Response;

public class CustomViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ByCityResponse mByCityResponse;


    public CustomViewPagerAdapter(Context context, Callback<ByCityResponse> byCityResponseCallback, Response<ByCityResponse> response) {
        mContext = context;
        mByCityResponse = response.body();
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout, container, false);

        View view1 = view.findViewById(R.id.weather_layout);
        View view2 = view.findViewById(R.id.air_layout);
        switch (position) {
            case 0:
                view2.setVisibility(View.GONE);
                view1.setVisibility(View.VISIBLE);

              //
                //  LinearLayout linearLayout = view1.findViewById(R.id.ll_root_weather_layout);
                TextView cityName = view1.findViewById(R.id.tv_cityName);
                TextView temp = view1.findViewById(R.id.tv_temp);
                TextView humidity = view1.findViewById(R.id.tv_humidity);
                TextView description = view1.findViewById(R.id.tv_description);
                TextView visibility = view1.findViewById(R.id.tv_visibility);
                TextView temp_min = view1.findViewById(R.id.tv_temp_min);
                TextView temp_max = view1.findViewById(R.id.tv_temp_max);
                ImageView icon = view1.findViewById(R.id.iv_icon);
                

//                linearLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Toast toast = Toast.makeText(mContext, "Left swipe for more details.", Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
//                        toast.show();
//                    }
//                });


                cityName.setText(mByCityResponse.getCityName());

                String iconCode = mByCityResponse.getWeatherList().get(0).getIcon();
                int temprature = (int)Math.round(mByCityResponse.getMain().getTemp() - 273.15);
                int minTemp = (int) Math.round(mByCityResponse.getMain().getTempMin() - 273.15);
                int maxTemp = (int) Math.round(mByCityResponse.getMain().getTemp_max() - 273.15);

                String icon_url = "http://openweathermap.org/img/w/" + iconCode + ".png";
                Picasso.get().load(icon_url).into(icon);
                int humidityVal = mByCityResponse.getMain().getHumidity();


                temp.setText(Integer.toString((temprature))+"°C");
                humidity.setText(Integer.toString(humidityVal)+"% Humidity");
                description.setText(mByCityResponse.getWeatherList().get(0).getDescription());
                visibility.setText("Visibility "+Integer.toString(mByCityResponse.getVisibility()) + " meter");
                temp_min.setText("Min Temp "+Integer.toString(minTemp)+"°C");
                temp_max.setText("Max Temp "+Integer.toString(maxTemp)+"°C");
                break;
            case 1:
                TextView windSpeed = view2.findViewById(R.id.tv_windSpeed);
                TextView pressure = view2.findViewById(R.id.tv_pressure);
                TextView sunrise = view2.findViewById(R.id.tv_sunrise);
                TextView sunset = view2.findViewById(R.id.tv_sunset);
                TextView temp_min1 = view2.findViewById(R.id.tv_temp_min);
                TextView temp_max1 = view2.findViewById(R.id.tv_temp_max);

                int minTemp1 = (int) Math.round(mByCityResponse.getMain().getTempMin() - 273.15);
                int maxTemp1 = (int) Math.round(mByCityResponse.getMain().getTemp_max() - 273.15);


                windSpeed.setText("Wind Speed "+Double.toString(mByCityResponse.getWind().getSpeed())+" miles/hr");
                pressure.setText("Atmospheric pressure "+Integer.toString(mByCityResponse.getMain().getPressure())+" hPa");
                sunrise.setText("Sun Rise: "+getDate(mByCityResponse.getSys().getSunrise()));
                sunset.setText("Sun set: "+(getDate(mByCityResponse.getSys().getSunset())));
                temp_min1.setText("Min Temp "+Integer.toString(minTemp1)+"°C");
                temp_max1.setText("Max Temp "+Integer.toString(maxTemp1)+"°C");

                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private String getDate(long time) {
        Date date = new Date(time*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss "); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));

        return sdf.format(date);
    }
}
