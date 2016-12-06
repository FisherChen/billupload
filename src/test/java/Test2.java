import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

/**
 * Created by fisher on 16-12-4.
 */
public class Test2 {
    public static void main(String[] args) throws IOException, InterruptedException {
        //
        // File billfile = new File("/home/fisher/Documents/test/billtest/20160803_00000001_001_0099902.html");
        File billfile = new File("/home/fisher/Documents/test/billtest/othertest/20160803_00000001_001_009990.html");

        System.out.println(!(checkFile(billfile)));
    }


    public static boolean checkFile(File billfile){
        boolean flag=false;
        ReversedLinesFileReader billReader = null;
        try {
            billReader = new ReversedLinesFileReader(billfile);
            String lastline=billReader.readLine();
            if (lastline.length()<1){
                lastline=billReader.readLine();
            }
            if(lastline.equals("</html>")){
                flag=true;
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


}
