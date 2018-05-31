package com.midas.mobile.service_customer.views;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.midas.mobile.R;
import com.midas.mobile.entity.CartData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.service_customer.adapters.CustomerCartRecyclerViewAdapter;
import com.midas.mobile.service_customer.model.OrderContents;
import com.midas.mobile.service_customer.model.OrderRequestBody;
import com.midas.mobile.utils.DefaultResponse;
import com.midas.mobile.utils.Keys;
import com.midas.mobile.utils.SQLiteUtil;
import com.midas.mobile.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {
    private final static String TAG = CartActivity.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @BindView(R.id.rcv_customer_cart)
    public RecyclerView rcvCustomerCart;

    @BindView(R.id.txt_cart_total_cost)
    public TextView txtTotalCost;

    @BindView(R.id.btn_cart_order)
    public Button btnCartOrder;

    @BindView(R.id.spinner_reservation_time)
    public Spinner spinner;

    private List<OrderContents> orderContents = new ArrayList<>();

    private CustomerCartRecyclerViewAdapter adapter;

    private String[] times = {"AM 10:00", "AM 11:00", "PM 12:00", "PM 1:00", "PM 2:00", "PM 3:00", "PM 4:00", "PM 5:00", "PM 6:00"};

    private String time = TimeUtil.currentTime();

    private String curTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(this);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, times);

        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i < 3) {
                    String[] d = times[i].split(" ");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formatDate = simpleDateFormat.format(new Date(System.currentTimeMillis()));

                    formatDate = formatDate + " " + d[1] + ":00";
                    curTime = formatDate;
                } else {
                    String[] d = times[i].split(" ");
                    String[] dd = d[1].split(":");
                    int p = Integer.parseInt(dd[0]);
                    p += 12;

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String formatDate = simpleDateFormat.format(new Date(System.currentTimeMillis()));

                    formatDate = formatDate + " " + p + ":00:00";
                    curTime = formatDate;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                curTime = time;
            }
        });


        List<CartData> list = new ArrayList<>();

        SQLiteUtil sqLiteUtils = new SQLiteUtil(this);
        Cursor cursor = sqLiteUtils.select();

        int sum = 0;

        while (cursor.moveToNext()) {
            int number = cursor.getInt(0);
            String name = cursor.getString(1);
            int count = cursor.getInt(2);
            int price = cursor.getInt(3);
            String imageUrl = cursor.getString(4);
            String size = cursor.getString(5);

            sum += calTotalMoney(count, price, size);

            orderContents.add(new OrderContents(name, size, count));

            list.add(new CartData(number, name, count, price, size, imageUrl));
        }


        cursor.close();
        sqLiteUtils.close();
        Log.e("test", list.size() + "");

        txtTotalCost.setText(convertWon(sum));

        adapter = new CustomerCartRecyclerViewAdapter(getApplication());

        rcvCustomerCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCustomerCart.setHasFixedSize(true);
        rcvCustomerCart.setAdapter(adapter);

        adapter.addDatas(list);

        btnCartOrder.setOnClickListener(view -> reservation(curTime));
    }
    
    private void reservation(String time) {


        OrderRequestBody orderRequestBody = new OrderRequestBody(time, orderContents);

        RetrofitManager.getRetrofitMethod().postReservation(Keys.getInstance().getToken(), orderRequestBody)
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
                            Toast.makeText(getApplicationContext(), "예약을 완료했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("cart error", e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        // TODO delete sqlite
                        SQLiteUtil utils = new SQLiteUtil(getApplicationContext());
                        utils.deleteAll();
                        adapter.clear();
                    }
                });
    }

    private int calTotalMoney(int count, int price, String sizeWord) {
        int size = calMoney(sizeWord);
        int total = count * (price + size);

        return total;
    }

    private int calMoney(String size) {

        int sizeUp = 0;

        switch (size) {
            case "Small": {
                sizeUp = 0;
                break;
            }
            case "Medium": {
                sizeUp = 500;
                break;
            }
            default: {
                sizeUp = 1000;
                break;
            }
        }

        return sizeUp;
    }

    private String convertWon(int values) {
        String t = String.valueOf(values);
        StringBuilder k = new StringBuilder();

        for (int i = t.length() - 1, j = 1; i >= 0; i--, j++) {
            k.insert(0, t.charAt(i));

            if (i != 0 && j % 3 == 0) {
                k.insert(0, ",");
            }
        }
        return k.toString();
    }
}
