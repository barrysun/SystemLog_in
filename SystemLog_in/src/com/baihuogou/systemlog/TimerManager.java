package com.baihuogou.systemlog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.baihuogou.systemlog.utils.Log4jUtil;


public class TimerManager {
	
	private static Logger logger = Log4jUtil.getLogger(TimerManager.class);
	//时间间隔
	private static final long PERIOD_DAY=24 * 60 * 60 * 1000;
	
	public TimerManager(String FilePath,String startDate){
	/*	Calendar calendar=Calendar.getInstance();
		//System.out.println(calendar.DATE);
		定制每日3:00执行方法
		calendar.set(Calendar.HOUR_OF_DAY, 3);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);*/
		//Date date=calendar.getTime();//第一次执行定时任务的时间
		//如果第一次执行定时任务的时间 小于 当前的时间
		SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        Date date=null;
		try {
			date = sdf.parse( String.format(" %s 03:00:00 ",startDate));
		} catch (ParseException e) {
			
			logger.error(e.getMessage());
		}
		//此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		/* if(date.before(new Date())){
			 date=this.addDay(date,1);
		 }*/
		// System.out.println(date.getDay()+"|"+date.getHours()+"|"+date.getMinutes());
		 Timer timer=new Timer();
		 NginxLogJob task = new NginxLogJob(FilePath);
		 //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		 timer.schedule(task,date,PERIOD_DAY);
		 //
		 
	}
	 //增加或减少天数
	 public Date addDay(Date date,int num){
		 Calendar startDT=Calendar.getInstance();
		 startDT.setTime(date);
		 startDT.add(Calendar.DAY_OF_MONTH, num);
		 return startDT.getTime();
	 }
}
