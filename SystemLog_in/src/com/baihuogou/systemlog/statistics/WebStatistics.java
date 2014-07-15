package com.baihuogou.systemlog.statistics;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.baihuogou.systemlog.utils.Db;
import com.baihuogou.systemlog.utils.IPAddress;

public class WebStatistics {
	
	
	/*
	   www.100hg.com(包含自动跳转的二级域名及定向指定的二级域名)
       http://????.100hg.com/tttj-0-1.html 天天特价
       http://????.100hg.com/womenshoes.html 女鞋馆
       http://????.100hg.com/women.html 女人馆
       http://????.100hg.com/book 图书馆
       http://????.100hg.com/chaoshi 云超市
       http://m.100hg.com/???? 移动端网站
汇总数据：
商城核心数据（PC+移动端）=访客数+浏览量
运营环节分析=流量环节（top50流量+top搜索关键词+手机端/PC端访客占比）
商品环节（top50的商品）
PC端店铺分析：

A.流量概况：
关键指标（浏览量+访客数）
最近7天top50指标（宝贝+访客来源+访客地区）

B.宝贝被访问排行：
概况：宝贝浏览量 查看人次 日均查看人次 平均停留时间 平均跳失率 
浏览量TOP20（浏览量+访客数）

C.分类页被访排行

D.店内搜索关键词

E.首页被访问数据：
概况：宝贝浏览量 查看人次 日均查看人次 平均停留时间 平均跳失率 
首页到宝贝页
首页到分类页

F:来源分析：
访客分析（详细列表）
宝贝页来源分析（去向分析=退出本店+到本店其它页+到下单页）+来源及去向详细列表
移动端店铺分析：
1.重要需求都同PC端的需求一致.
2.需要着重了解的每天V和M店下面产生的订单数据+注册的店铺数据（包括汇总+详细数据）.
	 */
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static String AboutDomans(String table) throws ClassNotFoundException, SQLException{
		String tr_str="<tr><td style=\"border:solid 1px #666666; padding:8px;\">%s</td><td style=\"border:solid 1px #666666; padding:8px;\">%s</td><td style=\"border:solid 1px #666666; padding:8px;\">%s</td></tr>";
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("<table    border=\"1px\" cellspacing=\"0px\" style=\"border:solid 1px #666666; border-collapse:collapse;text-align:center;\"><tr><th style=\"border:solid 1px #666666;padding:8px;background:#dedede; \">访问路径</th><th style=\"border:solid 1px #666666;padding:8px;background:#dedede; \">PV</th><th style=\"border:solid 1px #666666;padding:8px;background:#dedede; \">IP</th></tr>");
	
		stringBuilder.append(String.format(tr_str," http://*.100hg.com/tttj-0-1.html(天天特价)",Db.ExecuteQuery(
				"select count(*) as num from "+table+" where request like '% /tttj-0-1.html %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num"),Db.ExecuteQuery(
				"select count(DISTINCT(remote_addr)) as num from "+table+" where request like '% /tttj-0-1.html %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num")));

		stringBuilder.append(String.format(tr_str, "http://*.100hg.com/womenshoes.html(女鞋馆)",Db.ExecuteQuery(
				"select count(*) as num from "+table+" where request like '%/womenshoes.html %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num"),Db.ExecuteQuery(
				"select count(DISTINCT(remote_addr)) as num from "+table+" where request like '%/womenshoes.html %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num")));
		
		
		stringBuilder.append(String.format(tr_str, "http://*.100hg.com/women.html(女人馆)",Db.ExecuteQuery(
				"select count(*) as num from "+table+" where request like '%/women.html %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num"),Db.ExecuteQuery(
				"select count(DISTINCT(remote_addr)) as num from "+table+" where request like '%/women.html %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num")));
		
		stringBuilder.append(String.format(tr_str, "http://*.100hg.com/book(图书馆)",Db.ExecuteQuery(
				"select count(*) as num from "+table+" where request like '%/book %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num"),Db.ExecuteQuery(
				"select count(DISTINCT(remote_addr)) as num from "+table+" where request like '%/book %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num")));
		
		stringBuilder.append(String.format(tr_str, "http://*.100hg.com/chaoshi(云超市)",Db.ExecuteQuery(
				"select count(*) as num from "+table+" where request like '%/chaoshi %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num"),Db.ExecuteQuery(
				"select count(DISTINCT(remote_addr)) as num from "+table+" where request like '%/chaoshi %' and remote_addr not in('%61.139.124.218%') and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num")));
		
		
		stringBuilder.append(String.format(tr_str, "http://m.100hg.com/* (移动端网站)",Db.ExecuteQuery(
				"select count(*) as num from "+table+" where host like 'm.100hg.com%' and remote_addr not like '%61.139.124.218%' and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num"),Db.ExecuteQuery(
				"select count(DISTINCT(remote_addr)) as num from "+table+" where host like 'm.100hg.com%' and remote_addr not like '%61.139.124.218%' and remote_addr not like '127.0.0.1%'", null, null).get(0).get("num")));

		stringBuilder.append("</table>");
		return stringBuilder.toString();
	}

	/**
	 * 统计商品点击率排名
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static List<OrderProduct> productTopX(String table) throws ClassNotFoundException, SQLException, UnsupportedEncodingException{
		String sql="select mytable.p_id,mytable.remote_addr from (select  split_part(split_part(request,'/',3),'.',1) as p_id,remote_addr,request from "+table+"  where request like '% /product/%' and request not like ('%.jpeg%') and request not like ('%.JPG%') and request not like ('%.jpg%') and remote_addr not in('%61.139.124.218%')  and remote_addr not like '127.0.0.1%'  )  mytable where mytable.p_id not like '%HTTP%'  order by mytable.p_id";
		List<HashMap<String,Object>> list=Db.ExecuteQuery(sql, null, null);
		List<OrderProduct> ps=new ArrayList<OrderProduct>();
		String ipStr="";
		Integer num=0;
		List<String> address=null;
		for(int i=0;i<list.size();i++){
			if(list.get(i).get("p_id")==null||list.get(i).get("p_id").toString().trim().equals("")){
				continue;
			}
				if(ipStr.equals(list.get(i).get("p_id").toString().trim())){
					address.add(list.get(i).get("remote_addr")+"("+IPAddress.getAddress(list.get(i).get("remote_addr").toString().trim())+")");
					num++;
				}else{
					if(num>0&&!(ipStr.equals(""))){
						ps.add(new OrderProduct(ipStr,num,address));
					}
					ipStr=list.get(i).get("p_id").toString().trim();
					address=new ArrayList<String>();
					num=0;
				}		
		}
		  class ComparatorOrderProduct implements Comparator{
			@Override
			public int compare(Object o1, Object o2) {
				return ((OrderProduct)o2).getNumber().compareTo(((OrderProduct)o1).getNumber());
			}	
		}
		ComparatorOrderProduct comparator=new ComparatorOrderProduct();
		Collections.sort(ps,comparator);
		return ps;
	}
	
	/**
	 *  统计每天的排行榜
	 * @param TopX
	 * @param table
	 * @return
	 * @throws ClassNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws SQLException
	 */
	public static String productTopXMessage(int TopX,String table) throws ClassNotFoundException, UnsupportedEncodingException, SQLException{
		String tr_str="<tr><td style=\"border:solid 1px #666666; padding:8px;\">%s</td><td style=\"border:solid 1px #666666; padding:8px;\">%s</td><td style=\"border:solid 1px #666666; padding:8px;\">%s</td></tr>";
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("<table    border=\"1px\" cellspacing=\"0px\" style=\"border:solid 1px #666666; border-collapse:collapse;text-align:center;\"><tr><th style=\"border:solid 1px #666666;padding:8px;background:#dedede; \">产品编号</th><th style=\"border:solid 1px #666666;padding:8px;background:#dedede; \">IP访问量</th><th style=\"border:solid 1px #666666;padding:8px;background:#dedede; \">IP位置</th></tr>");
		List<OrderProduct> list=productTopX(table);
		for(OrderProduct p:list){
			if(TopX<=0) break;
		//	System.out.println(p.getNumber()+"|"+p.getProductId()+"|"+p.toAddressString());
			stringBuilder.append(String.format(tr_str,p.getProductId(), p.getNumber(),p.toAddressString()));
			TopX--;
		}
		stringBuilder.append("</table>");
		return stringBuilder.toString();
	}
	
	
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, UnsupportedEncodingException{
		//System.out.println(AboutDomans("system_nginx_log_20140713"));
	}
	
	
	

	
	
	

}
