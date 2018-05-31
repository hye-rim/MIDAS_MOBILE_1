package com.midas.mobile.service_manager.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.midas.mobile.R;
import com.midas.mobile.entity.PurchaseData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.networks.model.PurchaseResponse;
import com.midas.mobile.utils.BarChartUtil;
import com.midas.mobile.utils.Keys;
import com.midas.mobile.utils.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hyerim on 2018. 5. 27....
 */
public class ChartFragment  extends ManagerBaseFragment {
    private final String TAG = ChartFragment.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private View mView;
    private Unbinder mUnbinder;
    private Context mContext;
    private int[] mModels = new int[13];

    /*View Components*/
    @BindView(R.id.chart_bar_chart)
    public BarChart mBarChart;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.manager_fragment_chart, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        mContext = this.getContext();

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getReservationList();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void getReservationList() {
        RetrofitManager.getRetrofitMethod().getPurchaseHistoryAdminList(Keys.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PurchaseResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(PurchaseResponse defaultResponse) {
                        if (defaultResponse.getSuccess()) {

                            String currentYear = new TimeUtil().currentTime();
                            currentYear = currentYear.substring(0,4);
                            ArrayList<PurchaseData> arrayList = defaultResponse.getResult();
                            for (int i = 0; i < arrayList.size(); i++) {
                                String date = arrayList.get(i).date;
                                Log.e(TAG, date);
                                String[] yydd = date.split("\\.");
                                if(currentYear.equals(yydd[0])) {
                                    mModels[Integer.parseInt(yydd[1])] += arrayList.get(i).totalPrice;
                                }

                                Log.e(TAG,date);
//                                if (yydd[0].equals(currentYear)) {
//                                    mModels[Integer.parseInt(yydd[1])] += arrayList.get(i).totalPrice;
//                                }

                                Log.d(TAG, arrayList.get(i).totalPrice+"");
                            }


                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("cart error", e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        showChartView();
                    }
                });
    }

    private void showChartView() {
        BarChartUtil barChartUtils = new BarChartUtil(mContext, mBarChart);
        barChartUtils.init();

        for (int m : mModels) {
            Log.e("test value ", m+"");
        }

        barChartUtils.setData(mModels);
    }
}
