package cn.kk20.lib.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 日期格式装换工具类
 */
public class ITimeUtil {

	public static String time2String(long time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(time);
	}

	public static String time2String(long time, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(time);
	}

	public static long string2Time(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = null;
		try {
			date = dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

	public static long string2Time(String time,String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		java.util.Date date = null;
		try {
			date = dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

	public static String getFormatTime(String format){
		long time = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(time);
	}

	public static String getDateAndWeek(){
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if("1".equals(mWay)){
			mWay ="天";
		}else if("2".equals(mWay)){
			mWay ="一";
		}else if("3".equals(mWay)){
			mWay ="二";
		}else if("4".equals(mWay)){
			mWay ="三";
		}else if("5".equals(mWay)){
			mWay ="四";
		}else if("6".equals(mWay)){
			mWay ="五";
		}else if("7".equals(mWay)){
			mWay ="六";
		}
		return mYear + "年" + mMonth + "月" + mDay+"日"+"     星期"+mWay;
	}

	public static String getDayOfWeek(){
		Calendar calendar = Calendar.getInstance();
		int index = calendar.get(Calendar.DAY_OF_WEEK);
		String dayOfWeek = "";
		switch (index) {
			case 1:
				dayOfWeek = "星期日";
				break;
			case 2:
				dayOfWeek = "星期一";
				break;
			case 3:
				dayOfWeek = "星期二";
				break;
			case 4:
				dayOfWeek = "星期三";
				break;
			case 5:
				dayOfWeek = "星期四";
				break;
			case 6:
				dayOfWeek = "星期五";
				break;
			default:
				dayOfWeek = "星期六";
				break;
		}
		return dayOfWeek;
	}

}
