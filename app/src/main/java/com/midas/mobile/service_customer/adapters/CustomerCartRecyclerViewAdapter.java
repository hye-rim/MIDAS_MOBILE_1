package com.midas.mobile.service_customer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midas.mobile.R;
import com.midas.mobile.entity.CartData;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class CustomerCartRecyclerViewAdapter extends RecyclerView.Adapter<CustomerCartRecyclerViewHolder> {

    private Context context;

    public CustomerCartRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    private List<CartData> items = new ArrayList<>();

    public void addData(CartData data) {
        items.add(data);
        notifyDataSetChanged();
    }

    public void addDatas(List<CartData> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    @Override
    public CustomerCartRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomerCartRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_menu_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomerCartRecyclerViewHolder holder, int position) {
        Log.e("fasdfsdaf", items.size() + "");
        Glide.with(context).load(items.get(position).getImage()).into(holder.imgCart);
        holder.txtCartSize.setText(items.get(position).getSize());
        holder.txtCartName.setText(items.get(position).getName());
        holder.txtCartCount.setText(String.valueOf(items.get(position).getCount()));
        holder.txtCartCountChange.setText(String.valueOf(items.get(position).getCount()));
        holder.txtTotalMoney.setText(calTotalMoney(items.get(position).getCount(), items.get(position).getPrice(), items.get(position).getSize()));

        holder.btnMinus.setOnClickListener(view -> {
            int changeCount = Integer.parseInt(holder.txtCartCountChange.getText().toString());
            changeCount--;
            if (changeCount < 1) {
                changeCount = 1;
            }
            holder.txtCartCountChange.setText(String.valueOf(changeCount));
            holder.txtCartCount.setText(String.valueOf(changeCount));
            holder.txtTotalMoney.setText(calTotalMoney(changeCount, items.get(position).getPrice(), items.get(position).getSize()));
        });

        holder.btnPlus.setOnClickListener(view -> {
            int changeCount = Integer.parseInt(holder.txtCartCountChange.getText().toString());
            changeCount++;
            holder.txtCartCountChange.setText(String.valueOf(changeCount));
            holder.txtCartCount.setText(String.valueOf(changeCount));
            holder.txtTotalMoney.setText(calTotalMoney(changeCount, items.get(position).getPrice(), items.get(position).getSize()));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String calTotalMoney(int count, int price, String sizeWord) {
        int size = calMoney(sizeWord);
        int total = count * (price + size);

        return convertWon(total);
    }

    private int calMoney(String size) {

        int sizeUp = 0;

        switch (size) {
            case "Small": {
                sizeUp = 0;
                break;
            }
            case "Medium": {
                sizeUp = 500;
                break;
            }
            default: {
                sizeUp = 1000;
                break;
            }
        }

        return sizeUp;
    }

    private String convertWon(int values) {
        String t = String.valueOf(values);
        StringBuilder k = new StringBuilder();

        for (int i = t.length() - 1, j = 1; i >= 0; i--, j++) {
            k.insert(0, t.charAt(i));

            if (i != 0 && j % 3 == 0) {
                k.insert(0, ",");
            }
        }
        k.indexOf(" 원");
        return k.toString();
    }
}

class CustomerCartRecyclerViewHolder extends RecyclerView.ViewHolder {

    public ImageView imgCart;

    public TextView txtCartSize;

    public TextView txtCartCount;

    public TextView txtCartName;

    public TextView txtCartCountChange;

    public Button btnMinus;

    public Button btnPlus;

    public TextView txtTotalMoney;

    public CustomerCartRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);

        imgCart = itemView.findViewById(R.id.img_item_cart);
        txtCartSize = itemView.findViewById(R.id.txt_item_cart_size);
        txtCartCount = itemView.findViewById(R.id.txt_item_cart_count);
        txtCartName = itemView.findViewById(R.id.txt_item_cart_name);
        txtCartCountChange = itemView.findViewById(R.id.txt_item_cart_count_change);
        btnPlus = itemView.findViewById(R.id.btn_item_cart_count_plus);
        btnMinus = itemView.findViewById(R.id.btn_item_cart_count_minus);
        txtTotalMoney = itemView.findViewById(R.id.txt_item_cart_total_money);
    }
}
