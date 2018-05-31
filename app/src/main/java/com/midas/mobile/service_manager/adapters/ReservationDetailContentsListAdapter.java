package com.midas.mobile.service_manager.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midas.mobile.R;
import com.midas.mobile.entity.ReservationContentsData;

import java.util.ArrayList;

public class ReservationDetailContentsListAdapter extends RecyclerView.Adapter<ReservationDetailContentsListAdapter.ReservationContentsListViewHolder> {
    private ArrayList<ReservationContentsData> dataList;
    private Context con;
    private int layout;

    public ReservationDetailContentsListAdapter(ArrayList<ReservationContentsData> dataList, Context con, int layout) {
        this.dataList = dataList;
        this.con=con;
        this.layout=layout;
    }
    @Override
    public ReservationContentsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ReservationContentsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationContentsListViewHolder holder, int position) {


        ReservationContentsData item = dataList.get(position);

//        Log.e("URL", item.getMenuImage());

        Glide.with(con).load(item.getMenuImage()).into(holder.menuImage);

        holder.menuPrice.setText(String.format("%,d", item.getMenuPrice()) + " 원");

        holder.menuQuantity.setText(item.getMenuQuantitiy() + " ");
        holder.menuSize.setText(item.getMenuSize());


    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ReservationContentsListViewHolder extends RecyclerView.ViewHolder {

        public ImageView menuImage;
        public TextView menuPrice;
        public TextView menuQuantity;
        public TextView menuSize;




        public ReservationContentsListViewHolder(View v) {
            super(v);


            menuImage = v.findViewById(R.id.menuImage);
            menuPrice = v.findViewById(R.id.menuPrice);
            menuQuantity = v.findViewById(R.id.menuQuantity);
            menuSize = v.findViewById(R.id.menuSize);




        }
    }

}
