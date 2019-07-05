package com.example.sqlitetest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @创建者 李国赫
 * @创建时间 2019-07-04 10:17
 * @描述
 */
public class ThreadPoolUtil {

    private ThreadPoolUtil() { }

    private static int CORE_POOL_SIZE = 5;

    private static int MAX_POOL_SIZE = 10;

    private static int KEEP_ALIVE_TIME = 10000;

    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);

    private static ThreadPoolExecutor threadPool;
    private static ThreadFactory      threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "myThreadPool thread:"
                    + integer.getAndIncrement());
        }
    };

    static {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
    }

    /**
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        try {
            threadPool.execute(runnable);
        }
        catch(RejectedExecutionException e) { }
    }

}
