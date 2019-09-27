package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is part of the "production" code. It calls the rewritten class.
 */
public class Threadbog {
    private static final Logger logger = LoggerFactory.getLogger(Threadbog.class);

    public void crash() throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        logger.info("Calling Thread#start()");
        t.start();
        Thread.sleep(100);
        logger.info("Calling Thread#stop()");
        t.stop();
        logger.info("end.");
    }
}
