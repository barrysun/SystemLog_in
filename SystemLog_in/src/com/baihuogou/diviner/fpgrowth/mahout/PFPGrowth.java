package com.baihuogou.diviner.fpgrowth.mahout;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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


@SuppressWarnings({ "deprecation", "unused" })
public class PFPGrowth {
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws IOException { 
        
        Set<String> features = new HashSet<String>();
        String input = "/data/pfp.cvs";
        int minSupport = 3;
        int maxHeapSize = 100;//top-k
        String pattern = " \"[ ,\\t]*[,|\\t][ ,\\t]*\" ";        
        Charset encoding = Charset.forName("UTF-8");
        FPGrowth<String> fp = new FPGrowth<String>();
        String output = "/data/output.cvs";
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
        int i=0;
        for (Pair<String,TopKStringPatterns> entry : frequentPatterns) {
           System.out.print(entry.getFirst()+"-"); // the frequent patterns meet minSupport
            System.out.println(entry.getSecond()); // the frequent patterns meet minSupport and support
            System.out.println(i++);
        }
        System.out.print("\nthe end! ");
    }

}
