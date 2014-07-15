package com.baihuogou.systemlog.job;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.baihuogou.systemlog.NginxLogJob;
import com.baihuogou.systemlog.utils.Db;
import com.baihuogou.systemlog.utils.FileUtil;

public class NginxLogInsertJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			String LogFilePath;
			System.out.println("nginx_job=" + FileUtil.loadFileName());
			LogFilePath = Db.ExecuteQuery("select param_value from m_parameter where param_name='log_file_path'", null, null).get(0).get("param_value").toString();
			NginxLogJob.nginx_job(LogFilePath, FileUtil.loadFileName());
			//更新数据库时间
			FileUtil.UpdateFileName();
		} catch (ClassNotFoundException | SQLException | ParseException | IOException | InterruptedException e) {
			e.printStackTrace();
		} 
	}

}
