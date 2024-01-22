package com.logora.logora_sdk.utils;



import android.content.Context;
import android.content.res.Resources;

import com.logora.logora_sdk.R;

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

    public static String getTimeAgo(Context context, Date startDate) {
        Resources res = context.getResources();
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

        String output = res.getString(R.string.date);
        if (elapsedDays > 0) {
            return output+" "+ elapsedDays+ " " +res.getString(R.string.jours);
        } else if (elapsedHours > 0) {
            return output+" "+ elapsedHours+" "+res.getString(R.string.heures);
        } else if (elapsedMinutes > 0) {
            return output+" "+ elapsedMinutes+" "+res.getString(R.string.minutes);
        } else if (elapsedSeconds > 0) {
            return output+" "+ elapsedSeconds+" "+res.getString(R.string.secondes);
        }
        return output;

    }
}
