package com.midas.mobile.service_customer.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.midas.mobile.R;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.service_customer.model.OrderContents;
import com.midas.mobile.service_customer.model.OrderRequestBody;
import com.midas.mobile.utils.DefaultResponse;
import com.midas.mobile.utils.Keys;
import com.midas.mobile.utils.SQLiteUtil;
import com.midas.mobile.utils.TimeUtil;

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

public class DetailMenuInfoActivity extends AppCompatActivity {

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @BindView(R.id.txt_detail_menu_title)
    public TextView txtMenuTitle;

    @BindView(R.id.txt_detail_menu_explain)
    public TextView txtExplain;

    @BindView(R.id.txt_detail_menu_price)
    public TextView txtPrice;

    @BindView(R.id.img_detail_menu)
    public ImageView imgMenu;

    @BindView(R.id.txt_detail_menu_total_price)
    public TextView txtTotalPrice;

    @BindView(R.id.txt_detail_menu_count)
    public TextView txtCount;

    @BindView(R.id.btn_detail_menu_count_minus)
    public Button btnMinus;

    @BindView(R.id.btn_detail_menu_count_plus)
    public Button btnPlus;

    @BindView(R.id.btn_detail_menu_order)
    public Button btnOrder;

    @BindView(R.id.btn_detail_add_basket)
    public Button btnBasket;

    @BindView(R.id.layout_size)
    public RelativeLayout layoutSize;

    @BindView(R.id.txt_detail_menu_size)
    public TextView txtMenuSize;

    private int count = 1;

    private String name;
    private String price;
    private String explain;
    String choice = "";
    private String imageUrl;

    private String[] size = {"Small", "Medium", "Large"};

    private List<OrderContents> orderContents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu_info);

        ButterKnife.bind(this);

        Intent it = getIntent();
        name = it.getStringExtra(NAME);
        price = it.getStringExtra(PRICE);
        explain = it.getStringExtra(EXPLAIN);
        imageUrl = it.getStringExtra(IMAGE);

        init();
    }

    private void init() {
        txtMenuTitle.setText(name);
        txtExplain.setText(explain);

        txtMenuSize.setText("Small");

        Glide.with(this).load(imageUrl).into(imgMenu);

        txtCount.setText(String.valueOf(count));
        int totalC = Integer.parseInt(price) * count;
        txtTotalPrice.setText(convertWon(totalC));

        txtPrice.setText(convertWon(Integer.parseInt(price)));

        btnPlus.setOnClickListener(view -> {
            count++;
            txtCount.setText(String.valueOf(count));
            calTotalMoney();
        });

        btnMinus.setOnClickListener(view -> {
            count--;
            if (count < 1) {
                count = 1;
            }
            txtCount.setText(String.valueOf(count));
            calTotalMoney();
        });

        layoutSize.setOnClickListener(view -> {

            int index = 0;
            for (int i = 0; i < size.length; i++) {
                if (txtMenuSize.getText().toString().trim().equals(size[i])) {
                    index = i;
                    break;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setSingleChoiceItems(size, index,
                            (dialogInterface, i) -> choice = size[i])
                    .setPositiveButton("확인", (dialogInterface, i) -> {
                        txtMenuSize.setText(choice);
                        calTotalMoney();
                    }).setNegativeButton("취소", (dialogInterface, i) -> calTotalMoney());
            builder.show();
        });


        btnOrder.setOnClickListener(view -> {
            String time = TimeUtil.currentTime();

            orderContents.add(new OrderContents(txtMenuTitle.getText().toString(), txtMenuSize.getText().toString(), Integer.parseInt(txtCount.getText().toString())));

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
                        }
                    });
        });

        btnBasket.setOnClickListener(view -> {
            SQLiteUtil sqlite = new SQLiteUtil(getApplicationContext());
            sqlite.insert(name, Integer.parseInt(txtCount.getText().toString().trim()), Integer.parseInt(price), imageUrl, txtMenuSize.getText().toString().trim());
            sqlite.close();
            Toast.makeText(this, "장바구니에 추가하였습니다.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void calTotalMoney() {
        int count = Integer.parseInt(txtCount.getText().toString().trim());
        int cost = Integer.parseInt(price);
        int size = calMoney();

        int total = count * (cost + size);

        txtTotalPrice.setText(convertWon(total));
    }

    private int calMoney() {

        int sizeUp = 0;

        switch (txtMenuSize.getText().toString().trim()) {
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
