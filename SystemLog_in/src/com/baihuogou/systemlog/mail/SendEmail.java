package com.baihuogou.systemlog.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.baihuogou.systemlog.NginxPvStatistical;
import com.baihuogou.systemlog.utils.Db;
import com.baihuogou.systemlog.utils.Log4jUtil;

public class SendEmail {
	private static Logger logger = Log4jUtil.getLogger(SendEmail.class);
	public static void sendEmail(String FileName)
			throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
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
		try {
			mailInfo.setContent(NginxPvStatistical.executeStatistical(FileName));
		} catch (ClassNotFoundException | IOException | SQLException e) {
			logger.error(e.getMessage());
		}
		mailInfo.setSubject(FileName + " PV统计");
		mailInfo.setToAddress("1096490965@qq.com");
		// sms.sendTextMail(mailInfo);//发送文体格式
		SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式
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
				mailInfo.setToAddress(email);
				SimpleMailSender.sendHtmlMail(mailInfo);// 发送html格式
			}
		}
	}
	
	public static String[] emails(String theme) throws ClassNotFoundException,
			SQLException {
		String sql = "select email_list,email_title from push where theme=?";
		List<HashMap<String, Object>> list = Db.ExecuteQuery(sql,
				new Object[] { theme },null);
		String[] emailArr = null;
		if (list != null && list.size() == 1) {
			emailArr = list.get(0).get("email_list").toString().split("\\|");
		}
		return emailArr;
	}
	

}
