package com.logora.logora_android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static java.util.Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            return new java.util.Date();
        }
    }

    public static String getDateText(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }
}