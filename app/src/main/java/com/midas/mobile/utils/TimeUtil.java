package com.midas.mobile.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String currentTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = simpleDateFormat.format(date);

        return formatDate;
    }
}
