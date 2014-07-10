package com.baihuogou.diviner.fpgrowth.mahout;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashSet; 
import java.util.Set;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text; 
import org.apache.mahout.fpm.pfpgrowth.convertors.ContextStatusUpdater;
import org.apache.mahout.fpm.pfpgrowth.convertors.SequenceFileOutputCollector;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.StringOutputConverter;
                                                                                                                                                         
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;
import org.apache.mahout.fpm.pfpgrowth.fpgrowth.FPGrowth;    
import org.apache.mahout.math.map.OpenLongObjectHashMap;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.FileLineIterable;
import org.apache.mahout.common.iterator.StringRecordIterator;

import com.baihuogou.diviner.OrderCsv;
import com.baihuogou.diviner.RecommendFactory;
import com.baihuogou.systemlog.utils.Db;
import com.baihuogou.systemlog.utils.DbConnection;
import com.baihuogou.systemlog.utils.DivinerConstant;


@SuppressWarnings({ "deprecation", "unused" })
public class PFPGrowth {

	public static void action(){
		try {
			OrderCsv.createPFPCsv();
			HdfsFile.uploadFile(DivinerConstant.PFPCSV_PATH,"/data/pfp.csv");
			runPFP();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void runPFP()  throws IOException, ClassNotFoundException, SQLException{
		Set<String> features = new HashSet<String>();
        String input = "/data/pfp.csv";
        int minSupport = 2;
        int maxHeapSize = 100;//top-k
        String pattern = " \"[ ,\\t]*[,|\\t][ ,\\t]*\" ";        
        Charset encoding = Charset.forName("UTF-8");
        FPGrowth<String> fp = new FPGrowth<String>();
        String output = "/data/output.csv";
        Path path = new Path(output);
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf, path, Text.class, TopKStringPatterns.class);
        fp.generateTopKFrequentPatterns(
                new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern),
                fp.generateFList(
                    new StringRecordIterator(new FileLineIterable(new File(input), encoding, false), pattern),
                    minSupport),
                minSupport,
                maxHeapSize,
                features,
                new StringOutputConverter(new SequenceFileOutputCollector<Text,TopKStringPatterns>(writer)),
                new ContextStatusUpdater(null));
        writer.close();
        List<Pair<String,TopKStringPatterns>> frequentPatterns = FPGrowth.readFrequentPattern( conf, path);
        Db.executeUpdate("delete from recommendrpfp ", null, DbConnection.getConn("RE"));
        for (Pair<String,TopKStringPatterns> entry : frequentPatterns) {
           String weights=entry.getFirst().trim().replaceAll(" ", "|");
           String related_child=entry.getSecond().toString().split(",")[1].replaceAll("\\)", "");
           Db.executeUpdate(
					"insert into recommendrpfp (weights,related_child) values(?,?)",
					new Object[] {weights,related_child },
					DbConnection.getConn("RE"));
        }
	}
    
	public static void main(String[] args) { 
		action();
    }

}
