package com.gs.product.multithreading;

public class ThreeThreadCounter {
    private int counter = 1;
    private final int limit;
    private final Object lock = new Object();

    public ThreeThreadCounter(int limit) {
        this.limit = limit;
    }

    // Thread to increment numbers divisible 3
    private class Thread3 implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    if (counter >= limit) {
                        break;
                    }
                    if (counter % 3 == 0) {
                        System.out.println("Thread no 3: " + counter);
                        counter++;
                        lock.notifyAll(); // Notify the odd thread
                    } else {
                        try {
                            lock.wait(); // Wait for odd thread to finish
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }
    }

    // Thread to increment numbers which would leave remainder 2 when divided by 3
    private class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    if (counter >= limit) {
                        break;
                    }
                    if (counter % 3 == 2) {
                        System.out.println("Thread no 2 : " + counter);
                        counter++;
                        lock.notifyAll(); // Notify the even thread
                    } else {
                        try {
                            lock.wait(); // Wait for even thread to finish
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }
    }
    // Thread to increment numbers which would leave remainder 1 when divided by 3
    private class Thread1 implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    if (counter >= limit) {
                        break;
                    }
                    if (counter % 3 == 1) {
                        System.out.println("Thread no 1 : " + counter);
                        counter++;
                        lock.notifyAll(); // Notify the even thread
                    } else {
                        try {
                            lock.wait(); // Wait for even thread to finish
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }
    }

    public void startThreads() {
        Thread evenThread = new Thread(new Thread3());
        Thread oddThread = new Thread(new Thread2());
        Thread thread1 = new Thread(new Thread1());

        evenThread.start();
        oddThread.start();
        thread1.start();

        try {
            evenThread.join();
            oddThread.join();
            thread1.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        ThreeThreadCounter counter = new ThreeThreadCounter(2000);
        counter.startThreads();
    }
}
