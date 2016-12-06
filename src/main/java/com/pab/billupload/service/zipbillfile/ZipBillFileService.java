package com.pab.billupload.service.zipbillfile;

import org.apache.commons.io.input.ReversedLinesFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pab.billupload.pojo.BillIndexInfo;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

/**
 * Created by fisher on 16-10-15.
 */
public class ZipBillFileService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static String separator = FileSystems.getDefault().getSeparator();

    //default index file line num is 10000
    private int DEFAULT_FILE_LINE_NUM = 10000;

    //default zip file path is boot "/"
    private String ZIP_FILE_PATH = "/";


    public ZipBillFileService(int DEFAULT_FILE_LINE_NUM, String ZIP_FILE_PATH) {
        this.DEFAULT_FILE_LINE_NUM = DEFAULT_FILE_LINE_NUM;
        if (separator.equals(ZIP_FILE_PATH.substring(ZIP_FILE_PATH.length() - 1))) {
            this.ZIP_FILE_PATH = ZIP_FILE_PATH;
        } else {
            this.ZIP_FILE_PATH = ZIP_FILE_PATH + separator;
        }

    }

    //only watch the "ENTRY_CREATE" event type and the index file name should not contain "Re".
    public void mainWork(Path dir) throws IOException {


        String indexFileName = dir.getFileName().toString();
        // use String buffer store temp index code
        StringBuffer index = new StringBuffer();

        //user array for store log
        String[] log = new String[this.DEFAULT_FILE_LINE_NUM];
        long startTime = System.nanoTime();

        logger.info("now create zip file : " + dir.toString());
        if (indexFileName.indexOf("Re") <= 0) {

            ZipOutputStream zipOut = null;
            try {
                zipOut = createZipFile(dir, index, log);
                addIndexFileToZip(zipOut, index);
                String packageId = updateZipToCloud(zipOut);
                createLogFile(packageId, log, dir.getFileName().toString());

            } finally {
                closeStream(null, zipOut);
                index = null;
                log = null;
            }
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        double secondsElapsed = (double) elapsedTime / 1000000000.0;
        logger.info("OK, zip index file has done ,use : " + secondsElapsed + " s. " + dir.toString());

    }


    public BillIndexInfo convertLineToIndexBaseInfo(String line, Path dir) {

        BillIndexInfo billIndexInfo = new BillIndexInfo();
        String[] str = line.split("\\|", -1);

        billIndexInfo.setEmail(str[0]);
        billIndexInfo.setAccNo(str[1]);
        String[] splitFileName = str[2].split("/");
        billIndexInfo.setFileName(splitFileName[splitFileName.length - 1]);
        Path dirParent = dir.getParent().getParent();
        billIndexInfo.setBillPath(dirParent.toString() + separator + billIndexInfo.getFileName().split("_")[1] + separator + billIndexInfo.getFileName());

        return billIndexInfo;
    }

    public ZipOutputStream createZipFile(Path dir, StringBuffer index, String[] log) throws IOException {

        // parse the index file by line, each line converts to BillIndexInfo.
        String indexFileName = dir.getFileName().toString();
        BufferedReader reader = Files.newBufferedReader(dir, Charset.forName("UTF-8"));
        String zipFilePath = this.ZIP_FILE_PATH + indexFileName.split("\\.")[0] + ".zip";

        ZipOutputStream zipOut = null;

        try {
            zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
            String str;
            int i = 0;
            while ((str = reader.readLine()) != null) {

                BillIndexInfo billIndexInfo = this.convertLineToIndexBaseInfo(str, dir);

                //create index line
                index.append(billIndexInfo.toString("|"));

                //add file name to log
                log[i] = billIndexInfo.getFileName();
                i++;

                // add file to zip
                appendFileToZip(billIndexInfo.getBillPath(), zipOut);
            }
        } finally {
            return zipOut;
        }
    }

    public void appendFileToZip(String billFilePath, ZipOutputStream zipOut) throws IOException {
        FileInputStream billIn = null;
        int num = 0;
        try {
            File billfile = new File(billFilePath);
            boolean unfinished = true;

            while (unfinished) {
                try {
                    if (billfile.exists()) {
                        unfinished = !(checkFile(billfile));
                        if (unfinished) {
                            throw new FileNotFoundException();
                        }
                    } else {
                        throw new FileNotFoundException();
                    }
                } catch (IOException e) {
                    // make thread Sleep 2 Secends?
                    if (num >= 100) {
                        logger.info("Cant't find file or file doesn't been writed finished : " + billfile);
                        logger.info(" Now Sleeping 2 s..");
                        try {
                            Thread.sleep(1000 * 2);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        num = 0;
                    } else {
                        num++;
                    }
                }
            }
            billIn = new FileInputStream(billFilePath);
            zipOut.putNextEntry(new ZipEntry(Paths.get(billFilePath).getFileName().toString()));
            IOUtils.copy(billIn, zipOut);
        } finally {
            closeStream(billIn, null);
        }
    }

    public void addIndexFileToZip(ZipOutputStream zipOut, StringBuffer index) throws IOException {
        ByteArrayInputStream indexIn = null;
        if (index.length() > 0) {
            try {
                indexIn = new ByteArrayInputStream(index.toString().getBytes(StandardCharsets.UTF_8));
                zipOut.putNextEntry(new ZipEntry("INDEX"));
                IOUtils.copy(indexIn, zipOut);
            } finally {
                closeStream(indexIn, null);
            }
        }
    }

    public void createLogFile(String packageId, String[] log, String indexFileName) throws IOException {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < log.length; i++) {
            buff.append(packageId).append(",").append(log[i]).append("\n");
        }

        ByteArrayInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new ByteArrayInputStream(buff.toString().getBytes(StandardCharsets.UTF_8));
            outputStream = new FileOutputStream(this.ZIP_FILE_PATH + indexFileName.split("\\.")[0] + ".log");
            IOUtils.copy(inputStream, outputStream);
        } finally {
            closeStream(inputStream, outputStream);
        }

    }

    public void closeStream(InputStream is, OutputStream os) throws IOException {
        if (is != null) {
            is.close();
        }
        if (os != null) {
            os.close();
        }
    }

    /*notice:
    * The BillFile newline character must use "\n"（Linux\Unix）,Can't use "\r\n"(Windows).
    * If you want to test this on Windows,Please make your newline character。
    * */
    public boolean checkFile(File billfile) {
        boolean flag = false;
        ReversedLinesFileReader billReader = null;
        try {
            billReader = new ReversedLinesFileReader(billfile);
            String lastLine = billReader.readLine();
            if (lastLine.length() < 1) {
                lastLine = billReader.readLine();
            }
            if (lastLine.equals("</html>")) {
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                billReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public String updateZipToCloud(ZipOutputStream zipOutputStream) {
        String packageId = "test";
        // code here
        return packageId;
    }


}
