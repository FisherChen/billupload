import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fisher on 16-12-6.
 */
public class TEST3 {

    public static void main(String[] args) {
        /*LinkedBlockingQueue<String> abq = new LinkedBlockingQueue<String>();
        abq.offer("1");
        abq.offer("2");
        abq.offer("2");
        System.out.println(abq.poll());
        System.out.println(abq.poll());
        System.out.println(abq.poll());
*/
        File dir = new File("/home/fisher/Documents/test/bill/INDEX");

        /*File[] files = dir.listFiles((FileFilter) DirectoryFileFilter.INSTANCE);
        for (int i = 0; i < files.length; i++) {
            // System.out.println(files[i].toString());
        }

        Collection<File> f = FileUtils.listFilesAndDirs(dir, new NameFileFilter("INDEX"), DirectoryFileFilter.INSTANCE);
        for (File ff : f) {
            System.out.println(ff.toString());
        }
*/

        String[] Files = dir.list(new AndFileFilter(new SuffixFileFilter(".txt"), new NotFileFilter(new SuffixFileFilter("Re.txt"))));

        for (int i = 0; i < Files.length; i++) {
            System.out.println(Files[i].toString());
        }


    }

}
