package com.midas.mobile.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.midas.mobile.R;
import com.midas.mobile.service_customer.model.ReservationChartModel;

import java.util.ArrayList;
import java.util.List;

public class BarChartUtil implements OnChartValueSelectedListener {

    private Context context;
    private BarChart mChart;

    private List<ReservationChartModel> list = new ArrayList<>();

    public BarChartUtil(Context context, BarChart barChart) {
        this.context = context;
        this.mChart = barChart;
    }

    public void init() {

        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);


        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

    }

    public void setData(int[] models) {

        float start = 1f;

        int count = 12;
        int range = 0;
        for(int m: models) {
            range += m;
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = (int) start; i < start + count; i++) {
            float mult = (range + 1);
            float val = models[i];

            Log.e("test", mult + " : " + val);

            yVals1.add(new BarEntry(i, val));

        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "2018");

            set1.setDrawIcons(false);

            int startColor1 = ContextCompat.getColor(context, R.color.colorPrimary);
            int startColor2 = ContextCompat.getColor(context, R.color.colorPrimary);
            int startColor3 = ContextCompat.getColor(context, R.color.colorPrimary);
            int startColor4 = ContextCompat.getColor(context, R.color.colorPrimary);
            int startColor5 = ContextCompat.getColor(context, R.color.colorPrimary);
            int endColor1 = ContextCompat.getColor(context, R.color.colorPrimary);
            int endColor2 = ContextCompat.getColor(context, R.color.colorPrimary);
            int endColor3 = ContextCompat.getColor(context, R.color.colorPrimary);
            int endColor4 = ContextCompat.getColor(context, R.color.colorPrimary);
            int endColor5 = ContextCompat.getColor(context, R.color.colorPrimary);

            List<Integer> colors = new ArrayList<>();
            colors.add(startColor1);
            colors.add(startColor2);
            colors.add(startColor3);
            colors.add(startColor4);
            colors.add(startColor5);
            colors.add(endColor1);
            colors.add(endColor2);
            colors.add(endColor3);
            colors.add(endColor4);
            colors.add(endColor5);


            set1.setColors(colors);


            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);


            mChart.setData(data);
        }
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {

    }

}
