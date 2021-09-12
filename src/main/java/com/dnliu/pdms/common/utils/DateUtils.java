/**   
 * Copyright © 2018 Liufh. All rights reserved.
 * 
 * @Package: timetest 
 * @author: liufuhua
 * @date: 2018年10月24日 上午11:27:53 
 */
package com.dnliu.pdms.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** 
 * Description: 日期时间处理工具类
 * Company: 
 * @version 1.0
 * @author: liufuhua
 * @since: 2018年10月24日 上午11:27:53 
 *
 */
public class DateUtils {
	
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String HOUR_PATTERN = "yyyy-MM-dd HH";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String MONTH_PATTERN = "yyyy-MM";
    public static final String YEAR_PATTERN = "yyyy";
    public static final String MINUTE_ONLY_PATTERN = "mm";
    public static final String HOUR_ONLY_PATTERN = "HH";
    
	/**
	 * Description: 将时间yyyyMMddHHmmss转换为时间戳
	 * @author: liufuhua
	 * @since: 2018年10月24日 下午2:19:04 
	 * @param
	 *     s - 时间(20181024112552)
	 * @return 时间戳(1540351552000)
	 * @throws ParseException
	 */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
    
    /**
     * Description: 将时间戳转换为时间yyyyMMddHHmmss
     * @author: liufuhua
     * @since: 2018年10月24日 下午2:19:33 
     * @param
     *     s - 时间戳(1540351552000)
     * @return
     * @throws
     */
    public static String stampToDate(String s) {
        String res;
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    /**
     * Description: 获取当前时间yyyyMMddHHmmss
     * @author: liufuhua
     * @since: 2018年10月24日 下午2:19:33 
     * @param 
     * @return
     *     当前时间(20181024211127)
     * @throws
     */
    public static String getNowTime() {
    	long time = new Date().getTime();;
		String timeStr = stampToDate(String.valueOf(time));
		
		return timeStr;
    }
    
    /**
     * Description: 获取当前时间yyyy-MM-dd HH:mm:ss
     * @author: liufuhua
     * @since: 2018年10月24日 下午2:19:33 
     * @param 
     * @return
     *     当前时间(20181024211127)
     * @throws
     */
    public static String getNowFormatTime() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = df.format(new Date());

		return nowTime;
    }
    
    /**
     * 
     * Description: 获取当前日期(yyyyMMdd)
     * @author: liufuhua
     * @since: 2019年2月22日 下午2:23:48 
     * @param
     * @return
     * @throws
     */
    public static String getNowDate() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowTime = df.format(new Date());

		return nowTime;
    }
    
    /**
     * 
     * Description: 获取N天前日期yyyyMMdd
     * @author: liufuhua
     * @since: 2019年2月20日 下午5:33:06 
     * @param
     *     n - 多少天前的n
     * @return
     *     n天前日期(20190208)
     * @throws
     */
    public static String getPrivDate(int n) {
    	Date today = new Date();
    	//24小时 * 60分钟 * 60秒 * 1000毫秒，单位是L
    	long diff = n * 24 * 3600 * 1000L;
    	Date privDate = new Date(today.getTime() - diff);
    	
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String privDateStr = df.format(privDate);

		return privDateStr;
    }
    
    /**
     * 日期相加减天数
     * @param date 如果为Null，则为当前时间
     * @param days 加减天数
     * @param includeTime 是否包括时分秒,true表示包含
     * @return
     * @throws ParseException 
     */
    public static Date dateAdd(Date date, int days, boolean includeTime) throws ParseException{
        if(date == null){
            date = new Date();
        }
        if(!includeTime){
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN);
            date = sdf.parse(sdf.format(date));
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
    
    /**
     * 时间格式化成字符串
     * @param date Date
     * @param pattern StrUtils.DATE_TIME_PATTERN || StrUtils.DATE_PATTERN， 如果为空，则为yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static String dateFormat(Date date, String pattern) throws ParseException{
        if(StringUtil.isEmpty(pattern)){
            pattern = DateUtils.DATE_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    /**
     * 字符串解析成时间对象
     * @param dateTimeString String
     * @param pattern StrUtils.DATE_TIME_PATTERN || StrUtils.DATE_PATTERN，如果为空，则为yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static Date dateParse(String dateTimeString, String pattern) throws ParseException{
        if(StringUtil.isEmpty(pattern)){
            pattern = DateUtils.DATE_PATTERN;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateTimeString);
    }
    
    /**
     * 将日期时间格式成只有日期的字符串（可以直接使用dateFormat，Pattern为Null进行格式化）
     * @param dateTime Date
     * @return
     * @throws ParseException
     */
    public static String dateTimeToDateString(Date dateTime) throws ParseException{
        String dateTimeString = DateUtils.dateFormat(dateTime, DateUtils.DATE_TIME_PATTERN);  
        return dateTimeString.substring(0, 10); 
    }
    
    /**
     * 当时、分、秒为00:00:00时，将日期时间格式成只有日期的字符串，
     * 当时、分、秒不为00:00:00时，直接返回
     * @param dateTime Date
     * @return
     * @throws ParseException
     */
    public static String dateTimeToDateStringIfTimeEndZero(Date dateTime) throws ParseException{
        String dateTimeString = DateUtils.dateFormat(dateTime, DateUtils.DATE_TIME_PATTERN);
        if(dateTimeString.endsWith("00:00:00")){
            return dateTimeString.substring(0, 10);
        }else{
            return dateTimeString;
        }
    }
    
    /**
     * 将日期时间格式成日期对象，和dateParse互用
     * @param dateTime Date
     * @return Date
     * @throws ParseException
     */
    public static Date dateTimeToDate(Date dateTime) throws ParseException{
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /** 
     * 时间加减小时
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param hours 加减的小时 
     * @return Date 
     */  
    public static Date dateAddHours(Date startDate, int hours) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) + hours);  
        return c.getTime();  
    }
    
    /**
     * 时间加减分钟
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param minutes 加减的分钟
     * @return
     */
    public static Date dateAddMinutes(Date startDate, int minutes) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minutes);  
        return c.getTime();  
    }
    
    /**
     * 时间加减秒数
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param seconds 加减的秒数
     * @return
     */
    public static Date dateAddSeconds(Date startDate, int seconds) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + seconds);  
        return c.getTime();  
    }

    /** 
     * 时间加减天数 
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param days 加减的天数 
     * @return Date 
     */  
    public static Date dateAddDays(Date startDate, int days) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.DATE, c.get(Calendar.DATE) + days);  
        return c.getTime();  
    }
    
    /** 
     * 时间加减月数
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param months 加减的月数 
     * @return Date 
     */  
    public static Date dateAddMonths(Date startDate, int months) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);  
        return c.getTime();  
    }
    
    /** 
     * 时间加减年数
     * @param startDate 要处理的时间，Null则为当前时间 
     * @param years 加减的年数 
     * @return Date 
     */  
    public static Date dateAddYears(Date startDate, int years) {  
        if (startDate == null) {  
            startDate = new Date();  
        }  
        Calendar c = Calendar.getInstance();  
        c.setTime(startDate);  
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + years);  
        return c.getTime();  
    }  
    
    /** 
     * 时间比较（如果myDate>compareDate返回1，<返回-1，相等返回0） 
     * @param myDate 时间 
     * @param compareDate 要比较的时间 
     * @return int 
     */  
    public static int dateCompare(Date myDate, Date compareDate) {  
        Calendar myCal = Calendar.getInstance();  
        Calendar compareCal = Calendar.getInstance();  
        myCal.setTime(myDate);  
        compareCal.setTime(compareDate);  
        return myCal.compareTo(compareCal);  
    }
    
    /**
     * 获取两个时间中最小的一个时间
     * @param date
     * @param compareDate
     * @return
     */
    public static Date dateMin(Date date, Date compareDate) {
        if(date == null){
            return compareDate;
        }
        if(compareDate == null){
            return date;
        }
        if(1 == dateCompare(date, compareDate)){
            return compareDate;
        }else if(-1 == dateCompare(date, compareDate)){
            return date;
        }
        return date;  
    }
    
    /**
     * 获取两个时间中最大的一个时间
     * @param date
     * @param compareDate
     * @return
     */
    public static Date dateMax(Date date, Date compareDate) {
        if(date == null){
            return compareDate;
        }
        if(compareDate == null){
            return date;
        }
        if(1 == dateCompare(date, compareDate)){
            return date;
        }else if(-1 == dateCompare(date, compareDate)){
            return compareDate;
        }
        return date;  
    }
    
    /**
     * 获取两个日期（不含时分秒）相差的天数，不包含今天
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException 
     */
    public static int dateBetween(Date startDate, Date endDate) throws ParseException {
        Date dateStart = dateParse(dateFormat(startDate, DATE_PATTERN), DATE_PATTERN);
        Date dateEnd = dateParse(dateFormat(endDate, DATE_PATTERN), DATE_PATTERN);
        return (int) ((dateEnd.getTime() - dateStart.getTime())/1000/60/60/24); 
    }
    
    /**
     * 获取两个日期（不含时分秒）相差的天数，包含今天
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException 
     */
    public static int dateBetweenIncludeToday(Date startDate, Date endDate) throws ParseException {  
        return dateBetween(startDate, endDate) + 1;
    }
    
    /**
     * 获取日期时间的年份，如2017-02-13，返回2017
     * @param date
     * @return
     */
    public static int getYear(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    /**
     * 获取日期时间的月份，如2017年2月13日，返回2
     * @param date
     * @return
     */
    public static int getMonth(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }
    
    /**
     * 获取日期时间的第几天（即返回日期的dd），如2017-02-13，返回13
     * @param date
     * @return
     */
    public static int getDate(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }
    
    /**
     * 获取日期时间当月的总天数，如2017-02-13，返回28
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DATE);
    }
    
    /**
     * 获取日期时间当年的总天数，如2017-02-13，返回2017年的总天数
     * @param date
     * @return
     */
    public static int getDaysOfYear(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * 根据时间获取当月最大的日期
     * <li>2017-02-13，返回2017-02-28</li>
     * <li>2016-02-13，返回2016-02-29</li>
     * <li>2016-01-11，返回2016-01-31</li>
     * @param date Date
     * @return
     * @throws Exception 
     */
    public static Date maxDateOfMonth(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        int value = cal.getActualMaximum(Calendar.DATE);
        return dateParse(dateFormat(date, MONTH_PATTERN) + "-" + value, null);
    }
    
    /**
     * 根据时间获取当月最小的日期，也就是返回当月的1号日期对象
     * @param date Date
     * @return
     * @throws Exception 
     */
    public static Date minDateOfMonth(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);
        int value = cal.getActualMinimum(Calendar.DATE);
        return dateParse(dateFormat(date, MONTH_PATTERN) + "-" + value, null);
    }
    
    /**
     * @desc: 字符串时间格式化
     * @author: liufuhua
     * @since: 2019年3月27日 下午5:11:05 
     * @param
     *     dateTime - yyyyMMddHHmmss
     *     pattern - 格式化的格式类型
     * @return
     * @throws
     */
    public static String dateTimeFormat(String dateTime, String pattern) {
    	String formatTime = "";
    	
    	String year = dateTime.substring(0, 4);
    	String month = dateTime.substring(4, 6);
    	String date = dateTime.substring(6, 8);
    	String hour = dateTime.substring(8, 10);
    	String minute = dateTime.substring(10, 12);
    	String second = dateTime.substring(12, 14);
    	
    	if (pattern.equals(DATE_TIME_PATTERN)) {
    		formatTime = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
    	} else if (pattern.equals(MINUTE_PATTERN)) {
    		formatTime = year + "-" + month + "-" + date + " " + hour + ":" + minute;
    	} else if (pattern.equals(HOUR_PATTERN)) {
    		formatTime = year + "-" + month + "-" + date + " " + hour;
    	} else if (pattern.equals(DATE_PATTERN)) {
    		formatTime = year + "-" + month + "-" + date;
    	} else if (pattern.equals(MONTH_PATTERN)) {
    		formatTime = year + "-" + month;
    	} else if (pattern.equals(YEAR_PATTERN)) {
    		formatTime = year;
    	} else if (pattern.equals(MINUTE_ONLY_PATTERN)) {
    		formatTime = month;
    	} else if (pattern.equals(HOUR_ONLY_PATTERN)) {
    		formatTime = hour;
    	}
    	
    	return formatTime;
    }
    
    /**
     * 将java.sql.Timestamp对象转化为String字符串
     * @param time
     *            要格式的java.sql.Timestamp对象
     * @param strFormat
     *            输出的String字符串格式的限定（如："yyyy-MM-dd HH:mm:ss"）
     * @return 表示日期的字符串
     */
    public static String dateToStr(java.sql.Timestamp time, String strFormat) {
        DateFormat df = new SimpleDateFormat(strFormat);
        String str = df.format(time);
        return str;
    }
    
    public static void main(String[] args) {
    	String date = dateTimeFormat("20190327170752", DATE_TIME_PATTERN);
    	
    	System.out.println(date);
    }
}
