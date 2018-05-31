package com.midas.mobile.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;

import com.midas.mobile.R;
import com.midas.mobile.entity.UserData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.service_customer.views.CustomerMainActivity;
import com.midas.mobile.service_manager.activities.ManagerActivity;
import com.midas.mobile.utils.DefaultResponse;
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

public class SignInActivity extends BaseActivity {
    final static String TAG = SignUpActivity.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Context mContext;

    /* View Components*/
    @BindView(R.id.sign_in_edittext_id)
    public EditText mEditTextID;

    @BindView(R.id.sign_in_edittext_pw)
    public EditText mEditTextPW;

    @BindView(R.id.sign_in_radiobutton_customer)
    public RadioButton mRadiobuttonCustomer;

    @BindView(R.id.sign_in_radiobutton_manager)
    public RadioButton mRadiobuttonManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        this.mContext = this;
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

    @OnClick(R.id.sign_in_button)
    public void onClickSignInButton() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String id = mEditTextID.getText().toString() + mContext.getString(R.string.midasit_email);
        String type = UserData.USER;

        if (mRadiobuttonManager.isChecked()) {
            type = UserData.ADMIN;
        }

        UserData userData = new UserData(id, mEditTextPW.getText().toString(), type);


        //server
        String finalType = type;

        RetrofitManager.getRetrofitMethod().getToken(userData)
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
                            Keys.getInstance().setToken(resultMaintenanceStatusResponse.getResult());
//                            goToMainActivity();
                            switch (finalType) {
                                case UserData.ADMIN:
                                    startActivity(new Intent(SignInActivity.this, ManagerActivity.class));
                                    break;
                                case UserData.USER:
                                    startActivity(new Intent(SignInActivity.this, CustomerMainActivity.class));
                                    break;
                            }

                            finish();

                        } else {
                            ToastUtil.makeShortToast(mContext, "로그인에 실패하였습니다");
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

    @OnClick(R.id.sign_in_button_signup)
    public void onClickSignUpButton() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    private void goToMainActivity() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }


}
