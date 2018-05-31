package com.midas.mobile.service_customer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.midas.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerReservationRecyclerViewAdapter {
}

class CustomerReservationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.reservationDate)
    TextView txtDate;

    @BindView(R.id.userName)
    TextView txtUserName;

    @BindView(R.id.guideMessage)
    TextView txtUserGuideMessage;

    @BindView(R.id.reservationContentsList)
    RecyclerView rcvContentList;

    public CustomerReservationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
