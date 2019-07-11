package com.jmindel.fbuparstagram;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            Date date = sf.parse(rawJsonDate);
            relativeDate = getRelativeTimeAgo(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate.toUpperCase();
    }

    public static String getRelativeTimeAgo(Date rawDate) {
        String relativeDate = "";
        long dateMillis = rawDate.getTime();

        long now = (new Date()).getTime();
        long between = now - dateMillis;
        int hoursBetween = (int) ((between / (1000 * 60 * 60)));
        int yearsBetween = (new Date(now)).getYear() - (new Date(dateMillis)).getYear();

        if (yearsBetween >= 1) {
            SimpleDateFormat moreThanAYear = new SimpleDateFormat("MMM d yyyy", Locale.US);
            relativeDate = moreThanAYear.format(dateMillis);
        } else if (hoursBetween >= 24) {
            SimpleDateFormat moreThanADay = new SimpleDateFormat("MMM d", Locale.US);
            relativeDate = moreThanADay.format(dateMillis);
        } else {
            String rawRelativeDate = DateUtils.getRelativeTimeSpanString(dateMillis, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            String[] parts = rawRelativeDate.split(" ");
            relativeDate = parts[0];
            switch (parts[1]) {
                case "second":
                case "seconds":
                    relativeDate += "s";
                    break;
                case "minute":
                case "minutes":
                    relativeDate += "m";
                    break;
                case "hour":
                case "hours":
                    relativeDate += "h";
                    break;
                case "day":
                case "days":
                    relativeDate += "d";
                    break;
                case "month":
                case "months":
                    relativeDate += "mo";
                    break;
                case "year":
                case "years":
                    relativeDate += "y";
                    break;
                default:
                    relativeDate = rawRelativeDate;
                    break;
            }
        }

        return relativeDate.toUpperCase();
    }

    public static String ellipsize(String str, int maxChars) {
        if (str.length() > maxChars) {
            String ellipsized = str.substring(0, maxChars - 3);
            ellipsized += "...";
            return ellipsized;
        } else {
            return str;
        }
    }
}
