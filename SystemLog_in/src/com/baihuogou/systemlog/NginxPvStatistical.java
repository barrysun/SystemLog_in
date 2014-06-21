package com.baihuogou.systemlog;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.baihuogou.systemlog.utils.Db;
import com.baihuogou.systemlog.utils.ValueComparator;


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
		   List<Map.Entry<String, Integer>> orderlist = new ArrayList<Map.Entry<String, Integer>>(domainCount.entrySet());
		   ValueComparator vc = new ValueComparator();
		   Collections.sort(orderlist,vc);
		   Iterator<?> iter = orderlist.iterator();
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
	
	/**
	 * 
	 * @param time
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static String executeUVPVStatistical(String time) throws ClassNotFoundException, SQLException{
		
		String[] domainArray=new String[]{"www.100hg.com","file.100hg.com","order.100hg.com","cart.100hg.com","i.100hg.com","club.100hg.com",
				"neijiang.100hg.com","blog.100hg.com","tj.100hg.com","jizhang.100hg.com","openapi.100hg.com","api.100hg.com","chaoshi.100hg.com",
				"search.100hg.com","m.100hg.com","mm.100hg.com","static.100hg.com","passport.100hg.com","uimg.100hg.com","edu.100hg.com",
				"zabbix.100hg.com"};
		String SQL_PV="select count(remote_addr) as pvcount from system_nginx_log_"+time+" %s";
		String SQL_UV="select count(distinct(remote_addr)) as uvcount from system_nginx_log_"+time+" %s";
		Map<String,Integer> domainPVCount=new HashMap<String,Integer>();
		Map<String,Integer> domainUVCount=new HashMap<String,Integer>();
		StringBuilder domainStringBuilder=new StringBuilder();
		for(String domain : domainArray){
			domainStringBuilder.append("'"+domain+"',");
			domainPVCount.put(domain,Integer.valueOf(Db.ExecuteQuery(String.format(SQL_PV, " where host in ('"+domain+"')"), null).get(0).get("pvcount").toString()));
			domainUVCount.put(domain, Integer.valueOf(Db.ExecuteQuery(String.format(SQL_UV,  " where host in ('"+domain+"')"), null).get(0).get("uvcount").toString()));
		}
		String domainStr=domainStringBuilder.substring(0, domainStringBuilder.length()-1);
	
		domainPVCount.put("Other",Integer.valueOf(Db.ExecuteQuery(String.format(SQL_PV, " where host not in ("+domainStr+")"), null).get(0).get("pvcount").toString()));
		domainUVCount.put("Other",Integer.valueOf(Db.ExecuteQuery(String.format(SQL_UV, " where host not in ("+domainStr+")"), null).get(0).get("uvcount").toString()));
		//FileWriter fileWriter=new FileWriter("D:\\log20140618.txt");
		
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("<table  border=\"1px\" cellspacing=\"0px\" style=\"border:solid 1px #666666; border-collapse:collapse;text-align:center;\"><tr><th style=\"border:solid 1px #666666;padding:8px;background:#dedede; \">域名</th><th style=\"border:solid 1px #666666;padding:8px;background:#dedede; \">PV</th><th style=\"border:solid 1px #666666;padding:8px;background:#dedede; \">UV</th></tr>");
		   List<Map.Entry<String, Integer>> orderlist = new ArrayList<Map.Entry<String, Integer>>(domainPVCount.entrySet());
		   ValueComparator vc = new ValueComparator();
		   Collections.sort(orderlist,vc);
		   Iterator<?> iter = orderlist.iterator();
		  while (iter.hasNext()) {
		   @SuppressWarnings("rawtypes")
		   Map.Entry entry = (Map.Entry) iter.next();
		   Object key = entry.getKey();
		   Object val = entry.getValue();
		   stringBuilder.append("<tr><td style=\"border:solid 1px #666666; padding:8px;\">"+key+"</td><td style=\"border:solid 1px #666666; padding:8px;\">"+val+"</td><td style=\"border:solid 1px #666666; padding:8px;\">"+domainUVCount.get(key)+"</td></tr>");
		  }
		   stringBuilder.append("</table>");
//		   fileWriter.flush();
//		   fileWriter.close();
		   return stringBuilder.toString();
	}
}


