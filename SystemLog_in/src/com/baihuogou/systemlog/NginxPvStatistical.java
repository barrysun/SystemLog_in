package com.baihuogou.systemlog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.baihuogou.systemlog.utils.Db;


public class NginxPvStatistical {
	
	/**
	 * 生成统计
	 * @param time
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static String executeStatistical(String time) throws IOException, ClassNotFoundException, SQLException{
		List<HashMap<String,Object>> list=Db.ExecuteQuery("select remote_addr,request,host from system_nginx_log_"+time+" where request like '%/product/%' order by host ", null);
		//FileWriter fileWriter=new FileWriter("D:\\log20140618.txt");
		Map<String,Integer> domainCount=new HashMap<String,Integer>();
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("<table  border=\"1px\" cellspacing=\"0px\"><tr><td>域名</td><td>点击产品的次数</td><td>访问域名</td></tr>");
		   for (int i = 0; i < list.size(); i++) {
			   String str=String.valueOf(list.get(i).get("request"));
			   str=str.substring(str.indexOf(" ")+1);
			   str=str.substring(0,str.indexOf(" "));
			   String remote_addr=String.valueOf(list.get(i).get("host")).trim();
			   try{
			   str=str.substring(str.lastIndexOf('/')+1,str.indexOf('.'));
			   if(domainCount.containsKey(remote_addr)){
				   domainCount.put(remote_addr,Integer.valueOf(domainCount.get(remote_addr))+1);
			   }else{
				   domainCount.put(remote_addr,1);
			   }
			   }catch(Exception e){
				   System.out.println(str);
				   continue;
			   }
		   // fileWriter.write(String.valueOf(list.get(i).get("remote_addr")).trim()+","+str+","+String.valueOf(list.get(i).get("host")).trim()+"\n");
			  // stringBuilder.append("<tr><td>"+String.valueOf(list.get(i).get("remote_addr")).trim()+"</td><td>"+str+"</td><td>"+String.valueOf(list.get(i).get("host")).trim()+"</td></tr>");
		    }
		   Iterator<?> iter = domainCount.entrySet().iterator();
		 while (iter.hasNext()) {
		   @SuppressWarnings("rawtypes")
		Map.Entry entry = (Map.Entry) iter.next();
		   Object key = entry.getKey();
		   Object val = entry.getValue();
		   stringBuilder.append("<tr><td>"+key+"</td><td>"+val+"</td><td></td></tr>");
		  }
		   stringBuilder.append("</table>");
//		   fileWriter.flush();
//		   fileWriter.close();
		   return stringBuilder.toString();
	}

}
