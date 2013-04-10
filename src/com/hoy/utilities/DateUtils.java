package com.hoy.utilities;

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

	public static final String DATE_PATTERN = "dd/MM/yyyy";
	public static final String TIME_PATTERN = "HH:mm";
	public static final String DATE_AND_TIME_PATTERN = "dd/MM/yyyy HH:mm";

	public static String getDateStringFromDate(Date date) {
		DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
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
}
