package com.logora.logora_sdk.utils;

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

    public static String getTimeAgo(Date startDate) {

        long different = System.currentTimeMillis() - startDate.getTime();


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;


        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        different = different % secondsInMilli;

        String output = "il y a ";
        if (elapsedDays > 0) {
            return output + elapsedDays + " jours";
        } else if (elapsedHours > 0) {
            return output + elapsedHours + " heures";
        } else if (elapsedMinutes > 0) {
            return output + elapsedMinutes + " minutes";
        } else if (elapsedSeconds > 0) {
            return output + elapsedSeconds + " secondes";
        }
        return output;

    }
}
