package com.pab.billupload;

import com.pab.billupload.service.zipbillfile.ZipBillFileService;

import com.pab.billupload.util.PropertiesUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by fisher on 16-10-15.
 */
public class ApplicationTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        Properties prop = PropertiesUtil.getProperties();
        int DEFAULT_FILE_LINE_NUM = Integer.parseInt(prop.getProperty("DEFAULT_FILE_LINE_NUM"));
        String ZIP_FILE_PATH = prop.getProperty("ZIP_FILE_PATH");

        String fileIndexName="";
        if(args.length<1 ){
            System.out.println("请输入文件名（绝对路径）");
            System.exit(1);
        }else{
            fileIndexName=args[0];
            if(fileIndexName.length()<1){
                System.out.println("请输入打包的索引文件名（绝对路径）");
                System.exit(1);
            }
        }


        File index=new File(fileIndexName);
        if(!index.exists()){
            System.out.println(args[0]+" 不存在请重新输入！ ");
            System.exit(1);
        }

        Path dir = Paths.get(fileIndexName);
        List indexFileList = new ArrayList<Path>();
        indexFileList.add(dir);

        for (int i = 0; i < indexFileList.size(); i++) {

            ZipBillFileService zipBillFileService = new ZipBillFileService(DEFAULT_FILE_LINE_NUM, ZIP_FILE_PATH);
            zipBillFileService.mainWork((Path) indexFileList.get(i));
        }

    }
}