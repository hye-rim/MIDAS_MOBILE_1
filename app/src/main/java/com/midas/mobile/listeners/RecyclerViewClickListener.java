package com.midas.mobile.listeners;

import android.view.View;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public interface RecyclerViewClickListener {
    public void onClick(View view, int position); //click event

    public void onLongClick(View view, int position); //long click event

}
