package com.baihuogou.systemlog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnection {
	private static final String FILE_PATH_NAME = "database.properties";

	public static Connection getConn(String computer)
			throws ClassNotFoundException, SQLException {
		PropertiesUtil pro = new PropertiesUtil();
		String DB_DRIVER = null;
		String DB_URL = null;
		String DB_USER = null;
		String DB_PASSWORD = null;
		try {
			InputStream in = pro.getClass().getResourceAsStream(FILE_PATH_NAME);
			Properties props = new Properties();
			props.load(in);
			in.close();
			DB_DRIVER = props.getProperty(computer + "_DB_DRIVER");
			DB_URL = props.getProperty(computer + "_DB_URL");
			DB_USER = props.getProperty(computer + "_DB_USER");
			DB_PASSWORD = props.getProperty(computer + "_DB_PASSWORD");
		} catch (IOException e) {
			System.out.println("not found database.properties");
			e.printStackTrace();
		}
		Class.forName(DB_DRIVER);
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}
}
