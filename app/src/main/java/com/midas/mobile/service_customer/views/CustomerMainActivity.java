package com.midas.mobile.service_customer.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.midas.mobile.R;
import com.midas.mobile.activities.LicenseActivity;
import com.midas.mobile.entity.MenuData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.networks.model.FCMResponse;
import com.midas.mobile.networks.model.MenuListResponse;
import com.midas.mobile.service_customer.adapters.CustomerDrinkGridViewAdapter;
import com.midas.mobile.utils.DefaultResponse;
import com.midas.mobile.utils.Keys;
import com.midas.mobile.utils.SQLiteUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.midas.mobile.service_customer.event.IntentKey.EXPLAIN;
import static com.midas.mobile.service_customer.event.IntentKey.IMAGE;
import static com.midas.mobile.service_customer.event.IntentKey.NAME;
import static com.midas.mobile.service_customer.event.IntentKey.PRICE;

public class CustomerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.fbtAR)
    public FloatingActionButton fbt;


    private List<MenuData> list;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private CustomerDrinkGridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity_main);
        ButterKnife.bind(this);

        GridView rcv = findViewById(R.id.rcv_customer_drink_list);

        adapter = new CustomerDrinkGridViewAdapter(CustomerMainActivity.this);

        rcv.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent it = new Intent(getApplicationContext(), DetailMenuInfoActivity.class);
            it.putExtra(NAME, list.get(i).getmName());
            it.putExtra(PRICE, list.get(i).getmCost());
            it.putExtra(EXPLAIN, list.get(i).getmExplain());
            it.putExtra(IMAGE, list.get(i).getmImageUrl());
            startActivity(it);
        });

        rcv.setAdapter(adapter);

        getMenu();


        new SQLiteUtil(this).deleteAll();


        fbt.setOnClickListener(view -> startActivity(new Intent(this, CartActivity.class)));

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sendToken();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.cart) {
            startActivity(new Intent(this, CartActivity.class));
        } else if (id == R.id.reservation) {
            startActivity(new Intent(this, ReservationStatusActivity.class));
        } else if (id == R.id.purchaseHistory) {
            startActivity(new Intent(this, PurchaseHistoryActivity.class));

        } else if (id == R.id.logout) {
            finish();
        } else if (id == R.id.license) {
            startActivity(new Intent(this, LicenseActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void sendToken() {
        FirebaseInstanceId.getInstance().getToken();

        if (FirebaseInstanceId.getInstance().getToken() != null) {
            Log.d("token info", "token = " + FirebaseInstanceId.getInstance().getToken());

        }

        FCMResponse response = new FCMResponse(FirebaseInstanceId.getInstance().getToken());

        RetrofitManager.getRetrofitMethod().putFCMToken(Keys.getInstance().getToken(), response)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(DefaultResponse defaultResponse) {



                        if (defaultResponse.getSuccess()) {
                            Toast.makeText(getApplicationContext(), "send token", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("token error", "send token error : " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
