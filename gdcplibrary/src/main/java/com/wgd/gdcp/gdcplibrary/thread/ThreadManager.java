package com.wgd.gdcp.gdcplibrary.thread;

import com.lzh.easythread.Callback;
import com.lzh.easythread.EasyThread;

public final class ThreadManager {

    private final static EasyThread io;
    private final static EasyThread io1;
    private final static EasyThread cache;
    private final static EasyThread calculator;
    private final static EasyThread file;

    public static EasyThread getIO () {
        return io;
    }
    public static EasyThread getIO1 () {
        return io1;
    }


    public static EasyThread getCache() {
        return cache;
    }

    public static EasyThread getCalculator() {
        return calculator;
    }

    public static EasyThread getFile() {
        return file;
    }

    static {
        //创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        cache = EasyThread.Builder.createCacheable().setName("cache").setCallback(new DefaultCallback()).build();
        //创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
        io = EasyThread.Builder.createFixed(6).setName("IO").setPriority(7).setCallback(new DefaultCallback()).build();
        io1 = EasyThread.Builder.createFixed(1).setName("IO1").setPriority(7).setCallback(new DefaultCallback()).build();

        calculator = EasyThread.Builder.createFixed(4).setName("calculator").setPriority(Thread.MAX_PRIORITY).setCallback(new DefaultCallback()).build();
        file = EasyThread.Builder.createFixed(4).setName("file").setPriority(3).setCallback(new DefaultCallback()).build();
    }

    private static class DefaultCallback implements Callback {

        @Override
        public void onError(String threadName, Throwable t) {
//            MyLog.e("Task with thread %s has occurs an error: %s", threadName, t.getMessage());
        }

        @Override
        public void onCompleted(String threadName) {
//            MyLog.d("Task with thread %s completed", threadName);
        }

        @Override
        public void onStart(String threadName) {
//            MyLog.d("Task with thread %s start running!", threadName);
        }
    }

    private void stop(){

    }

}
