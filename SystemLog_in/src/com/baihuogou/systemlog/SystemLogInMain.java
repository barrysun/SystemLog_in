package com.baihuogou.systemlog;

import java.io.IOException;
import java.sql.SQLException;

public class SystemLogInMain {
	
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		//new NginxLogJob().nginx_job("C:\\Users\\Barry\\Downloads\\%s\\access_%s.log","20140601");
		//System.out.println(String.format("%s|%s", args[0],args[1]));
		//new TimerManager("C:\\Users\\Barry\\Downloads\\%s\\access_%s.log");
		new TimerManager(args[0]);
		//nginx_job(args[0],args[1]);
	}

}
