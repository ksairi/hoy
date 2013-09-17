package com.hoy.utilities;

import com.hoy.constants.MilongaHoyConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Se generalizan las funcionalidades utilizadas tanto en el cliente, como en el servidor.
 *
 * @author MKsairi, LDicesaro
 */
public class DateUtils {

    public static final String YYYY_MM_DD_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DATE_AND_TIME_PATTERN = "dd/MM/yyyy HH:mm";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final String FULL_NAME_OF_DAY_PATTERN = "EEEE";
    public static final String SHORT_NAME_OF_DAY_PATTERN = "EE";
    public static final String SHORT_NAME_OF_MONTH_PATTERN = "MMM";

    public static String getDateStringFromDate(Date date) {
        DateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_PATTERN);
        String format = formatter.format(date);
        return format;
    }

    public static String getTimeStringFromDate(Date date) {
        DateFormat formatter = new SimpleDateFormat(TIME_PATTERN);
        String format = formatter.format(date);
        return format;
    }

    public static String getDateAndTimeStringFromDate(Date date) {
        DateFormat formatter = new SimpleDateFormat(DATE_AND_TIME_PATTERN);
        String format = formatter.format(date);
        return format;
    }

    public static Date getDateFromString(String string) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
        Date date = formatter.parse(string);
        return date;
    }

    public static Date getTimeFromString(String string) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(TIME_PATTERN);
        Date date = formatter.parse(string);
        return date;
    }

    public static Date getDateAndTimeFromString(String string) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(DATE_AND_TIME_PATTERN);
        Date date = formatter.parse(string);
        return date;
    }

    /**
     * Se obtiene la fecha de hoy.
     *
     * @return
     */
    public static Date getToday() {
        return Calendar.getInstance().getTime();
    }

    /**
     * Se obtiene la fecha de hoy en formato String.
     *
     * @return
     */

    public static String getTodayString() {
        return DateUtils.getDateStringFromDate(Calendar.getInstance().getTime());
    }

    public static String getTodayAndTimeString() {
        return DateUtils.getDateAndTimeStringFromDate(Calendar.getInstance().getTime());
    }

    /**
     * Se obtiene la fecha de hoy, con los datos horarios inicializados a cero.
     *
     * @return
     */
    public static Calendar getTodayCleanTime() {
        return getCleanTime(Calendar.getInstance());
    }

    /**
     * Se obtiene la fecha pasada por parametro, con los datos horarios
     * inicializados a cero.
     *
     * @param date
     */
    public static Date getCleanTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getCleanTime(calendar).getTime();
    }

    /**
     * Se obtiene la fecha pasada por parametro, con los datos horarios
     * inicializados a cero.
     *
     * @param calendar
     */
    public static Calendar getCleanTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

	/*public static String changeDateFormat(String string){

		String result = null;
		try{
			DateFormat formatter = new SimpleDateFormat(YYYY_MM_DD_PATTERN);
			Date aux  = formatter.parse(string);
			formatter = new SimpleDateFormat(DD_MM_PATTERN);
			result = formatter.format(aux);

		}catch (ParseException e){
			return result;

		}

		return result;
	}*/

    public static String getTimeFromTimeString(String time) {

        if (time != null && time.length() == MilongaHoyConstants.TIME_WITH_SECONDS_LENGTH) {
            return time.substring(0, time.length() - 3);
        }

        return time;
    }

    public static String getDateToShow(String dateString) {

        String result = null;
        try {
            DateFormat nameDayFormatter = new SimpleDateFormat(SHORT_NAME_OF_DAY_PATTERN);
            DateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_PATTERN);
            DateFormat nameMonthFormat = new SimpleDateFormat(SHORT_NAME_OF_MONTH_PATTERN);

            Date date = dateFormat.parse(dateString);
            Calendar dateToShow = Calendar.getInstance();
            dateToShow.setTime(date);
            String monthName = nameMonthFormat.format(date);

            result = nameDayFormatter.format(date);
            result = result.concat(" ").concat(String.valueOf(dateToShow.get(Calendar.DAY_OF_MONTH))).concat(" ").concat(monthName);

        } catch (ParseException e) {
            return result;

        }

        return result;
    }

    public static String getTodayDateToShow() {

        DateFormat nameDayFormatter = new SimpleDateFormat(SHORT_NAME_OF_DAY_PATTERN);
        DateFormat nameMonthFormat = new SimpleDateFormat(SHORT_NAME_OF_MONTH_PATTERN);

        Calendar dateToShow = Calendar.getInstance();
        Date date = dateToShow.getTime();
        String monthName = nameMonthFormat.format(dateToShow.getTime());

        String result = nameDayFormatter.format(date);
        result = result.concat(" ").concat(String.valueOf(dateToShow.get(Calendar.DAY_OF_MONTH))).concat(" ").concat(monthName);

        return result;

    }
}
