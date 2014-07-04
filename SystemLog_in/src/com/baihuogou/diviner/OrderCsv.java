package com.baihuogou.diviner;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.baihuogou.systemlog.utils.Db;
import com.baihuogou.systemlog.utils.DbConnection;
import com.baihuogou.systemlog.utils.DivinerConstant;


public class OrderCsv {
	
	/**
	 * 构建Order评分矩阵，商品 和用户key Value Csv文件
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void createOrderCsvJob() throws ClassNotFoundException, SQLException, IOException{
		
		List<HashMap<String,Object>> list=Db.ExecuteQuery("select product_id,change_by_user_login_id from order_item  ", null,DbConnection.getConn("ES") );
		FileWriter fileWriter=new FileWriter(DivinerConstant.ORDERCSV_PATH);
		FileWriter ufileWriter=new FileWriter(DivinerConstant.USERCSV_PATH);
		FileWriter pfileWriter=new FileWriter(DivinerConstant.ITEMCSV_PATH);
		
		HashMap<String,Integer> map= new HashMap<String,Integer>();
		HashMap<String,Integer> pMap=new HashMap<String,Integer>();
		List<HashMap<String,Object>> plist=Db.ExecuteQuery("select distinct(product_id) from order_item order by product_id", null,DbConnection.getConn("ES") );
		List<HashMap<String,Object>> ulist=Db.ExecuteQuery("select distinct(change_by_user_login_id) from order_item order by change_by_user_login_id", null,DbConnection.getConn("ES"));
		for(int i=0;i<plist.size();i++){
			  if(plist.get(i).get("product_id")!=null&&!plist.get(i).get("product_id").toString().equals("")){
				  pfileWriter.write((i+1)+","+String.valueOf(plist.get(i).get("product_id")).trim()+"\n"); 
				  pMap.put(String.valueOf(plist.get(i).get("product_id")).trim(), (i+1));
			  }
		  }
		pfileWriter.flush();
		pfileWriter.close();
		for(int i=0;i<ulist.size();i++){
			  if(ulist.get(i).get("change_by_user_login_id")!=null&&!ulist.get(i).get("change_by_user_login_id").toString().equals("")){
				  ufileWriter.write((i+1)+","+String.valueOf(ulist.get(i).get("change_by_user_login_id")).trim()+"\n"); 
				  map.put(String.valueOf(ulist.get(i).get("change_by_user_login_id")).trim(), (i+1));
			  }
		  }
		 ufileWriter.flush();
		  ufileWriter.close();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).get("change_by_user_login_id")!=null&&!list.get(i).get("change_by_user_login_id").toString().equals(""))
		    fileWriter.write(map.get(String.valueOf(list.get(i).get("change_by_user_login_id")).trim())+","+pMap.get(String.valueOf(list.get(i).get("product_id")).trim())+",1.0\n");
		    }
		   fileWriter.flush();
		   fileWriter.close();
	}
	
}
