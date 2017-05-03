package com.mymo.alexamino.intent.util;

import com.amazonaws.util.StringUtils;
import com.mymo.alexamino.annotation.Slot;
import com.mymo.alexamino.exception.IntentHandlingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mymo.alexamino.annotation.Slot.*;

/**
 * Created by Phil Madden on 2/18/17.
 */
public class DateUtil {

    public static Date parseUtteranceDate(String utteranceDate) {
        try {
            if (Slot.DateFilter.WEEK_OF_YEAR.matches(utteranceDate)) {
                return new SimpleDateFormat(AMAZON_WEEK_FORMAT).parse(utteranceDate.replaceAll("W", ""));
            } else if (Slot.DateFilter.WEEKEND.matches(utteranceDate)) {
                return getWeekendDate(utteranceDate);
            } else if (Slot.DateFilter.MONTH.matches(utteranceDate)) {
                return getDate(utteranceDate);
            } else if (Slot.DateFilter.DECADE.matches(utteranceDate)) {
                return null;
            } else if (Slot.DateFilter.YEAR.matches(utteranceDate)) {
                return new SimpleDateFormat(AMAZON_YEAR_FORMAT).parse(utteranceDate);
            } else if (Slot.DateFilter.SEASON.matches(utteranceDate)) {
                return null;
            } else if (Slot.DateFilter.NOW.matches(utteranceDate)) {
                return new Date();
            } else if (Slot.DateFilter.ANY.matches(utteranceDate)) {
                return getDate(utteranceDate);
            } else {
                throw new IntentHandlingException("Unrecognized utterance date format: " + utteranceDate);
            }
        } catch (ParseException e) {
            throw new IntentHandlingException("Unable to parse utterance date: " + utteranceDate);
        }
    }

    private static Date getWeekendDate(String utteranceDate) {
        Matcher matcher = Pattern.compile(Slot.DateFilter.WEEKEND.regex()).matcher(utteranceDate);
        if (matcher.find()) {
            String year = matcher.group(1);
            String week = matcher.group(2);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.YEAR, Integer.parseInt(year));
            calendar.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week));
            calendar.set(Calendar.DAY_OF_WEEK, 7);

            return calendar.getTime();
        }
        //TODO: Error
        return null;
    }


    ///ERRrrrrrr not working for months. returning 1970
    private static Date getDate(String utteranceDate) throws ParseException {
        String normalizedDate = utteranceDate.replace("'?s", "");
        Matcher matcher = Pattern.compile(Slot.DateFilter.DATE.regex()).matcher(normalizedDate);

        Date date = new Date();

        if (matcher.find()) {
            if (!StringUtils.isNullOrEmpty(matcher.group(1))) {
                date = new SimpleDateFormat(AMAZON_DATE_FORMAT).parse(normalizedDate);
            } else {
                try {
                    date = new SimpleDateFormat(SHOT_DAY_FORMAT).parse(matcher.group(2));
                } catch (ParseException e) {
                    date = new SimpleDateFormat(SHOT_MONTH_FORMAT).parse(matcher.group(2));
                }
            }
        }

        return date;
    }
}
