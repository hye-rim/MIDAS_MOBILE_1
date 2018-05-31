package com.midas.mobile.service_manager.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.midas.mobile.R;
import com.midas.mobile.activities.BaseActivity;
import com.midas.mobile.custom.MainViewPager;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.networks.model.FCMResponse;
import com.midas.mobile.service_manager.adapters.ManagerViewPagerAdapter;
import com.midas.mobile.utils.DefaultResponse;
import com.midas.mobile.utils.Keys;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class ManagerActivity extends BaseActivity {
    private final static String TAG = ManagerActivity.class.getSimpleName();
    private Context mContext;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private ManagerViewPagerAdapter mMainViewPagerAdapter;

    /* View Components*/
    @BindView(R.id.manager_viewpager)
    public MainViewPager mViewPager;
    @BindView(R.id.manager_navigationtabbar)
    public NavigationTabBar mNavigationTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_main);

        this.mContext = this;
        ButterKnife.bind(this);

        init();

        sendToken();

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

    private void init() {
        mMainViewPagerAdapter = new ManagerViewPagerAdapter(getSupportFragmentManager(),
                4);
        mViewPager.setAdapter(mMainViewPagerAdapter);

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_user),
                        R.color.colorPrimary)
                        .title(mContext.getString(R.string.menu_user))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_cart),
                        R.color.colorPrimary)
                        .title(mContext.getString(R.string.menu_cart))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_mug_alt),
                        R.color.colorPrimary)
                        .title(mContext.getString(R.string.menu_menu))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_datareport),
                        R.color.colorPrimary)
                        .title(mContext.getString(R.string.menu_chart))
                        .build()
        );

//        mNavigationTabBar.setIconSizeFraction(0.5f);
        mViewPager.setPagingEnabled(false);

        mNavigationTabBar.setModels(models);
        mNavigationTabBar.setViewPager(mViewPager, 0);
        mNavigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                mNavigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        mNavigationTabBar.postDelayed(() -> {
            for (int i = 0; i < mNavigationTabBar.getModels().size(); i++) {
                final NavigationTabBar.Model model = mNavigationTabBar.getModels().get(i);
                mNavigationTabBar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        model.showBadge();
                    }
                }, i * 100);
            }
        }, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainViewPagerAdapter.notifyDataSetChanged();
    }
}
