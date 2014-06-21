package com.baihuogou.systemlog.mail;

import java.sql.SQLException;

public class SendEmailTest {
	
	  public static void main(String[] args) throws ClassNotFoundException, SQLException{ 
		 // SendEmail.sendEmail("20140619");
		  SendEmail.SendPvUvEmail("20140619");
	  }

}
