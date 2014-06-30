package com.baihuogou.systemlog;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemLogInMain {
	
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		parames(args);
	}
	
	public static void parames(String[] args){
		String[]  paramArray=args;
		int i=args.length;
		if(i!=2){
			System.out.println(" params error!");
			System.out.println("---------------------------------------------------------------");
			System.out.println(" Nginx_Log_in   ");
			System.out.println(" Author:  Barry.W.Sun ");
			System.out.println(" Version:1.0 ");
			System.out.println(" Des:nginx的日志目录 \\your path\\yyyyMM\\access_yyyyMMdd.log ");
			System.out.println(" params: -StartDate=\"yyyy-MM-dd\"  -NginxLogPath=your_path ");
			System.out.println("---------------------------------------------------------------");
			return;
		}
		
		String startDate=null,nginxLogPath=null;
		for(int j=0;j<2;j++){
			String param=paramArray[j];
			if(param.startsWith("-StartDate=")){
				 startDate=param.substring("-StartDate=".length());
			}else if(param.startsWith("-NginxLogPath=")){
				nginxLogPath=param.substring("-NginxLogPath=".length());
			}
		}
		//验证路径是否存在
		File file=new File(nginxLogPath);
		if(nginxLogPath==null||nginxLogPath.equals("")||(!file.exists()&&!file.isDirectory())){
			System.out.println("-NginxLogPath= error or path is not found!");
			return;
		}
		//验证时间是否正确
		if(startDate==null||startDate.equals("")||isValidDate(startDate)){
			System.out.println("-StartDate= Malformed！");
		}
		nginxLogPath+="/%s/access_%s.log";
		System.out.println("nginxLogPath="+nginxLogPath);
		new TimerManager(nginxLogPath,startDate);
	}
	
	public static boolean isValidDate(String sDate) {
		String datePattern1 = "\\d{4}-\\d{2}-\\d{2}";
		StringBuilder datePattern2 =new StringBuilder();
		datePattern2.append("^((\\d{2}(([02468][048])|([13579][26]))");
		datePattern2.append("[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|");
		datePattern2.append("(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?");
		datePattern2.append("((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?(");
		datePattern2.append("(((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?");
		datePattern2.append("((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))");
		if ((sDate != null)) {
			Pattern pattern = Pattern.compile(datePattern1);
			Matcher match = pattern.matcher(sDate);
			if (match.matches()) {
				pattern = Pattern.compile(datePattern2.toString());
				match = pattern.matcher(sDate);
				return match.matches();
			} else {
				return false;
			}
		}
		return false;
	}
}
