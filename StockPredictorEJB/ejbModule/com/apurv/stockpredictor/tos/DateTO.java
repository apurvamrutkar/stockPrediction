/**
 * 
 */
package com.apurv.stockpredictor.tos;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Apurv Amrutkar
 *
 */
public class DateTO {

	public int date;

	public int month;

	public int year;

	public int minute;

	public int second;

	public DateTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DateTO(int date, int month, int year, int minute, int second) {
		super();
		this.date = date;
		this.month = month;
		this.year = year;
		this.minute = minute;
		this.second = second;
	}

	public DateTO(Date d) {
		Calendar date = new GregorianCalendar();
		date.setTime(d);
		this.date = date.get(Calendar.DATE);
		this.month = date.get(Calendar.MONTH);
		this.year = date.get(Calendar.YEAR);
		this.minute = date.get(Calendar.MINUTE);
		this.second = date.get(Calendar.SECOND);
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

}
