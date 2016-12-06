package com.pab.billupload;

import com.pab.billupload.service.zipbillfile.ZipBillFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pab.billupload.util.PropertiesUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by fisher on 16-10-15.
 */
public class Application3 {
	private static Logger logger = LoggerFactory.getLogger(Application3.class);

	public static void main(String[] args) throws IOException, InterruptedException {
		Properties prop = PropertiesUtil.getProperties();
		int DEFAULT_FILE_LINE_NUM = Integer.parseInt(prop.getProperty("DEFAULT_FILE_LINE_NUM"));
		String ZIP_FILE_PATH = prop.getProperty("ZIP_FILE_PATH");

		List indexFileList = new ArrayList<Path>();
		Path dir = Paths.get("/home/fisher/Documents/test/billtest/INDEX/2.txt");
		indexFileList.add(dir);

		for (int i = 0; i < indexFileList.size(); i++) {

			ZipBillFileService zipBillFileService = new ZipBillFileService(DEFAULT_FILE_LINE_NUM, ZIP_FILE_PATH);
			zipBillFileService.mainWork((Path) indexFileList.get(i));
		}

	}
}
