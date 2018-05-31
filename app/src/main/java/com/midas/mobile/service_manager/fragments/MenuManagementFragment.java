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
import com.midas.mobile.entity.MenuData;
import com.midas.mobile.networks.RetrofitManager;
import com.midas.mobile.service_manager.activities.CreateMenuActivity;
import com.midas.mobile.service_manager.adapters.MenuManagementRecyclerAdapter;
import com.midas.mobile.networks.model.MenuListResponse;
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
public class MenuManagementFragment extends ManagerBaseFragment implements MenuManagementRecyclerAdapter.OnMenuDetailButtonClickListener, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = MenuManagementFragment.class.getSimpleName();
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final int REQ_CODE_MENU = 10;
    private final int RES_CODE_MENU_CREATE = 11;
    private final int RES_CODE_MENU_EDIT = 12;


    private View mView;
    private Unbinder mUnbinder;
    private Context mContext;
    private ArrayList<MenuData> mMenuDataArrayList;

    private MenuManagementRecyclerAdapter mMenuRecyclerAdapter; // mRecyclerView 데이터를 전해줄 MenuManagementRecyclerAdapter
    private RecyclerView.LayoutManager mLayoutManager; // RecyclerView LayoutManager

    /*View Components*/
    @BindView(R.id.manager_menu_management_total_count)
    TextView mTextViewMenuTotalCount;
    @BindView(R.id.manager_menu_swiperefreshlayout)
    SwipeRefreshLayout mMenuSwiperefreshLayout;
    @BindView(R.id.manager_menu_management_fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.manager_menu_recyclerview)
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
        mView = inflater.inflate(R.layout.manager_fragment_menu_managment, container, false);
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

        mMenuDataArrayList = new ArrayList<>();
    }

    private void updateMenuList() {
        RetrofitManager.getRetrofitMethod().getMenu(Keys.getInstance().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MenuListResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(MenuListResponse resultMaintenanceStatusResponse) {
                        if (resultMaintenanceStatusResponse.getSuccess()) {
                            mMenuDataArrayList.clear();
                            mMenuDataArrayList.addAll(resultMaintenanceStatusResponse.getResult());

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
        mTextViewMenuTotalCount.setText(mMenuDataArrayList.size() + "");

        mMenuRecyclerAdapter = new MenuManagementRecyclerAdapter(mContext, mMenuDataArrayList);
        mMenuRecyclerAdapter.setCustomOnClickListener(this);
        mRecyclerView.setAdapter(mMenuRecyclerAdapter); //adapter 설정
        mMenuRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickEditButton(MenuData menuData, int position) {
        Intent intent = new Intent(getActivity(), CreateMenuActivity.class);
        intent.putExtra("IS_EDIT", true);
        Bundle bundle = new Bundle();
        bundle.putSerializable("menu_data", menuData);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQ_CODE_MENU);
    }

    private void editMenu() {

    }

    @Override
    public void onClickDeleteButton(MenuData menuData, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("해당 메뉴를 삭제하시겠습니까?")
                .setPositiveButton("확인", (dialogInterface, i) -> {
                    deleteMenu(menuData.getmName(), position);
                }).setNegativeButton("취소", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });
        builder.show();
    }

    private void deleteMenu(String name, int position) {

        RetrofitManager.getRetrofitMethod().deleteMenu(Keys.getInstance().getToken(), name)
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
                            mMenuDataArrayList.remove(position);
                            mTextViewMenuTotalCount.setText(mMenuDataArrayList.size() + "");

                            mMenuRecyclerAdapter.notifyItemRemoved(position);

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


    @OnClick(R.id.manager_menu_management_fab)
    public void onClickMenuFloatingActionButton() {
        Intent intent = new Intent(getActivity(), CreateMenuActivity.class);
        startActivityForResult(intent, REQ_CODE_MENU);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_MENU) {
            if (RES_CODE_MENU_CREATE == resultCode || RES_CODE_MENU_EDIT == resultCode) {
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
