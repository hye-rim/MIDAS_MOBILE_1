package com.midas.mobile.listeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hyerim on 2018. 5. 26....
 */
public class RecyclerClickListener implements RecyclerView.OnItemTouchListener {
    private RecyclerViewClickListener mClicklistener; //clicklistener
    private GestureDetector mGestureDetector;

    public RecyclerClickListener(Context context, final RecyclerView recycleView, final RecyclerViewClickListener clicklistener) {

        this.mClicklistener = clicklistener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
//                View child=recycleView.findChildViewUnder(e.getX(),e.getY());
//                if(child!=null && clicklistener!=null){
//                    clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
//                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mClicklistener != null && mGestureDetector.onTouchEvent(e)) {
            mClicklistener.onClick(child, rv.getChildAdapterPosition(child));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
