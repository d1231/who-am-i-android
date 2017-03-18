package com.whomi.utils;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    @NonNull
    public static Date getCalendarDate(long timeStamp) {


        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date date = new Date(timeStamp);

        Date dateWithZeroTime;
        try {
            dateWithZeroTime = formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            return new Date();
        }

        return dateWithZeroTime;

    }

}
