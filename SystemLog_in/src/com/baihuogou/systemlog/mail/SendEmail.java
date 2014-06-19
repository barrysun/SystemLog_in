package com.baihuogou.systemlog.mail;

import java.io.IOException;

public class SendEmail {
	
	public static void sendEmail(){
		//这个类主要是设置邮件   
	      MailSenderInfo mailInfo = new MailSenderInfo();    
	      mailInfo.setMailServerHost("smtp.163.com");    
	      mailInfo.setMailServerPort("25");    
	      mailInfo.setValidate(true);    
	      mailInfo.setUserName("15882346251@163.com");    
	      mailInfo.setPassword("sunwubin");//您的邮箱密码    
	      mailInfo.setFromAddress("15882346251@163.com");    
	      mailInfo.setToAddress("1096490965@qq.com");    
	      mailInfo.setSubject("20140617PV统计"); 
	      StringBuilder str=new StringBuilder();
	        //可以换成工程目录下的其他文本文件
	    /*  str.append("<table border=\"1px\" cellspacing=\"0px\">");
	      str.append("<tr><td>时间</td><td>PV点击量</td></tr>");
	      str.append("<tr><td>00:00-01:00</td><td>214</td></tr>");
	      str.append("</tr></table>");*/
	      
	      mailInfo.setContent("执行完成。。。");    
	         //这个类主要来发送邮件   
	      SimpleMailSender sms = new SimpleMailSender();   
	      //sms.sendTextMail(mailInfo);//发送文体格式    
	      sms.sendHtmlMail(mailInfo);//发送html格式   
	}
	

}
