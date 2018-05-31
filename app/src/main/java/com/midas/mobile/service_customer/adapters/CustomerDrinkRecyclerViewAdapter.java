package com.midas.mobile.service_customer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midas.mobile.R;
import com.midas.mobile.entity.MenuData;
import com.midas.mobile.service_customer.views.DetailMenuInfoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.midas.mobile.service_customer.event.IntentKey.EXPLAIN;
import static com.midas.mobile.service_customer.event.IntentKey.IMAGE;
import static com.midas.mobile.service_customer.event.IntentKey.NAME;
import static com.midas.mobile.service_customer.event.IntentKey.PRICE;

public class CustomerDrinkRecyclerViewAdapter extends RecyclerView.Adapter<GenericViewHolder> {

    private Context context;
    private List<MenuData> items = new ArrayList<>();

    private View v;

    public CustomerDrinkRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void addData(MenuData item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void addDatas(List<MenuData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_customer_main, parent, false);
        return new CustomerDrinkRecyclerViewHolder(v);
//        switch (viewType) {
//            case 0: {
//                v = inflater.inflate(R.layout.item_menu_list_1, parent, false);
//                return new CustomerDrinkRecyclerViewHolder(v);
//            }
//            default: {
//                v = inflater.inflate(R.layout.item_menu_list_2, parent, false);
//                return new CustomerDrinkRecyclerViewHolder2(v);
//            }
//        }
    }


    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.setDataOnView(position);
        Intent it = new Intent(context, DetailMenuInfoActivity.class);
        it.putExtra(NAME, items.get(position).getmName());
        it.putExtra(PRICE, items.get(position).getmCost());
        it.putExtra(EXPLAIN, items.get(position).getmExplain());
        it.putExtra(IMAGE, items.get(position).getmImageUrl());
        holder.itemView.setOnClickListener(view -> context.startActivity(it));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CustomerDrinkRecyclerViewHolder extends GenericViewHolder {

        @BindView(R.id.imgItemMenu)
        public ImageView imgCustomerItemDrink;
        @BindView(R.id.txtItemMenuName)
        public TextView txtCustomerItemDrinkName;
        @BindView(R.id.txtItemMenuPrice)
        public TextView txtCustomerItemDrinkCost;
        @BindView(R.id.txtItemMenuExplain)
        public TextView txtCustomerItemDrinkExplain;

        public CustomerDrinkRecyclerViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }

        @Override
        public void setDataOnView(int position) {

            Glide.with(context).load(items.get(position).getmImageUrl()).into(imgCustomerItemDrink);
            txtCustomerItemDrinkName.setText(items.get(position).getmName());
            txtCustomerItemDrinkCost.setText(items.get(position).getmCost());
            txtCustomerItemDrinkExplain.setText(items.get(position).getmExplain());

        }
    }

    class CustomerDrinkRecyclerViewHolder2 extends GenericViewHolder {

        @BindView(R.id.imgItemMenu2)
        public ImageView imgCustomerItemDrink;
        @BindView(R.id.txtItemMenuName2)
        public TextView txtCustomerItemDrinkName;
        @BindView(R.id.txtItemMenuPrice2)
        public TextView txtCustomerItemDrinkCost;
        @BindView(R.id.txtItemMenuExplain2)
        public TextView txtCustomerItemDrinkExplain;

        public CustomerDrinkRecyclerViewHolder2(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @Override
        public void setDataOnView(int position) {
            // TODO add glide
            Glide.with(context).load(items.get(position).getmImageUrl()).into(imgCustomerItemDrink);
            txtCustomerItemDrinkName.setText(items.get(position).getmName());
            txtCustomerItemDrinkCost.setText(items.get(position).getmCost());
            txtCustomerItemDrinkExplain.setText(items.get(position).getmExplain());
        }
    }

}

abstract class GenericViewHolder extends RecyclerView.ViewHolder {
    public GenericViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setDataOnView(int position);
}


