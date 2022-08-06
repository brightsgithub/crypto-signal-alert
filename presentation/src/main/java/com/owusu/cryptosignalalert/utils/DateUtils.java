package com.owusu.cryptosignalalert.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bright.owusu-amankwa on 05/09/2017.
 */

public class DateUtils {

    // http://www.sdfonlinetester.info/# (always use lowercase yy)
    private static final String LOG_TAG = DateUtils.class.getSimpleName();
    private SimpleDateFormat calendarTimeFormat;
    private SimpleDateFormat calendarDateFormatXAxis24Hrs;
    private SimpleDateFormat calendarDateFormatXAxis7Day;
    private SimpleDateFormat calendarDateFormatXAxisMonth;
    private SimpleDateFormat calendarDateFormatXAxis3Month;
    private SimpleDateFormat calendarDateFormatXAxisPastYear;

    public SimpleDateFormat getCalendarDateFormatXAxis24Hrs() {
        return calendarDateFormatXAxis24Hrs = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    public SimpleDateFormat getCalendarDateFormatXAxis7Day() {
        return calendarDateFormatXAxis7Day = new SimpleDateFormat("EEE, d", Locale.getDefault());
    }

    public SimpleDateFormat getCalendarDateFormatXAxisMonth() {
        return calendarDateFormatXAxisMonth = new SimpleDateFormat("MMM d", Locale.getDefault());
    }

    public SimpleDateFormat getCalendarDateFormatXAxis3Month() {
        return calendarDateFormatXAxis3Month = new SimpleDateFormat("MMM", Locale.getDefault());
    }

    public SimpleDateFormat getCalendarDateFormatXAxisPastYear() {
        return calendarDateFormatXAxisPastYear = new SimpleDateFormat("MM/yy", Locale.getDefault());
    }

    private static DateUtils SINGLE_INSTANCE;

    public static DateUtils getInstance() {
        if(SINGLE_INSTANCE == null) {
            SINGLE_INSTANCE = new DateUtils();
        }
        return SINGLE_INSTANCE;
    }

    private DateUtils(){}

    public long getTimeNow(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }


    public boolean isChosenDateAfterDefaultDate(final Date defaultDate, final Date chosenDate) {
        final Calendar defaultDateCal = Calendar.getInstance();
        defaultDateCal.setTime(defaultDate);
        final Calendar chosenDateCal = Calendar.getInstance();
        chosenDateCal.setTime(chosenDate);
        return isChosenDateAfterDefaultDate(defaultDateCal, chosenDateCal);
    }

    public boolean isChosenDateAfterDefaultDate(final Calendar defaultDate, final Calendar chosenDate) {
        // test your condition
        if (chosenDate.before(defaultDate)) {
            // Date is in the past
            return false;
        } else if (chosenDate.after(defaultDate)) {
            // date is future date
            return true;
        } else {
            // date is today. can be the same or after
            return true;
        }
    }

    public Calendar getOneHourAheadOfToday() {
        Calendar defaultTimeOneHourAhead = Calendar.getInstance();
        defaultTimeOneHourAhead.add(Calendar.HOUR_OF_DAY, 1);  // add an hour
        return defaultTimeOneHourAhead;
    }

    public SimpleDateFormat getCalendarDateFormat() {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        return calendarDateFormat;
    }

    public String convertDateToFormattedStringWithTime(final long date) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        SimpleDateFormat calendarDateFormatWithTime = new SimpleDateFormat("EEE MMM d (HH:mm)", Locale.getDefault());
        final String formattedDate = calendarDateFormatWithTime.format(c.getTime());
        return formattedDate;
    }

    public String convertDateToFormattedString(final long date) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        final String formattedDate = calendarDateFormat.format(c.getTime());
        return formattedDate;
    }

    public String applyChosenFormatter(final long date, final SimpleDateFormat format) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        final String formattedDate = format.format(c.getTime());
        return formattedDate;
    }

    public SimpleDateFormat getCalendarTimeFormat() {
        return calendarTimeFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
    }

    public String getTrimmedFormat(final String text)
    {
        String[] splits  = text.split(":");

        if(splits.length == 3) {
            if("00".equals(splits[0])){
                return text.substring(3);
            }
        }

        return text;
    }

    public long convertToUnixTimeStamp(final long timeStamp) {
        // https://stackoverflow.com/questions/2420288/milliseconds-to-unix-timestamp?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        long unixTime = timeStamp / 1000L;
        return unixTime;
    }
}
