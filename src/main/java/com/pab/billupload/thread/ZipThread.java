package com.pab.billupload.thread;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.LinkedBlockingDeque;

import com.pab.billupload.service.zipbillfile.ZipBillFileService;

/**
 * Created by fisher on 16-12-2.
 */
public class ZipThread extends Thread {

    private int DEFAULT_FILE_LINE_NUM;
    private String ZIP_FILE_PATH;
    private final LinkedBlockingDeque<Path> indexQueue;

    public ZipThread(LinkedBlockingDeque<Path> indexQueue,int DEFAULT_FILE_LINE_NUM,String ZIP_FILE_PATH){
        this.indexQueue=indexQueue;
        this.DEFAULT_FILE_LINE_NUM=DEFAULT_FILE_LINE_NUM;
        this.ZIP_FILE_PATH=ZIP_FILE_PATH;
    }

    public void run(){

        ZipBillFileService zipBillFileService =new ZipBillFileService(DEFAULT_FILE_LINE_NUM,ZIP_FILE_PATH);
        Path indexPath;
        try {
            while (true){
                if ((indexPath=this.indexQueue.poll())!=null){
                zipBillFileService.mainWork(indexPath);
                }else {
                    try {
                        System.out.println("Sleep 2 s..");
                        Thread.sleep(1000*3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
