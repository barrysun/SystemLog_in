package com.baihuogou.systemlog;

import java.io.IOException;
import java.sql.SQLException;

public class NginxPvStatisticalTest {
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException{
		NginxPvStatistical. executeStatistical("20140619");
	}

}
