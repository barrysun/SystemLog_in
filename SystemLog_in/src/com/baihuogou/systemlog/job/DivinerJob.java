package com.baihuogou.systemlog.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.baihuogou.diviner.Result;

public class DivinerJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Result.action();
	}

}
