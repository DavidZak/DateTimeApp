package mradmin.example.com.datetimeapp.util;

import android.app.Application;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LastSeen extends Application {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    private static final int WEEK_MILLIS = 7 * DAY_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else if (diff < 24 * HOUR_MILLIS * 7) {
            return diff / DAY_MILLIS + " days ago";
        } else if (diff < 24 * HOUR_MILLIS * 7 * 2) {
            return diff / WEEK_MILLIS + " week ago";
        } else {
            //return diff / WEEK_MILLIS + " weeks ago";
            return getFullStringDate(time);
        }
    }

    public static String getFullStringDate(long dateTime) {
        if (dateTime < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            dateTime *= 1000;
        }

        long now = System.currentTimeMillis();
        if (dateTime > now || dateTime <= 0) {
            return "just now";
        }

        return new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(new Date(dateTime));
    }

    public static String getShortStringDate(long dateTime) {
        return new SimpleDateFormat("EEE, d MMM yyyy").format(new Date(dateTime));
    }

    public static long getFormattedDate(String strDate) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        Date date = null;

        long startDate = 0;

        try {

            date = formatter.parse(strDate);
            startDate = date.getTime();

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return startDate;
    }
}
