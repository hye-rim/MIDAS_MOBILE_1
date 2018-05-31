package com.midas.mobile.listeners;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public interface PieChartClickEventListener extends OnChartValueSelectedListener {
    @Override
    public void onValueSelected(Entry e, Highlight h);

    @Override
    public void onNothingSelected();

}
