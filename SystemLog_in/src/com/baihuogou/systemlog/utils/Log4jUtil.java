package com.baihuogou.systemlog.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jUtil {
	public static Logger getLogger(Class<?> clazz){
		PropertyConfigurator.configure(Log4jUtil.class.getResource("log4j.properties"));
		return Logger.getLogger(clazz);
	}
}
