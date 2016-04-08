package com.slima.marvelh19.services;

/**
 * Created by sergio.lima on 07/04/2016.
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public class MyExecutor extends ThreadPoolExecutor {


    final static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(8);
    final static int corePoolSize = 2;
    final static int maximumPoolSize = 4;
    final static long keepAliveTime = 500;

    public MyExecutor() {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(command);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }
}