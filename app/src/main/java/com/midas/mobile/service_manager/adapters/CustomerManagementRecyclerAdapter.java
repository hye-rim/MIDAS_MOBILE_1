package com.midas.mobile.service_manager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.midas.mobile.R;
import com.midas.mobile.entity.UserData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class CustomerManagementRecyclerAdapter extends RecyclerView.Adapter<CustomerManagementRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private final String TAG = CustomerManagementRecyclerAdapter.class.getSimpleName();

    private OnCustomerDetailButtonClickListener mOnCustomerDetailButtonClickListener;
    private LayoutInflater mLayoutInflater; //inflate 사용위한 inflater
    private ArrayList<UserData> mUserArrayList; //CustomerManagement의 List
    private Context mContext;

    public interface OnCustomerDetailButtonClickListener {
        public void onClickEditButton(UserData userData, int position);
        public void onClickDeleteButton(UserData userData, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_name)
        TextView mTextViewUserName;
        @BindView(R.id.user_email)
        TextView mTextViewUserEmail;
        @BindView(R.id.user_register_date)
        TextView mTextViewRegisterDate;
        @BindView(R.id.user_reservation_count)
        TextView mTextViewReservationCount;
        @BindView(R.id.user_button_edit)
        ImageButton mImageButtonEdit;
        @BindView(R.id.user_button_delete)
        ImageButton mImageButtonDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public CustomerManagementRecyclerAdapter(Context context, ArrayList<UserData> userArrayList) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

        this.mUserArrayList = new ArrayList<>();
        this.mUserArrayList.addAll(userArrayList);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_customer_management_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserData user = mUserArrayList.get(position);

        if (user.type.equals(UserData.ADMIN)) {
            holder.mTextViewUserName.setText(user.name + " (관리자)");
        } else {
            holder.mTextViewUserName.setText(user.name);
        }
        holder.mTextViewUserEmail.setText(user.email);
        holder.mTextViewRegisterDate.setText(user.registeredDate);
        holder.mTextViewReservationCount.setText(user.reservationCnt);
        holder.mImageButtonEdit.setTag(position);//아이템 위치를 태그로 닮
        holder.mImageButtonEdit.setOnClickListener(this);// 수정 버튼 이벤트 리스너
        holder.mImageButtonDelete.setTag(position);//아이템 위치를 태그로 닮
        holder.mImageButtonDelete.setOnClickListener(this);// 삭제 버튼 이벤트 리스너

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_button_edit:
                mOnCustomerDetailButtonClickListener.onClickEditButton(mUserArrayList.get((int) v.getTag()), (int) v.getTag());
                break;
            case R.id.user_button_delete:
                mOnCustomerDetailButtonClickListener.onClickDeleteButton(mUserArrayList.get((int)v.getTag()), (int) v.getTag());
            default:
                break;
        }
        return;
    }

    public void setCustomOnClickListener(CustomerManagementRecyclerAdapter.OnCustomerDetailButtonClickListener onCustomerDetailButtonClickListener) {
        this.mOnCustomerDetailButtonClickListener = onCustomerDetailButtonClickListener;
    }

    @Override
    public int getItemCount() {
        return mUserArrayList.size();
    }

}
