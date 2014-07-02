package com.baihuogou.diviner;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.baihuogou.systemlog.utils.DivinerConstant;


public class Result {
	final static int NEIGHBORHOOD_NUM = 2;
	final static int RECOMMENDER_NUM = 3;
	
	public static void Calculate() throws TasteException, IOException{
		String file = DivinerConstant.ORDERCSV_PATH;
	    System.out.println(new File(DivinerConstant.ORDERCSV_PATH).getPath());
/*      MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("toor");
        dataSource.setDatabaseName("mahout");                            
        MySQLJDBCIDMigrator mysqlMigrator= new MySQLJDBCIDMigrator(dataSource, "intro", "uid", "iid");
        */ 
	    
        DataModel dataModel = RecommendFactory.buildDataModel(file);
        RecommenderBuilder rb1 = Evaluator.userEuclidean(dataModel);
        RecommenderBuilder rb2 = Evaluator.itemEuclidean(dataModel);
        RecommenderBuilder rb3 = Evaluator.userEuclideanNoPref(dataModel);
        RecommenderBuilder rb4 = Evaluator.itemEuclideanNoPref(dataModel);
        LongPrimitiveIterator iter = dataModel.getUserIDs();
        while (iter.hasNext()) {
            long uid = iter.nextLong();
         // System.out.print("userEuclidean       =>");
            result(uid, rb1, dataModel);
          //  System.out.print("itemEuclidean       =>");
            result(uid, rb2, dataModel);
         //   System.out.print("userEuclideanNoPref =>");
            result(uid, rb3, dataModel);
         //   System.out.print("itemEuclideanNoPref =>");
            result(uid, rb4, dataModel);
        }
	}
	
	  public static void main(String[] args) throws TasteException, IOException, ClassNotFoundException, SQLException {
		    OrderCsv.createOrderCsvJob();
		    Calculate();
	    }

	    public static void result(long uid, RecommenderBuilder recommenderBuilder, DataModel dataModel) throws TasteException {
	        List<RecommendedItem> list = recommenderBuilder.buildRecommender(dataModel).recommend(uid, RECOMMENDER_NUM);
	        RecommendFactory.showItems(uid, list, false);
	    }
}
