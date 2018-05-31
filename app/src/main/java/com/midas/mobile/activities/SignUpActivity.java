package com.midas.mobile.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.midas.mobile.R;
import com.midas.mobile.entity.UserData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.utils.DefaultResponse;
import com.midas.mobile.utils.FormValidatorUtil;
import com.midas.mobile.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.midas.mobile.networks.RetrofitManager.catchAllThrowable;

public class SignUpActivity extends BaseActivity {
    final static String TAG = SignUpActivity.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Context mContext;

    /* View Components*/
    @BindView(R.id.sign_up_edittext_id)
    EditText mEditTextID;

    @BindView(R.id.sign_up_edittext_name)
    EditText mEditTextName;

    @BindView(R.id.sign_up_edittext_pw)
    EditText mEditTextPW;

    @BindView(R.id.sign_up_edittext_pw_confirm)
    EditText mEditTextPWConfirm;

    @BindView(R.id.sign_up_textview_pw_alert)
    TextView mTextViewPwAlert;

    @BindView(R.id.sign_up_button)
    Button mButtonToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        this.mContext = this;

        init();
        updateUserInterface();

    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnTextChanged({R.id.sign_up_edittext_pw, R.id.sign_up_edittext_pw_confirm})
    public void setTextChangedPwEditText() {
        updateUserInterface();
    }

    private void updateUserInterface() {
        mTextViewPwAlert.setVisibility(View.INVISIBLE);
        if (isPwSame()) {
            mTextViewPwAlert.setVisibility(View.INVISIBLE);
        } else {
            mTextViewPwAlert.setText("비밀번호가 일치하지 않습니다");
            mTextViewPwAlert.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.sign_up_button)
    public void onClickSignUpButton() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (!isPwSame()) {
            ToastUtil.makeShortToast(mContext, "입력한 정보를 다시 확인해 주세요");
            updateUserInterface();
            return;
        }

        String id = mEditTextID.getText().toString();
        String name = mEditTextName.getText().toString();
        String password = mEditTextPW.getText().toString();

        if (FormValidatorUtil.validateStringIsEmpty(id) || FormValidatorUtil.validateStringIsEmpty(name) || FormValidatorUtil.validateStringIsEmpty(password)) {
            ToastUtil.makeShortToast(mContext, "입력한 정보를 다시 확인해 주세요");
            updateUserInterface();
            return;
        }

        //회원가입은 사용자만 가능 / 관리자는 서버에서 직접 부여
        createUser(id + mContext.getString(R.string.midasit_email), name, password, UserData.USER);

    }

    private void createUser(String email, String name, String password, String type) {
        UserData userData = new UserData(email, name, password, type);

        RetrofitManager.getRetrofitMethod().postUser(userData)
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
                            goToSignInActivity();
                            ToastUtil.makeShortToast(mContext, "가입이 완료되었습니다.");
                        } else {
                            ToastUtil.makeShortToast(mContext, "가입에 실패하였습니다");
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

    //비밀번호와 비밀 번호의 확인이 일치하는지 판별하는 메서드. 일치하면 true, 다르면 false
    private boolean isPwSame() {
        return (mEditTextPW.getText().toString().equals(mEditTextPWConfirm.getText().toString()));
    }

    private void goToSignInActivity() {
        finish();
    }
}
