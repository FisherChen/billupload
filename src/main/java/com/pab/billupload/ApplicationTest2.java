package com.pab.billupload;


import com.pab.billupload.thread.ZipThread;
import com.pab.billupload.util.PropertiesUtil;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by fisher on 16-10-15.
 */
public class ApplicationTest2 {
    private static Logger logger = LoggerFactory.getLogger(ApplicationTest2.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Properties prop = PropertiesUtil.getProperties();
        int MAX_THREAD_NUM = Integer.parseInt(prop.getProperty("MAX_THREAD_NUM"));
        int DEFAULT_FILE_LINE_NUM = Integer.parseInt(prop.getProperty("DEFAULT_FILE_LINE_NUM"));
        String ZIP_FILE_PATH = prop.getProperty("ZIP_FILE_PATH");
        String indexPath = prop.getProperty("SCAN_PATH");
        int APPS_NUM = Integer.parseInt(prop.getProperty("APPS_NUM"));
        int APPS_INDEX = Integer.parseInt(prop.getProperty("APPS_INDEX"));


        logger.info("App starting .....");

        LinkedBlockingDeque<Path> indexFileQueue = new LinkedBlockingDeque();

        File indexFilePath = new File(indexPath);
        String[] indexFilesName = indexFilePath.list(new AndFileFilter(new SuffixFileFilter(".txt"), new NotFileFilter(new SuffixFileFilter("Re.txt"))));
        Arrays.sort(indexFilesName);

        // 为了考虑文件是顺序的写
        for (int i = 0; i < indexFilesName.length; i++) {
            if ( ((i+1) % APPS_NUM) == APPS_INDEX){
                indexFileQueue.offer(Paths.get(indexPath + indexFilesName[i]));
            }
        }


        for (int i = 0; i < MAX_THREAD_NUM; i++) {
            ZipThread zipThread = new ZipThread(indexFileQueue, DEFAULT_FILE_LINE_NUM, ZIP_FILE_PATH);
            zipThread.start();
        }

        logger.info("App start Done. ");
    }
}

