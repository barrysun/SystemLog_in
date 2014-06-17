package com.baihuogou.systemlog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;

public class PropertiesUtil extends Properties {
	private static final long serialVersionUID = 1L;
	private Properties propt;

	public PropertiesUtil() {
		super();
		propt = new Properties();
	}

	public String getString(String fileName, String key) {
		return getString(fileName, key, "");
	}

	public String getString(String fileName, String key, String defaultValue) {
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream(fileName);
			propt.load(is);
			if (key == null)
				return null;
			if (propt == null)
				return defaultValue;
			if (defaultValue == null)
				defaultValue = "";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return propt.getProperty(key, defaultValue);
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {
		if (key == null || propt == null)
			return defaultValue;
		String str = propt.getProperty(key);
		if (str == null)
			return defaultValue;
		else {
			int n;
			try {
				n = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				n = 0;
			}
			return n;
		}
	}

	public long getLong(String key) {
		return getInt(key, 0);
	}

	public long getLong(String key, long defaultValue) {
		if (key == null)
			return defaultValue;
		String str;
		if (propt == null)
			return defaultValue;
		else
			str = propt.getProperty(key);
		long n;
		try {
			n = Long.parseLong(str);
		} catch (Exception e) {
			n = defaultValue;
		}
		return n;
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		if (key == null)
			return defaultValue;
		if (propt != null) {
			String str = propt.getProperty(key);
			if (str == null)
				return defaultValue;
			else if (str.equalsIgnoreCase("true"))
				return true;
			else
				return false;
		}
		return defaultValue;
	}

	public String[] getStrArray(String key) {
		return getStrArray(key, " \t\n\r\f", "");
	}

	public String[] getStrArray(String key, String delim) {
		return getStrArray(key, delim, "");
	}

	public String[] getStrArray(String key, String delim, String defaultValue) {
		String[] result;
		if (key == null)
			return null;
		if (defaultValue == null)
			defaultValue = "";
		String str = getString(key, defaultValue);
		StringTokenizer st = new StringTokenizer(str, delim);
		int n = st.countTokens();
		result = new String[n];
		int i = 0;
		while (st.hasMoreElements()) {
			result[i] = (String) st.nextElement();
			i++;
		}
		return result;
	}
}
