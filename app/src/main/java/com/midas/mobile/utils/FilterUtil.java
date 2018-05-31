package com.midas.mobile.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Pattern;

/**
 * Created by hyerim on 2018. 5. 26....
 *
**/

public class FilterUtil implements InputFilter {
    public final static String FILTER_E_N_H = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$";
    public final static String FILTER_E_H = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$";

    private Context mContext;
    private Pattern mPattern;

    public FilterUtil(Context context, String str) {
        this.mContext = context;
        if (str.compareTo(FILTER_E_N_H) == 0) {
            mPattern = Pattern.compile(FILTER_E_N_H);
        } else if (str.compareTo(FILTER_E_H) == 0) {
            mPattern = Pattern.compile(FILTER_E_H);
        } else {
            mPattern = Pattern.compile(FILTER_E_N_H);
        }
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (source.equals("") || mPattern.matcher(source).matches()) {
            return source;
        }

        ToastUtil.makeShortToast(mContext, "입력할 수 없는 문자입니다");
        return "";
    }
}

