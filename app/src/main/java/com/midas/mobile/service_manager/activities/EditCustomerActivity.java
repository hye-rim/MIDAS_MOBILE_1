package com.midas.mobile.service_manager.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.midas.mobile.R;
import com.midas.mobile.activities.BaseActivity;
import com.midas.mobile.entity.UserData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.utils.DefaultResponse;
import com.midas.mobile.utils.Keys;
import com.midas.mobile.utils.TimeUtil;
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
public class EditCustomerActivity extends BaseActivity {
    private final static String TAG = EditCustomerActivity.class.getSimpleName();
    private final int RES_CODE_USER_CREATE = 1;
    private final int RES_CODE_USER_EDIT = 3;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private Context mContext;
    private UserData mUser;
    private boolean isEdit;

    /* View Components*/
    @BindView(R.id.customer_create_textview_email)
    TextView mTextViewEmail;
    @BindView(R.id.customer_create_textview_name)
    TextView mTextViewName;
    @BindView(R.id.customer_create_textview_reservation_count)
    TextView mTextViewReservationCount;
    @BindView(R.id.customer_create_textview_register_date)
    TextView mTextViewRegisternDate;

    @BindView(R.id.customer_create_radiobutton_customer)
    RadioButton mRadioButtonCustomer;
    @BindView(R.id.customer_create_radiobutton_manager)
    RadioButton mRadioButtonManager;
    @BindView(R.id.customer_create_button_edit)
    Button mEditButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity_customer_create);
        ButterKnife.bind(this);
        this.mContext = this;

        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);
        mUser = (UserData) getIntent().getSerializableExtra("user");

        init();


    }

    private void init() {
        mTextViewEmail.setText(mUser.email);
        mTextViewName.setText(mUser.name);
        mTextViewReservationCount.setText(mUser.reservationCnt);
        mTextViewRegisternDate.setText(mUser.registeredDate);

        if (mUser.type == UserData.ADMIN) {
            mRadioButtonManager.setChecked(true);
            mRadioButtonCustomer.setChecked(false);
        } else {
            mRadioButtonManager.setChecked(false);
            mRadioButtonCustomer.setChecked(true);
        }
    }

    private void initDefault() {
        mTextViewRegisternDate.setText(TimeUtil.currentTime());
        mEditButton.setText("생성");
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

    @OnClick(R.id.customer_create_button_edit)
    public void onClickEditButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("정보를 수정하시겠습니까?")
                .setPositiveButton("확인", (dialogInterface, i) -> {
                    editUser();
                }).setNegativeButton("취소", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });
        builder.show();

    }

    private void editUser() {
        if (mRadioButtonCustomer.isChecked()) {
            mUser.type = UserData.USER;
        } else {
            mUser.type = UserData.ADMIN;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", mUser.type);
        RetrofitManager.getRetrofitMethod().editUser(Keys.getInstance().getToken(), mUser.email.trim(), jsonObject)
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
                            ToastUtil.makeShortToast(mContext, resultMaintenanceStatusResponse.getMessage());
                            setResult(RES_CODE_USER_EDIT);
                            onBackPressed();
                        } else {
                            ToastUtil.makeShortToast(mContext, resultMaintenanceStatusResponse.getMessage());
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
