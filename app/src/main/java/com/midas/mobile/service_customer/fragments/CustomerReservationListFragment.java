package com.midas.mobile.service_customer.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midas.mobile.R;
import com.midas.mobile.service_manager.adapters.ReservationListAdapter;
import com.midas.mobile.entity.ReservationData;
import com.midas.mobile.networks.model.ReservationResponse;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.utils.Keys;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CustomerReservationListFragment extends Fragment {

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static CustomerReservationListFragment newInstance() {
        return new CustomerReservationListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmen_customer_reservation_list,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

                            initReservationList(view, defaultResponse.getResult());
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

    void initReservationList(View view, ArrayList<ReservationData> itemList){

        RecyclerView listView = (RecyclerView) view.findViewById(R.id.rcv_customer_reservation);
        listView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        if (itemList.size() > 0) {
            listView.setAdapter(new ReservationListAdapter(itemList, view.getContext(), R.layout.item_reservation));
        }
        listView.setLayoutManager(layoutManager);
    }
}
