import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fisher on 16-12-6.
 */
public class TEST3 {

    public static void main(String[] args) {
        LinkedBlockingQueue<String> abq = new LinkedBlockingQueue<String>();
        abq.offer("1");
        abq.offer("2");
        abq.offer("2");
        System.out.println(abq.poll());
        System.out.println(abq.poll());
        System.out.println(abq.poll());
    }

}
