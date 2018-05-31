package com.midas.mobile.service_customer.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.midas.mobile.R;
import com.midas.mobile.entity.ReservationData;
import com.midas.mobile.networks.model.ReservationResponse;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.utils.BarChartUtil;
import com.midas.mobile.utils.TimeUtil;
import com.midas.mobile.utils.Keys;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CustomerReservationStatusFragment extends Fragment {

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private BarChart barChart;
    private int[] models = new int[13];

    public static CustomerReservationStatusFragment newInstance() {
        return new CustomerReservationStatusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmen_customer_reservation_status, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        barChart = getView().findViewById(R.id.reservation_bar_chart);

        getReservationList(getView());

    }

    private void getReservationList(View view) {

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
                                String[] yydd = date.split(".");

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

                        BarChartUtil barChartUtils = new BarChartUtil(getContext(), barChart);
                        barChartUtils.init();

                        for (int m : models) {
                            Log.e("test value ", m+"");
                        }

                        barChartUtils.setData(models);
                    }
                });
    }
}
