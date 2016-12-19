package com.pab.billupload.thread;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingDeque;

import com.pab.billupload.service.watchdir.WatchDirService;
import com.pab.billupload.util.PropertiesUtil;

public class WatchThread extends Thread {

    private String scan_path = "";
    private boolean recursive_flag = false;
    private final LinkedBlockingDeque<Path> indexQueue;


    public WatchThread(LinkedBlockingDeque<Path> indexQueue) {
        this.indexQueue = indexQueue;
        Properties prop = PropertiesUtil.getProperties();
        this.scan_path = prop.getProperty("SCAN_PATH");
        this.recursive_flag = (prop.getProperty("recursive_flag").equals("yes"));
    }

    public void run() {

        try {
            WatchDirService watchDir = new WatchDirService(this.scan_path, this.recursive_flag, indexQueue);
            watchDir.processEvents();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
