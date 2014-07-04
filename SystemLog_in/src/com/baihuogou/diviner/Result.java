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

import com.baihuogou.systemlog.utils.Db;
import com.baihuogou.systemlog.utils.DbConnection;
import com.baihuogou.systemlog.utils.DivinerConstant;
import com.baihuogou.systemlog.utils.Log4jUtil;
import org.apache.log4j.Logger;

public class Result {
	private static Logger logger = Log4jUtil.getLogger(Result.class);
	final static int NEIGHBORHOOD_NUM = 2;
	final static int RECOMMENDER_NUM = 3;
	
	public static void main(String[] args){
		action();
	}

	public static void action() {
		try {
			OrderCsv.createOrderCsvJob();
			Calculate();
		} catch (ClassNotFoundException | SQLException | IOException
				| TasteException e) {
			logger.error(e.getMessage());
		}
	}

	public static void Calculate() throws TasteException, IOException,
			ClassNotFoundException, SQLException {
		String file = DivinerConstant.ORDERCSV_PATH;
		System.out.println(new File(DivinerConstant.ORDERCSV_PATH).getPath());
		DataModel dataModel = RecommendFactory.buildDataModel(file);
		RecommenderBuilder rb1 = Evaluator.userEuclidean(dataModel);
		RecommenderBuilder rb2 = Evaluator.itemEuclidean(dataModel);
		RecommenderBuilder rb3 = Evaluator.userEuclideanNoPref(dataModel);
		RecommenderBuilder rb4 = Evaluator.itemEuclideanNoPref(dataModel);
		LongPrimitiveIterator iter = dataModel.getUserIDs();
		Db.executeUpdate("delete from recommendrcf", null,
				DbConnection.getConn("RE"));
		while (iter.hasNext()) {
			long uid = iter.nextLong();
			String userEuclidean = result(uid, rb1, dataModel);
			String itemEuclidean = result(uid, rb2, dataModel);
			String userEuclideanNoPref = result(uid, rb3, dataModel);
			String itemEuclideanNoPref = result(uid, rb4, dataModel);
			if ((userEuclidean != null && userEuclidean.length() > 0)
					|| (itemEuclidean != null && itemEuclidean.length() > 0)
					|| (userEuclideanNoPref != null && userEuclideanNoPref.length() > 0)
					|| (itemEuclideanNoPref != null && itemEuclideanNoPref.length() > 0)) {
				Db.executeUpdate(
						"insert into recommendrcf(uid,usereuclidean,itemeuclidean,usereuclideannopref,itemeuclideannopref) values(?,?,?,?,?)",
						new Object[] { RecommendFactory.showUser((int) uid),
								userEuclidean, itemEuclidean,
								userEuclideanNoPref, itemEuclideanNoPref },
						DbConnection.getConn("RE"));
			}
		}
	}

	public static String result(long uid,
			RecommenderBuilder recommenderBuilder, DataModel dataModel)
			throws TasteException {
		List<RecommendedItem> list = recommenderBuilder.buildRecommender(
				dataModel).recommend(uid, RECOMMENDER_NUM);
		return RecommendFactory.showItems(uid, list, false);
	}
}
