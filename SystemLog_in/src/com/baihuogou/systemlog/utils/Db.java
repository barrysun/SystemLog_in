package com.baihuogou.systemlog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class Db {
	
	private static final String FILE_PATH_NAME = "database.properties";
	private static  String DB_DRIVER = null;
	private static  String DB_URL = null;
	private static  String DB_USER = null;
	private static  String DB_PASSWORD = null;
	public static String DDL_NGINX_LOG_CREATE_SQL=null;
	
	static{
		StringBuilder DDL_NGINX_LOG=new StringBuilder(); 
		PropertiesUtilTest pro=new PropertiesUtilTest();
		 try {   
	            InputStream in = pro.getClass().getResourceAsStream(FILE_PATH_NAME);   
	            Properties props = new Properties();   
	            props.load(in);   
	            in.close();  
	            DB_DRIVER=props.getProperty("DB_DRIVER");
	            DB_URL= props.getProperty("DB_URL"); 
	            DB_USER=props.getProperty("DB_USER"); 
	            DB_PASSWORD=props.getProperty("DB_PASSWORD"); 
	        } catch (IOException e) { 
	        	 System.out.println("not found database.properties");
	            e.printStackTrace();
	        } 
		 
		 DDL_NGINX_LOG.append(" CREATE TABLE system_nginx_log_%s(");
		 DDL_NGINX_LOG.append("		 id   SERIAL PRIMARY KEY,");
		 DDL_NGINX_LOG.append("		 old_str  varchar(10000) ,");
		 DDL_NGINX_LOG.append("		 remote_addr  varchar(16) ,");
		 DDL_NGINX_LOG.append("		 remote_user  varchar(100),");
		 DDL_NGINX_LOG.append("		 time_local  varchar(100) ,");
		 DDL_NGINX_LOG.append("		 request  varchar(20000),");
		 DDL_NGINX_LOG.append("		 status  varchar(1000)  ,");
		 DDL_NGINX_LOG.append("		 body_bytes_set  varchar(1000)  ,");
		 DDL_NGINX_LOG.append("	 http_referer  varchar(10000) ,");
		 DDL_NGINX_LOG.append(" http_user_agent  varchar(10000)  ,");
		 DDL_NGINX_LOG.append("	 http_x_forwarded_for  varchar(10000) ,");
		 DDL_NGINX_LOG.append("	 host  varchar(200) ");
		 DDL_NGINX_LOG.append(");");
		
	/*	DDL_NGINX_LOG.append("CREATE TABLE `system_nginx_log_%s` (");
		DDL_NGINX_LOG.append("`id`  int(11) NOT NULL AUTO_INCREMENT ,");
		DDL_NGINX_LOG.append("`old_str`  varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`remote_addr`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`remote_user`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`time_local`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`request`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`status`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`body_bytes_set`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`http_referer`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`http_user_agent`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`http_x_forwarded_for`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("`host`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,");
		DDL_NGINX_LOG.append("PRIMARY KEY (`id`)");
		DDL_NGINX_LOG.append(")");*/
		DDL_NGINX_LOG_CREATE_SQL=DDL_NGINX_LOG.toString();
	}
	
	private static Connection getConn() throws ClassNotFoundException,
			SQLException {
		Class.forName(DB_DRIVER);
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

	public static int executeUpdate(final String sql) throws SQLException,
			ClassNotFoundException {
		int result = -1;
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = getConn();
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			close(conn, stmt, null);
		}
		return result;
	}

	public static int executeUpdate( String sql,Object[] params) throws SQLException,
			ClassNotFoundException {
		int result = -1;
		PreparedStatement stmt = null;
		Connection conn = null;
		try {
			conn = getConn();
			stmt = conn.prepareStatement(sql);
			if (params != null) {
				int i = 0;
				for (Object obj : params) {
					stmt.setObject(i + 1, obj);
					i++;
				}
			}
			result = stmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			close(conn, stmt, null);
		}
		return result;
	}

	public static List<HashMap<String, Object>> ExecuteQuery( String sql,
			Object[] params) throws ClassNotFoundException, SQLException {
		List<HashMap<String, Object>> datas = null;
		PreparedStatement sta = null;
		ResultSet rs = null;
		Connection conn = getConn();
		try {
			sta = conn.prepareStatement(sql);
			if (params != null) {
				int i = 0;
				for (Object obj : params) {
					sta.setObject(i + 1, obj);
					i++;
				}
			}
			rs = sta.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int recount = rsmd.getColumnCount();
			String[] colLabels = new String[recount];
			for (int i = 0; i < recount; i++) {
				colLabels[i] = rsmd.getColumnLabel(i + 1);
			}
			datas = new ArrayList<HashMap<String, Object>>();
			while (rs.next()) {
				HashMap<String, Object> data = new HashMap<String, Object>();
				for (int i = 0; i < colLabels.length; i++) {
					data.put(colLabels[i], rs.getObject(colLabels[i]));
				}
				datas.add(data);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			close(conn, sta, rs);
		}
		return datas;
	}

	private static void close(Connection conn,  Statement stmt,
			 ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		
		}
	}
	

}
