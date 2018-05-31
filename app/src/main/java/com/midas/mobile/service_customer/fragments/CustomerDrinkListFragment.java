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
import com.midas.mobile.entity.MenuData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.service_customer.adapters.CustomerDrinkRecyclerViewAdapter;
import com.midas.mobile.networks.model.MenuListResponse;
import com.midas.mobile.utils.Keys;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CustomerDrinkListFragment extends Fragment {
    private View mView;
    private Unbinder mUnbinder;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private List<MenuData> list;

    private CustomerDrinkRecyclerViewAdapter adapter;

    public static CustomerDrinkListFragment newInstance() {
        return new CustomerDrinkListFragment();
    }


    private void getMenu() {
        RetrofitManager.getRetrofitMethod().getMenu(Keys.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MenuListResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(MenuListResponse menuListResponse) {
                        if (menuListResponse.getSuccess()) {
                            list = menuListResponse.getResult();
                            Log.e("test", list.size() + "");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        List<MenuData> items = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            MenuData m = list.get(i);
                            items.add(new MenuData(i % 2, i, m.getmName(), m.getmCost(), m.getmExplain(), m.getmImageUrl()));
                        }
                        adapter.addDatas(items);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_customer_drink_list, container, false);
        mUnbinder = ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView rcv = getView().findViewById(R.id.rcv_customer_drink_list);
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv.setHasFixedSize(true);
        adapter = new CustomerDrinkRecyclerViewAdapter(getContext());

        rcv.setAdapter(adapter);

        getMenu();

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
}
