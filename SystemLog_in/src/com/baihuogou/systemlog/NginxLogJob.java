package com.baihuogou.systemlog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TimerTask;

import com.baihuogou.systemlog.mail.SendEmail;
import com.baihuogou.systemlog.utils.Db;
import com.baihuogou.systemlog.utils.FileUtil;

public class NginxLogJob extends TimerTask{

	private static final char[] ps_chars={'-','[',']','"',' '};
	private static final String _SHELL="rsync -avRz 192.168.1.228::nginxlog /usr/local/system_log/nginx_log/";
	
	private String LogFilePath;
	
	public NginxLogJob(String LogFilePath){
		this.LogFilePath=LogFilePath;
	}
	
	@Override
	public void run() {
		try {
			try {
				System.out.println("nginx_job="+FileUtil.loadFileName());
				nginx_job(LogFilePath,FileUtil.loadFileName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 1.同步rsync log文件
	 * 1.1验证是否存在log文件。
	 * 2.创建对呀的数据表
	 * 3.将log文件数据导入到DB中
	 * 4.解析日志
	 * 5.发送email文件通知订阅者
	 * 
	 * @param Path
	 * @param FileName
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public  void nginx_job(String Path,String FileName) throws ClassNotFoundException, SQLException, IOException, InterruptedException{
		//String FileName="20140601";//FileUtil.loadFileName();
		execute_shell();
		//System.out.println("shell....");
		create_nginx_table(FileName);
		nginx_log_in_mysql(String.format(Path, FileName.subSequence(0, 6),FileName),FileName);
		nginx_log_process(FileName);
		SendEmail.sendEmail();
		
	}
	
	
	public static void execute_shell() throws IOException, InterruptedException{
		Process pid=null;
		pid=Runtime.getRuntime().exec(_SHELL);
		if(pid!=null){
			pid.waitFor();
		}
		
	}

	
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
}
