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

import com.baihuogou.systemlog.job.NginxLogInsertJob;

public class SystemLogInMain {
	

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			JobDetail job = new JobDetailImpl("NginxLog","Diviner",NginxLogInsertJob.class);
			CronTrigger trigger = new CronTriggerImpl("NginxLog","Diviner","0 0 3 * * ?");
			sched.scheduleJob(job, trigger);
			sched.start();
		} catch (SchedulerException | ParseException e) {
			e.printStackTrace();
		}
		/*try {
			String LogFilePath;
			System.out.println("nginx_job=" + FileUtil.loadFileName());
			LogFilePath = Db.ExecuteQuery("select param_value from m_parameter where param_name='log_file_path'", null, null).get(0).get("param_value").toString();
			NginxLogJob.nginx_job(LogFilePath, FileUtil.loadFileName());
			//更新数据库时间
			FileUtil.UpdateFileName();
		} catch (ClassNotFoundException | SQLException | ParseException | IOException | InterruptedException e) {
			e.printStackTrace();
		} */
	}
	
}
