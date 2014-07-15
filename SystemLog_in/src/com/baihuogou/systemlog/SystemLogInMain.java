package com.baihuogou.systemlog;

import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import com.baihuogou.systemlog.job.DivinerJob;
import com.baihuogou.systemlog.job.NginxLogInsertJob;

public class SystemLogInMain {
	

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			JobDetail job = new JobDetailImpl("NginxLog","Diviner",NginxLogInsertJob.class);
			//JobDetail job2 = new JobDetailImpl("DivinerJob","Diviner",DivinerJob.class);
			CronTrigger trigger = new CronTriggerImpl("NginxLog","Diviner","0 0 3 * * ?");
			//CronTrigger trigger2 = new CronTriggerImpl("DivinerJob","Diviner","0 30 8 * * ?");
			sched.scheduleJob(job, trigger);
			//sched.scheduleJob(job2,trigger2);
			sched.start();
		} catch (SchedulerException | ParseException e) {
			e.printStackTrace();
		}
	}
	
}
