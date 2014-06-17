package com.baihuogou.systemlog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import com.baihuogou.systemlog.utils.Db;


public class SystemLogInMain {
	
	private static final char[] ps_chars={'-','[',']','"',' '};
	
	public static void nginx_log_in_mysql(final String Path,final String  FileName) throws IOException, ClassNotFoundException, SQLException{
		 File file = new File(Path);
         BufferedReader br = new BufferedReader(new FileReader(file));
         String s = null;
         while((s = br.readLine())!=null){
        	 String oldStr=s;
        	 String remote_addr=null;
        	 String remote_user=null;
        	 String time_local=null;
        	 String request=null;
        	 String status=null;
        	 String body_bytes_set=null;
        	 String http_referer=null;
        	 String http_user_agent=null;
        	 String http_x_forwarded_for=null;
        	 String host=null;
        	int spindex= oldStr.indexOf(ps_chars[0]);
        	remote_addr=oldStr.substring(0,spindex);
        	String lastStr=oldStr.substring(spindex+1);
        	remote_user=lastStr.substring(0,lastStr.indexOf(ps_chars[1]));
        	lastStr=lastStr.substring(lastStr.indexOf(ps_chars[1]));
        	time_local=lastStr.substring(1,lastStr.indexOf(ps_chars[2]));
        	lastStr=lastStr.substring(lastStr.indexOf(ps_chars[3])+1);
        	request=lastStr.substring(0,lastStr.indexOf(ps_chars[3]));
        	lastStr=lastStr.substring(lastStr.indexOf(ps_chars[3])+1).trim();
        	status=lastStr.substring(0,lastStr.indexOf(ps_chars[4]));
        	lastStr=lastStr.substring(lastStr.indexOf(ps_chars[4])).trim();
        	body_bytes_set=lastStr.substring(0,lastStr.indexOf(ps_chars[4]));
        	lastStr=lastStr.substring(lastStr.indexOf(ps_chars[4])).trim();
        	String[] arr=lastStr.split("\" \"");
        	http_referer=arr[0].replace("\"", "").trim();
        	http_user_agent=arr[1].trim();
        	http_x_forwarded_for=arr.length>=3?arr[2].trim():"";
        	host=arr.length>=4?(arr[3].trim().replace("\"", "")):"";
        	
        	 Object[] object=new Object[]{
            	  remote_addr,
            	  remote_user,
            	  time_local,
            	  request,
            	  status,
            	  body_bytes_set,
            	  http_referer,
            	  http_user_agent,
            	  http_x_forwarded_for,
            	  host,
            	  oldStr
        	 };	
             Db.executeUpdate(String.format("insert into system_nginx_log_%s (remote_addr,remote_user,time_local,request,status,body_bytes_set,http_referer,http_user_agent,http_x_forwarded_for,host,old_str) values(?,?,?,?,?,?,?,?,?,?,?)",FileName), object);       
         }
         br.close();
	}
	
	public static void create_nginx_table(String FileName) throws ClassNotFoundException, SQLException{
		Db.executeUpdate(String.format(Db.DDL_NGINX_LOG_CREATE_SQL, FileName));
	}

	public static void nginx_log_process(String Time_Str){
		
	}
	public static void nginx_job(String Path,String FileName) throws ClassNotFoundException, SQLException, IOException{
		//String FileName="20140601";//FileUtil.loadFileName();
		create_nginx_table(FileName);
		nginx_log_in_mysql(String.format(Path, FileName.subSequence(0, 6),FileName),FileName);
		nginx_log_process(FileName);
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		nginx_job("C:\\Users\\Barry\\Downloads\\%s\\access_%s.log","20140601");
		//System.out.println(String.format("%s|%s", args[0],args[1]));
		//nginx_job(args[0],args[1]);
	}

}
