package com.baihuogou.systemlog.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileUtil {
	//根据昨天的时间查询文件
	
	public static String loadFile(){
		//loadFileName();
		String file_path=String.format("access_%s.log", loadFileName());
		System.out.println(file_path);
		return null;
	}
	
	public static String loadFileName(){
		Calendar  cal=Calendar.getInstance();
		cal=getBeforeDay(cal);
		Date date=cal.getTime();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}
	private static Calendar getBeforeDay(Calendar cl){  
	        int day = cl.get(Calendar.DATE);  
	        cl.set(Calendar.DATE, day-1);  
	        return cl;  
	    }  
	

}
