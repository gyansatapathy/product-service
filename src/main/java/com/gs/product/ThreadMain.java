package com.gs.product;

public class ThreadMain {

    public static void main(String[] args) {
        Runnable r = new Counter();
        Thread t1 = new Thread(r);
        t1.setName("thread 1");
        Thread t2 = new Thread(r);
        t2.setName("thread 2");
    }
}

class Counter implements Runnable {
    private int number = 0;

    @Override
    public synchronized void run() {
        System.out.println(Thread.currentThread().getName());
        number += 1;
    }
}
