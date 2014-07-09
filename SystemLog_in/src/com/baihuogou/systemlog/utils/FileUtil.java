package com.baihuogou.systemlog.utils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FileUtil {
	//根据昨天的时间查询文件
	private static final String QUERY_SQL="select param_value from m_parameter where param_name='exe_time'";
	private static String UPDATE_SQL="update m_parameter set param_value=? where param_name='exe_time'";
	
	public static String loadFile() throws ClassNotFoundException, SQLException, ParseException{
		//loadFileName();
		String file_path=String.format("access_%s.log", loadFileName());
		System.out.println(file_path);
		return null;
	}
	
	/**
	 * 
	 * 1.查询数据库中的日期
	 * 2.更新到下个执行时间
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws ParseException 
	 */
	public static String loadFileName() throws ClassNotFoundException, SQLException, ParseException{
		String date_str=Db.ExecuteQuery(QUERY_SQL, null, null).get(0).get("param_value").toString().trim();
		return date_str;
	}
	
	public static void UpdateFileName() throws ClassNotFoundException, SQLException, ParseException{
		String date_str=Db.ExecuteQuery(QUERY_SQL, null, null).get(0).get("param_value").toString().trim();
	    Db.executeUpdate(UPDATE_SQL, new Object[]{Calendar2String(getAfterDay(String2Calendar(date_str)))}, null);
	}
	
	public static Calendar getBeforeDay(Calendar cl){  
	        int day = cl.get(Calendar.DATE);  
	        cl.set(Calendar.DATE, day-1);  
	        return cl;  
	}
	
	public static Calendar getAfterDay(Calendar cl){
		 int day = cl.get(Calendar.DATE);  
	        cl.set(Calendar.DATE, day+1);  
	        return cl; 
	}
	public  static Calendar String2Calendar(String date_str) throws ParseException{
		Calendar dayc1 = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date daystart = df.parse(date_str);    //start_date是类似"2013-02-02"的字符串
		dayc1.setTime(daystart); 
		return dayc1;
	}
	
   
	
	public static String Calendar2String(Calendar cal){
		SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");// 设置你想要的格式
		return df1.format(cal.getTime());
	}
	
	
}
