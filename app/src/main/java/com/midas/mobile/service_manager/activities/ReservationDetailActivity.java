package com.midas.mobile.service_manager.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.midas.mobile.R;
import com.midas.mobile.activities.BaseActivity;
import com.midas.mobile.service_manager.adapters.ReservationDetailContentsListAdapter;
import com.midas.mobile.custom.RecyclerItemClickListener;
import com.midas.mobile.entity.ReservationContentsData;
import com.midas.mobile.entity.ReservationData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.utils.DefaultResponse;
import com.midas.mobile.utils.Keys;
import com.midas.mobile.utils.ToastUtil;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.midas.mobile.service_manager.fragments.ReservationFragment.reservationDataArrayList;
import static com.midas.mobile.utils.Keys.MEDIA_TYPE_JSON;

public class ReservationDetailActivity extends BaseActivity {
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final int RES_CODE_RESERVATION_EDIT = 5;

    int position;
    ReservationData reservationData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        position = getIntent().getIntExtra("position", -1);

        reservationData = reservationDataArrayList.get(position);
        TextView reservationId = findViewById(R.id.reservationId);
        reservationId.setText("No. " + reservationData.getReservationId());


        TextView userName = findViewById(R.id.userName);
        userName.setText(reservationData.getName());

        TextView reservationDate = findViewById(R.id.reservationDate);

        SimpleDateFormat transFormat = new SimpleDateFormat("a hh:mm");
        try {
            Date time = transFormat.parse(reservationData.getDate());
            reservationDate.setText(time.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }



        TextView totalPrice = findViewById(R.id.totalPrice);

        totalPrice.setText(String.format("%,d", reservationData.getTotalPrice()) + " 원");

        initReservationContentsList(reservationData.getContents());

        findViewById(R.id.complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReservationStatus(reservationData.getReservationId());
            }
        });

    }


    void initReservationContentsList(ArrayList<ReservationContentsData> reservationContentsDataArrayList){



        RecyclerView listView = (RecyclerView) findViewById(R.id.reservationContentsList);
        listView.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(ReservationDetailActivity.this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (reservationContentsDataArrayList.size() > 0) {
            ReservationDetailContentsListAdapter reservationListAdapter = new ReservationDetailContentsListAdapter(reservationContentsDataArrayList, ReservationDetailActivity.this, R.layout.item_reservation_detail_contents);
            listView.setAdapter(reservationListAdapter);
        }
        listView.setLayoutManager(layoutManager);
        listView.setHasFixedSize(true);


        listView.addOnItemTouchListener(new RecyclerItemClickListener(ReservationDetailActivity.this, listView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(view.getContext(), ReservationDetailActivity.class);
//                intent.putExtra("position", position);

//                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));



    }



    void setReservationStatus(int reservationId){
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", 102);
        } catch (final Exception exception) {

            return;
        }

        RetrofitManager.getRetrofitMethod().setReservationStatus(Keys.getInstance().getToken(),reservationId, RequestBody.create(MEDIA_TYPE_JSON, jsonObject.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(DefaultResponse defaultResponse) {
                        ToastUtil.makeShortToast(getApplicationContext(), defaultResponse.getMessage());
                        if (defaultResponse.getSuccess()) {
                            setResult(RES_CODE_RESERVATION_EDIT);
                            finish();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("cart error", e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        // TODO delete sqlite
                    }
                });
    }
}
