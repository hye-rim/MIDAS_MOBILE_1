package com.midas.mobile.service_customer.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.midas.mobile.R;
import com.midas.mobile.entity.ReservationData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.networks.model.ReservationResponse;
import com.midas.mobile.service_manager.adapters.ReservationListAdapter;
import com.midas.mobile.utils.Keys;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReservationStatusActivity extends AppCompatActivity {

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmen_customer_reservation_list);
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

                            initReservationList(defaultResponse.getResult());
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

    void initReservationList(ArrayList<ReservationData> itemList) {

        RecyclerView listView =  findViewById(R.id.rcv_customer_reservation);
        listView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ReservationStatusActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (itemList.size() > 0) {
            listView.setAdapter(new ReservationListAdapter(itemList, ReservationStatusActivity.this, R.layout.item_reservation));
        }
        listView.setLayoutManager(layoutManager);
    }
}
