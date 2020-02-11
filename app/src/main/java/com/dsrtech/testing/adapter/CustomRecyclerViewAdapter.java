package com.dsrtech.testing.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsrtech.testing.R;
import com.dsrtech.testing.model.ByIdResponse;
import com.dsrtech.testing.presenter.OnItemClick;
import com.squareup.picasso.Picasso;

import retrofit2.Response;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private ByIdResponse mBbyIdResponse;
    private OnItemClick onItemClick;

    public CustomRecyclerViewAdapter(Context context, Response<ByIdResponse> response,OnItemClick onItemClick) {
        mContext = context;
        mBbyIdResponse = response.body();
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_city_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        //apply animation to icon
        holder.ivIcon.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition));


        //apply animation to content
        holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale));

        String iconCode = mBbyIdResponse.getResponseList().get(position).getWeatherList().get(0).getIcon();
        String icon_url = "http://openweathermap.org/img/w/" + iconCode + ".png";
        Picasso.get().load(icon_url).into(holder.ivIcon);
        String temprature = String.valueOf(Math.round( mBbyIdResponse.getResponseList().get(position).getMain().getTemp()));
        holder.tvTemp.setText(temprature+"°C");

        String name = mBbyIdResponse.getResponseList().get(position).getCityName();
        holder.tvCityName.setText(name);


        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemClick.onItemClick(holder.getAdapterPosition(),mBbyIdResponse);
                return true;


            }
        });

    }

    @Override
    public int getItemCount() {
        return mBbyIdResponse.getResponseList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvCityName;
        TextView tvTemp;
        LinearLayout linearLayout;
        LinearLayout linearLayout_root;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.iv_item_icon);
            tvTemp = itemView.findViewById(R.id.tv_item_temp);
            tvCityName = itemView.findViewById(R.id.tv_item_city_name);
            linearLayout = itemView.findViewById(R.id.ll_content);
            linearLayout_root = itemView.findViewById(R.id.ll_root_item_layout);




        }
    }
}
