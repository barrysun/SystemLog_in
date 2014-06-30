package com.baihuogou.systemlog.mail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.baihuogou.systemlog.NginxPvStatistical;
import com.baihuogou.systemlog.utils.Db;

public class SendEmail {

	public static void sendEmail(String FileName)
			throws ClassNotFoundException, SQLException {
		SendProductPV(FileName);
		SendPvUvEmail(FileName);
	}

	public static void SendProductPV(String FileName) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.163.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("15882346251@163.com");
		mailInfo.setPassword("sunwubin");// 您的邮箱密码
		mailInfo.setFromAddress("15882346251@163.com");
		// StringBuilder str=new StringBuilder();
		// 可以换成工程目录下的其他文本文件
		/*
		 * str.append("<table border=\"1px\" cellspacing=\"0px\">");
		 * str.append("<tr><td>时间</td><td>PV点击量</td></tr>");
		 * str.append("<tr><td>00:00-01:00</td><td>214</td></tr>");
		 * str.append("</tr></table>");
		 */
		try {
			mailInfo.setContent(NginxPvStatistical.executeStatistical(FileName));
		} catch (ClassNotFoundException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mailInfo.setSubject(FileName + " PV统计");
		mailInfo.setToAddress("1096490965@qq.com");
		// sms.sendTextMail(mailInfo);//发送文体格式
		SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式

		/*
		 * mailInfo.setSubject(FileName + " PV统计"); String[]
		 * emailArray=emails("tongji"); if(emailArray!=null){ for(String
		 * email:emailArray){ mailInfo.setToAddress(email); //
		 * sms.sendTextMail(mailInfo);//发送文体格式
		 * SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式 } }
		 */
	}

	public static void SendPvUvEmail(String FileName)
			throws ClassNotFoundException, SQLException {
		// 这个类主要是设置邮件
		String[] emailArray = emails("pvuv");
		String emailContent = NginxPvStatistical
				.executeUVPVStatistical(FileName);
		if (emailArray != null) {
			for (String email : emailArray) {
				System.out.println(email);
				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setMailServerHost("smtp.163.com");
				mailInfo.setMailServerPort("25");
				mailInfo.setValidate(true);
				mailInfo.setUserName("15882346251@163.com");
				mailInfo.setPassword("sunwubin");// 您的邮箱密码
				mailInfo.setFromAddress("15882346251@163.com");
				mailInfo.setContent(emailContent);
				mailInfo.setSubject(FileName + " PV UV统计");
				// sms.sendTextMail(mailInfo);//发送文体格式
				// SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式
				// mailInfo.setSubject(FileName + " PV统计");
				mailInfo.setToAddress(email);
				// sms.sendTextMail(mailInfo);//发送文体格式
				SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式
			}
		}
	}

	/*
	 * public static void main(String[] args){ sendEmail(); }
	 */
	public static String[] emails(String theme) throws ClassNotFoundException,
			SQLException {
		String sql = "select email_list,email_title from push where theme=?";
		List<HashMap<String, Object>> list = Db.ExecuteQuery(sql,
				new Object[] { theme });
		String[] emailArr = null;
		if (list != null && list.size() == 1) {
			emailArr = list.get(0).get("email_list").toString().split("\\|");
		}
		return emailArr;
	}
}
