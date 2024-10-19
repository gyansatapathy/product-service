package com.gs.product.multihreading;

public class OddEvenCounter {
    private int counter = 1;
    private final int limit;
    private final Object lock = new Object();

    public OddEvenCounter(int limit) {
        this.limit = limit;
    }

    // Thread to increment even numbers
    private class EvenThread implements Runnable {
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

    // Thread to increment odd numbers
    private class OddThread implements Runnable {
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
        Thread evenThread = new Thread(new EvenThread());
        Thread oddThread = new Thread(new OddThread());
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
        OddEvenCounter counter = new OddEvenCounter(2000);
        counter.startThreads();
    }
}
