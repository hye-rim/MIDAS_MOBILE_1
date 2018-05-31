package com.midas.mobile.service_manager.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midas.mobile.R;
import com.midas.mobile.activities.SignUpActivity;
import com.midas.mobile.entity.UserData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.service_manager.activities.EditCustomerActivity;
import com.midas.mobile.service_manager.adapters.CustomerManagementRecyclerAdapter;
import com.midas.mobile.networks.model.CustomerListResponse;
import com.midas.mobile.utils.DefaultResponse;
import com.midas.mobile.utils.Keys;
import com.midas.mobile.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.midas.mobile.networks.RetrofitManager.catchAllThrowable;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class UserManagermentFragment extends ManagerBaseFragment implements CustomerManagementRecyclerAdapter.OnCustomerDetailButtonClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = MenuManagementFragment.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final int RES_CODE_USER_CREATE = 1;
    private final int RES_CODE_USER_EDIT = 3;
    private final int REQ_CODE_USER = 2;

    private View mView;
    private Unbinder mUnbinder;
    private Context mContext;
    private ArrayList<UserData> mUserDataArrayList;

    private CustomerManagementRecyclerAdapter mCustomerRecyclerAdapter; // mRecyclerView 데이터를 전해줄 CustomerManagementRecyclerAdapter
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager

    /*View Components*/
    @BindView(R.id.manager_customer_management_total_count)
    TextView mTextViewCustomerTotalCount;
    @BindView(R.id.manager_customer_swiperefreshlayout)
    SwipeRefreshLayout mMenuSwiperefreshLayout;
    @BindView(R.id.manager_customer_management_fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.manager_customer_recyclerview)
    RecyclerView mRecyclerView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.manager_fragment_customer_managment, container, false);
        mUnbinder = ButterKnife.bind(this, mView);
        mContext = this.getContext();

        init();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int pastVisibleItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (pastVisibleItems == 0) {    //리스트의 첫 화면인 경우
                    mFloatingActionButton.setVisibility(View.VISIBLE);
                } else if (!recyclerView.canScrollVertically(0)) {    //아래로 스크롤 할 수 없는 경우
                    mFloatingActionButton.setVisibility(View.GONE);
                } else {
                    mFloatingActionButton.setVisibility(View.VISIBLE);
                }
            }
        });

        updateMenuList();

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

    private void init() {
        mLayoutManager = new LinearLayoutManager(mContext);

        mMenuSwiperefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager); //layout 설정

        mUserDataArrayList = new ArrayList<>();
    }

    private void updateMenuList() {
        RetrofitManager.getRetrofitMethod().getUser(Keys.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerListResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(CustomerListResponse resultMaintenanceStatusResponse) {
                        if (resultMaintenanceStatusResponse.getSuccess()) {
                            mUserDataArrayList.clear();
                            mUserDataArrayList.addAll(resultMaintenanceStatusResponse.getResult());

                            updateView();
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.makeShortToast(mContext, catchAllThrowable(mContext, throwable));
                    }

                    @Override
                    public void onComplete() {


                    }
                });

    }

    private void updateView() {
        mTextViewCustomerTotalCount.setText(mUserDataArrayList.size() + "");

        mCustomerRecyclerAdapter = new CustomerManagementRecyclerAdapter(mContext, mUserDataArrayList);
        mCustomerRecyclerAdapter.setCustomOnClickListener(this);
        mRecyclerView.setAdapter(mCustomerRecyclerAdapter); //adapter 설정
        mCustomerRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickEditButton(UserData userData, int position) {
        Intent intent = new Intent(getActivity(), EditCustomerActivity.class);
        intent.putExtra("IS_EDIT", true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", userData);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQ_CODE_USER);
    }

    @Override
    public void onClickDeleteButton(UserData userData, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("해당 유저를 삭제하시겠습니까?")
                .setPositiveButton("확인", (dialogInterface, i) -> {
                    deleteUser(userData.email, position);
                }).setNegativeButton("취소", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });
        builder.show();
    }

    private void deleteUser(String email, int position) {
        RetrofitManager.getRetrofitMethod().deleteUser(Keys.getInstance().getToken(), email)
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
                            mUserDataArrayList.remove(position);
                            mCustomerRecyclerAdapter.notifyItemRemoved(position);
                        } else {
                            ToastUtil.makeShortToast(mContext, resultMaintenanceStatusResponse.getMessage());
                        }
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.makeShortToast(mContext, catchAllThrowable(mContext, throwable));
                    }

                    @Override
                    public void onComplete() {


                    }
                });

    }

    @OnClick(R.id.manager_customer_management_fab)
    public void onClickUserFloatingActionButton() {
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        startActivityForResult(intent, REQ_CODE_USER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_USER) {
            if (RES_CODE_USER_EDIT == resultCode || RES_CODE_USER_CREATE == resultCode) {
                onRefresh();
            }

        }
    }

    @Override
    public void onRefresh() {
        // 새로고침 코드
        updateMenuList();

        mMenuSwiperefreshLayout.setRefreshing(false);
    }

}

