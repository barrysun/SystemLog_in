package com.baihuogou.diviner.fpgrowth.mahout;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
public class HdfsFile {
	
	public static void createFile(String dst,byte[] contents) throws IOException{
		Configuration conf=new Configuration();
		FileSystem fs=FileSystem.get(conf);
		Path dstPath=new Path(dst);//
		 //打开一个输出流
		FSDataOutputStream outputStream = fs.create(dstPath);
		outputStream.write(contents);
		outputStream.close();
		fs.close();
		System.out.println("文件创建成功！");
	}
	//上传本地文件
	public static void uploadFile(String src,String dst) throws IOException{
		Configuration conf=new Configuration();
		FileSystem fs=FileSystem.get(conf);
		Path srcPath=new Path(src);
		Path dstPath=new Path(dst);
		//调用文件系统的文件复制函数,前面参数是指是否删除原文件，true为删除，默认为false
		fs.copyFromLocalFile(false,srcPath, dstPath);
		 //打印文件路径
	     System.out.println("Upload to "+conf.get("fs.default.name"));
	     System.out.println("------------list files------------"+"\n");
	     FileStatus [] fileStatus = fs.listStatus(dstPath);
	     for (FileStatus file : fileStatus) 
	     {
	            System.out.println(file.getPath());
	     }
	     fs.close();
	}
	
	   /**
     * 在HDFS上创建目录
     * @param content
     * @param dstFile
     * @throws IOException 
     */
    public static void createDirectoryInHDFS(String dstDir) {
        Configuration conf = new Configuration();
        Path dst = new Path(dstDir);
        FileSystem hdfs = null;        
        try {            
            hdfs=FileSystem.get(conf);
            hdfs.mkdirs(dst);            
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(hdfs != null) {
                try {
                    hdfs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }        
    }
	
	public static void readFile(String filePath) throws IOException{
		Configuration conf=new Configuration();
		FileSystem fs=FileSystem.get(conf);
		Path srcPath=new Path(filePath);
		InputStream in=null;
		try{
			in=fs.open(srcPath);
			IOUtils.copyBytes(in, System.out, 4096, false); //复制到标准输出流
		}finally{
			IOUtils.closeStream(in);

		}
	}
	
	public static void mkdirs(String folder) throws IOException {
		Configuration conf = new Configuration();
        Path path = new Path(folder);
        FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.1.201:9000"), conf);
        if (!fs.exists(path)) {
            fs.mkdirs(path);
            System.out.println("Create: " + folder);
        }
        fs.close();
    }
}
