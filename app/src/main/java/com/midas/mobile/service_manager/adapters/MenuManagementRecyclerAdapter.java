package com.midas.mobile.service_manager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midas.mobile.R;
import com.midas.mobile.entity.MenuData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class MenuManagementRecyclerAdapter  extends RecyclerView.Adapter<MenuManagementRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private final String TAG = CustomerManagementRecyclerAdapter.class.getSimpleName();

    private OnMenuDetailButtonClickListener mOnMenuDetailButtonClickListener;
    private LayoutInflater mLayoutInflater; //inflate 사용위한 inflater
    private ArrayList<MenuData> mMenuArrayList; //CustomerManagement의 List
    private Context mContext;

    public interface OnMenuDetailButtonClickListener {
        public void onClickEditButton(MenuData menuData, int position);
        public void onClickDeleteButton(MenuData menuData, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.menu_image)
        ImageView mImageView;
        @BindView(R.id.menu_name)
        TextView mTextViewMenuName;
        @BindView(R.id.menu_price)
        TextView mTextViewPrice;
        @BindView(R.id.menu_contents)
        TextView mTextViewContents;
        @BindView(R.id.menu_textview_update_date)
        TextView mTextViewUpdateDate;
        @BindView(R.id.menu_button_edit)
        ImageButton mImageButtonEdit;
        @BindView(R.id.menu_button_delete)
        ImageButton mImageButtonDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public MenuManagementRecyclerAdapter(Context context, ArrayList<MenuData> menuArrayList) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

        this.mMenuArrayList = new ArrayList<>();
        this.mMenuArrayList.addAll(menuArrayList);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_menu_management_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuData menu = mMenuArrayList.get(position);
        Glide.with(mContext)
                .load(mMenuArrayList.get(position).getmImageUrl())
                .into(holder.mImageView);
        holder.mTextViewMenuName.setText(menu.getmName()); //메뉴 이름
        holder.mTextViewPrice.setText(menu.getmCost() + "원"); //메뉴 가격
        holder.mTextViewContents.setText(menu.getmExplain());
        holder.mTextViewUpdateDate.setText(menu.getmUpdatedDate());
        holder.mImageButtonEdit.setTag(position);//아이템 위치를 태그로 닮
        holder.mImageButtonEdit.setOnClickListener(this);// 수정 버튼 이벤트 리스너
        holder.mImageButtonDelete.setTag(position);//아이템 위치를 태그로 닮
        holder.mImageButtonDelete.setOnClickListener(this);// 삭제 버튼 이벤트 리스너

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_button_edit:
                mOnMenuDetailButtonClickListener.onClickEditButton(mMenuArrayList.get((int) v.getTag()), (int) v.getTag());
                break;
            case R.id.menu_button_delete:
                mOnMenuDetailButtonClickListener.onClickDeleteButton(mMenuArrayList.get((int)v.getTag()), (int) v.getTag());
            default:
                break;
        }
        return;
    }

    public void setCustomOnClickListener(OnMenuDetailButtonClickListener onMenuDetailButtonClickListener) {
        this.mOnMenuDetailButtonClickListener = onMenuDetailButtonClickListener;
    }
    @Override
    public int getItemCount() {
        return mMenuArrayList.size();
    }

}

