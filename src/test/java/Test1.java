import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by fisher on 16-12-4.
 */
public class Test1 {
    public static void main(String[] args) throws IOException {

        String str = "hello \r\n";
        OutputStream out = null;
        InputStream in = null;
        try {
            out = new FileOutputStream("/home/fisher/Documents/test/othertest/bigfile.txt");
            in = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
            for (int i=0;i<100;i++) {
                in = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
                IOUtils.copy(in , out);
            }



        } finally {
            in.close();
            out.close();
        }

       /* File from=new File("/home/fisher/Documents/test/billtest/20160803_00000001_001_0099902.html");

        for (int i=1;i<=20000;i++){

            String to_name="/home/fisher/Documents/test/billtest/00000001/20160803_00000001_001_"+i+".html";
            File to=new File(to_name);
            FileUtils.copyFile(from,to);

        }

*/



    }
}
