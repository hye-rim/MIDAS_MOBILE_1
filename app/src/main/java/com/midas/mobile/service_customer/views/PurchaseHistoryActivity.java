package com.midas.mobile.service_customer.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.midas.mobile.R;
import com.midas.mobile.entity.ReservationData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.networks.model.ReservationResponse;
import com.midas.mobile.utils.BarChartUtil;
import com.midas.mobile.utils.Keys;
import com.midas.mobile.utils.TimeUtil;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    private BarChart barChart;
    private int[] models = new int[13];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmen_customer_reservation_status);

        barChart = findViewById(R.id.reservation_bar_chart);
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

                            String currentYear = new TimeUtil().currentTime();
                            List<ReservationData> list = defaultResponse.getResult();
                            for (int i = 0; i < list.size(); i++) {
                                String date = list.get(i).getDate();
                                String[] yydd = date.split("-");

                                currentYear = currentYear.substring(0, 4);
                                if (yydd[0].equals(currentYear)) {
                                    models[Integer.parseInt(yydd[1])] += list.get(i).getTotalPrice();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("cart error", e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        // TODO delete sqlite

                        BarChartUtil barChartUtils = new BarChartUtil(PurchaseHistoryActivity.this, barChart);
                        barChartUtils.init();

                        for (int m : models) {
                            Log.e("test value ", m+"");
                        }

                        barChartUtils.setData(models);
                    }
                });
    }
}
