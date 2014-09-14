package net.templefox.misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateFormater {
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static Date parse(String date) {
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			return new Date(0);
		}
	}
	
	public static String format(Date date){
		try {
			return	simpleDateFormat.format(date);
		} catch (Exception e) {
			return "Unknown";
		}
	}
}
