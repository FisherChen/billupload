package com.pab.billupload.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	private static Properties prop = null;

	public static Properties getProperties() {
		if (prop == null) {
			String path=PropertiesUtil.class.getResource("/sys.properties").getPath();
			File propFile = new File(path);
			try {
				Properties prop2 = new Properties();
				FileInputStream fis = new FileInputStream(propFile);
				prop2.load(fis);// 将属性文件流装载到Properties对象中
				prop=prop2;
				fis.close();// 关闭流
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

}
