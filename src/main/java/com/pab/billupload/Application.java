package com.pab.billupload;


import com.pab.billupload.thread.WatchThread;
import com.pab.billupload.thread.ZipThread;
import com.pab.billupload.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by fisher on 16-10-15.
 */
public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Properties prop = PropertiesUtil.getProperties();
        int MAX_THREAD_NUM = Integer.parseInt(prop.getProperty("MAX_THREAD_NUM"));
        int DEFAULT_FILE_LINE_NUM = Integer.parseInt(prop.getProperty("DEFAULT_FILE_LINE_NUM"));
        String ZIP_FILE_PATH = prop.getProperty("ZIP_FILE_PATH");

        logger.info("App starting .....");

        LinkedBlockingDeque<Path> indexFileQueue = new LinkedBlockingDeque();
        Thread watchThread = new WatchThread(indexFileQueue);
        watchThread.start();


        for (int i = 0; i < MAX_THREAD_NUM; i++) {
            ZipThread zipThread = new ZipThread(indexFileQueue, DEFAULT_FILE_LINE_NUM, ZIP_FILE_PATH);
            zipThread.start();
        }

        logger.info("App start Done. ");
    }
}

