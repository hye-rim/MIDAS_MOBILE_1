package com.midas.mobile.service_manager.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.midas.mobile.R;
import com.midas.mobile.activities.BaseActivity;
import com.midas.mobile.entity.MenuData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.utils.DefaultResponse;
import com.midas.mobile.utils.FormValidatorUtil;
import com.midas.mobile.utils.Keys;
import com.midas.mobile.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.midas.mobile.networks.RetrofitManager.catchAllThrowable;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class CreateMenuActivity extends BaseActivity {
    private final static String TAG = CreateMenuActivity.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final int RES_CODE_MENU_CREATE = 11;
    private final int RES_CODE_MENU_EDIT = 12;
    private final String[] IMAGELIST = {"http://hanul.name/cafeM/1.jpg", "http://hanul.name/cafeM/2.jpg",
            "http://hanul.name/cafeM/3.jpg", "http://hanul.name/cafeM/4.jpg", "http://hanul.name/cafeM/5.jpg",
            "http://hanul.name/cafeM/6.jpg", "http://hanul.name/cafeM/7.jpg"};


    private Context mContext;
    private MenuData mMenu;
    private int mImageIndex;
    private Boolean isEdit;

    /* View Components*/
    @BindView(R.id.menu_create_imageview)
    ImageView mImageView;
    @BindView(R.id.menu_create_name)
    EditText mEditTextName;
    @BindView(R.id.menu_create_price)
    EditText mEditTextPrice;
    @BindView(R.id.menu_create_image_refresh)
    ImageButton mImageButtonRefresh;
    @BindView(R.id.menu_create_contents)
    EditText mEditTextContents;
    @BindView(R.id.manager_menu_create_button)
    Button mCreateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_menu_create);
        ButterKnife.bind(this);
        this.mContext = this;

        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
        if (isEdit) {
            mMenu = (MenuData) getIntent().getSerializableExtra("menu_data");
            initEdited();

        } else {
            initDefault();
        }


    }

    private void initEdited() {
        mEditTextName.setText(mMenu.getmName());
        mEditTextPrice.setText(mMenu.getmCost());
        mEditTextContents.setText(mMenu.getmExplain());
        Glide.with(mContext)
                .load(mMenu.getmImageUrl())
                .into(mImageView);
//        mImageButtonRefresh.setEnabled(false);
        mEditTextName.setEnabled(false);
        mCreateButton.setText("수정");
    }

    private void initDefault() {
        mImageIndex = 0;
        Glide.with(mContext)
                .load(IMAGELIST[mImageIndex])
                .into(mImageView);
    }

    @Override
    public void onBackPressed() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

    @OnClick(R.id.menu_create_image_refresh)
    public void onClickRefreshButton() {
        mImageIndex = (mImageIndex < 6 ? mImageIndex + 1 : 0);

        Glide.with(mContext)
                .load(IMAGELIST[mImageIndex])
                .into(mImageView);
    }

    @OnClick(R.id.manager_menu_create_button)
    public void onClickCreateButton() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (FormValidatorUtil.validateStringIsEmpty(mEditTextName.getText().toString()) || FormValidatorUtil.validateStringIsEmpty(mEditTextPrice.getText().toString())
                || FormValidatorUtil.validateStringIsEmpty(mEditTextContents.getText().toString())) {
            ToastUtil.makeShortToast(mContext, "입력한 정보를 다시 확인해 주세요");
            return;
        }


        if (!isEdit) {
            MenuData menuData = new MenuData(mEditTextName.getText().toString().trim(), mEditTextPrice.getText().toString().trim(), mEditTextContents.getText().toString(), IMAGELIST[mImageIndex]);
            createMenu(menuData);
        } else {
            MenuData menuData = new MenuData(mEditTextName.getText().toString().trim(), mEditTextPrice.getText().toString().trim(), mEditTextContents.getText().toString(), mMenu.getmImageUrl());
            editMenu(menuData);
        }

    }

    private void createMenu(MenuData menuData) {
        RetrofitManager.getRetrofitMethod().postMenu(Keys.getInstance().getToken(), menuData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(DefaultResponse resultMaintenanceStatusResponse) {
                        if (resultMaintenanceStatusResponse.getSuccess()) {
                            ToastUtil.makeShortToast(mContext, "메뉴가 생성되었습니다");
                            setResult(RES_CODE_MENU_CREATE);
                            finish();
                        } else {
                            ToastUtil.makeShortToast(mContext, "메뉴 생성에 실패하였습니다");
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.makeShortToast(mContext, catchAllThrowable(getApplicationContext(), throwable));
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    private void editMenu(MenuData menuData) {
        RetrofitManager.getRetrofitMethod().putMenu(Keys.getInstance().getToken(), menuData.getmName(), menuData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DefaultResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(DefaultResponse resultMaintenanceStatusResponse) {
                        if (resultMaintenanceStatusResponse.getSuccess()) {
                            ToastUtil.makeShortToast(mContext, "메뉴가 수정되었습니다");
                            setResult(RES_CODE_MENU_EDIT);
                            finish();
                        } else {
                            ToastUtil.makeShortToast(mContext, "메뉴 수정에 실패하였습니다");
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.makeShortToast(mContext, catchAllThrowable(getApplicationContext(), throwable));
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

}
