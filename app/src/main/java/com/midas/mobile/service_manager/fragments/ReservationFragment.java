package com.midas.mobile.service_manager.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midas.mobile.R;
import com.midas.mobile.custom.RecyclerItemClickListener;
import com.midas.mobile.entity.ReservationData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.networks.model.ReservationResponse;
import com.midas.mobile.service_manager.activities.ReservationDetailActivity;
import com.midas.mobile.service_manager.adapters.ReservationListAdapter;
import com.midas.mobile.utils.Keys;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("ValidFragment")
public class ReservationFragment extends ManagerBaseFragment {
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final int REQ_CODE_RESERVATION = 4;
    private final int RES_CODE_RESERVATION_EDIT = 5;

    ReservationListAdapter reservationListAdapter;

    private View mView;

    public static ArrayList<ReservationData> reservationDataArrayList;

    Handler handler;

    private TextView txtReservationTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.manager_fragment_reservation, container, false);
        handler = new Handler();


        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtReservationTotal = getView().findViewById(R.id.txt_reservation_total);
        getReservationList();
    }

    private void getReservationList() {

        RetrofitManager.getRetrofitMethod().getReservationList(Keys.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReservationResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ReservationResponse defaultResponse) {
                        if (defaultResponse.getSuccess()) {

                            reservationDataArrayList = defaultResponse.getResult();

                            initReservationList(mView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("cart error", e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        // TODO delete sqlite
                        txtReservationTotal.setText(String.valueOf(reservationDataArrayList.size()));
                    }
                });
    }

    void initReservationList(View view) {

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.reservation_list);
        listView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (reservationDataArrayList.size() > 0) {
            reservationListAdapter = new ReservationListAdapter(reservationDataArrayList, view.getContext(), R.layout.item_reservation);
            listView.setAdapter(reservationListAdapter);
        }
        listView.setLayoutManager(layoutManager);


        listView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), listView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), ReservationDetailActivity.class);
                intent.putExtra("position", position);

                startActivityForResult(intent, REQ_CODE_RESERVATION);
                ;
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_RESERVATION) {
            if (RES_CODE_RESERVATION_EDIT == resultCode) {
                getReservationList();
            }

        }
    }

}
