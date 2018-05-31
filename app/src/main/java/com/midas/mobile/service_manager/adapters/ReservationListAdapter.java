package com.midas.mobile.service_manager.adapters;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midas.mobile.R;
import com.midas.mobile.entity.ReservationContentsData;
import com.midas.mobile.entity.ReservationData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ReservationListViewHolder>{
    private ArrayList<ReservationData> dataList;
    private Context con;
    private int layout;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    ReservationListAdapter adapter;

    public ReservationListAdapter(ArrayList<ReservationData> dataList, Context con, int layout) {
        this.dataList = dataList;
        this.con=con;
        this.layout=layout;
        this.adapter = this;
    }


    @Override
    public ReservationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ReservationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationListViewHolder holder, int position) {


        ReservationData item = dataList.get(position);
        holder.userName.setText(item.getName());


        SimpleDateFormat transFormat = new SimpleDateFormat("a hh:mm");
        try {
            Date time = transFormat.parse(item.getDate());
            holder.reservationDate.setText(time.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }




        if(item.getContents().size() - 1 == 0){
            holder.guideMessage.setText("님이 " + item.getContents().get(0).getMenuName() + " 을 주문하셨습니다");
        }else{
            holder.guideMessage.setText("님이 " + item.getContents().get(0).getMenuName()
                    + " 외 " + (item.getContents().size() - 1) + " 건을 주문하셨습니다");
        }


        initReservationContentsList(holder.reservationContentsList, item.getContents());

        if(item.getStatus() == 102){
            holder.layout.setVisibility(View.VISIBLE);
            holder.completeStatus.setVisibility(View.VISIBLE);
        }else{
            holder.layout.setVisibility(View.GONE);
            holder.completeStatus.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ReservationListViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout reservationLayout;
        public TextView guideMessage;
        public TextView userName;
        public TextView reservationDate;
        public RecyclerView reservationContentsList;
        public LinearLayout layout;
        public TextView completeStatus;

        public ReservationListViewHolder(View v) {
            super(v);

            reservationLayout = v.findViewById(R.id.reservation_layout);
            guideMessage = v.findViewById(R.id.guideMessage);
            userName = v.findViewById(R.id.userName);
            layout = v.findViewById(R.id.layout);
            completeStatus = v.findViewById(R.id.completeStatus);

            reservationDate = v.findViewById(R.id.reservationDate);

            reservationContentsList = v.findViewById(R.id.reservationContentsList);

        }
    }


    void initReservationContentsList(RecyclerView listView, ArrayList<ReservationContentsData> itemList){

        listView.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(con);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        ArrayList<ReservationContentsData> tmpList = new ArrayList<>();
        for(int i = 0; i < itemList.size(); i++){
            if(i<=2)
                tmpList.add(itemList.get(i));
        }


        if (itemList.size() > 0) {
            listView.setAdapter(new ReservationContentsListAdapter(tmpList, con, R.layout.item_reservation_contents));
        }
        listView.setLayoutManager(layoutManager);


    }


}
