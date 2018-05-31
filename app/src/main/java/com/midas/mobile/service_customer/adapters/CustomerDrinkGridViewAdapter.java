package com.midas.mobile.service_customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midas.mobile.R;
import com.midas.mobile.entity.MenuData;

import java.util.ArrayList;
import java.util.List;

public class CustomerDrinkGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<MenuData> items = new ArrayList<>();

    private View v;
    LayoutInflater inflater;

    public CustomerDrinkGridViewAdapter(Context context) {

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public int getCount() {
        return items.size();
    }

    @Override
    public MenuData getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.item_customer_main, parent, false);


        ImageView imgCustomerItemDrink = convertView.findViewById(R.id.imgItemMenu);
        TextView txtCustomerItemDrinkName = convertView.findViewById(R.id.txtItemMenuName);
//        TextView txtItemMenuPrice = convertView.findViewById(R.id.txtItemMenuPrice);
        TextView txtCustomerItemDrinkCost = convertView.findViewById(R.id.txtItemMenuPrice);



        TextView txtCustomerItemDrinkExplain = convertView.findViewById(R.id.txtItemMenuExplain);


        Glide.with(context).load(items.get(position).getmImageUrl()).into(imgCustomerItemDrink);
        txtCustomerItemDrinkName.setText(items.get(position).getmName());
//        txtCustomerItemDrinkCost.setText(items.get(position).getmCost());

        txtCustomerItemDrinkCost.setText(String.format("%,d", Integer.parseInt(items.get(position).getmCost())) + " 원");

//        txtItemMenuPrice.setText(items.get(position).getmCost());
        txtCustomerItemDrinkExplain.setText(items.get(position).getmExplain());

        return convertView;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return items.get(position).getType();
//    }
//
//    class CustomerDrinkRecyclerViewHolder extends GenericViewHolder {
//
//        @BindView(R.id.imgItemMenu)
//        public ImageView imgCustomerItemDrink;
//        @BindView(R.id.txtItemMenuName)
//        public TextView txtCustomerItemDrinkName;
//        @BindView(R.id.txtItemMenuPrice)
//        public TextView txtCustomerItemDrinkCost;
//        @BindView(R.id.txtItemMenuExplain)
//        public TextView txtCustomerItemDrinkExplain;
//
//        public CustomerDrinkRecyclerViewHolder(View v) {
//            super(v);
//            ButterKnife.bind(this,v);
//        }
//
//        @Override
//        public void setDataOnView(int position) {
//
//            Glide.with(context).load(items.get(position).getmImageUrl()).into(imgCustomerItemDrink);
//            txtCustomerItemDrinkName.setText(items.get(position).getmName());
//            txtCustomerItemDrinkCost.setText(items.get(position).getmCost());
//            txtCustomerItemDrinkExplain.setText(items.get(position).getmExplain());
//
//        }
//    }
//
//    class CustomerDrinkRecyclerViewHolder2 extends GenericViewHolder {
//
//        @BindView(R.id.imgItemMenu2)
//        public ImageView imgCustomerItemDrink;
//        @BindView(R.id.txtItemMenuName2)
//        public TextView txtCustomerItemDrinkName;
//        @BindView(R.id.txtItemMenuPrice2)
//        public TextView txtCustomerItemDrinkCost;
//        @BindView(R.id.txtItemMenuExplain2)
//        public TextView txtCustomerItemDrinkExplain;
//
//        public CustomerDrinkRecyclerViewHolder2(View v) {
//            super(v);
//            ButterKnife.bind(this, v);
//        }
//
//        @Override
//        public void setDataOnView(int position) {
//            // TODO add glide
//            Glide.with(context).load(items.get(position).getmImageUrl()).into(imgCustomerItemDrink);
//            txtCustomerItemDrinkName.setText(items.get(position).getmName());
//            txtCustomerItemDrinkCost.setText(items.get(position).getmCost());
//            txtCustomerItemDrinkExplain.setText(items.get(position).getmExplain());
//        }
//    }

}
//
//abstract class GenericViewHolder extends RecyclerView.ViewHolder {
//    public GenericViewHolder(View itemView) {
//        super(itemView);
//    }
//
//    public abstract void setDataOnView(int position);
//}


