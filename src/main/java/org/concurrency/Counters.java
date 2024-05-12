package org.concurrency;

public class Counters {

    private static int countUp = 0;
    private static int countDown = 20;


    private static boolean countingUpCompleted = false;


    private static final Object lock = new Object();


    private static Thread upThread = new Thread(() -> {
        synchronized (lock) {
            while (countUp < 20) {
                System.out.println("Count Up: " + ++countUp);
                try {
                    lock.notify();
                    lock.wait(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countingUpCompleted = true;
            lock.notify();
        }
    });

    // Thread for counting down
    private static Thread downThread = new Thread(() -> {
        synchronized (lock) {
            try {
                while (!countingUpCompleted) {
                    lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (countDown >= 0) {
                System.out.println("Count Down: " + countDown--);
                try {
                    lock.notify();
                    if (countDown >= 0) {
                        lock.wait(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public static void main(String[] args) {

        upThread.start();
        downThread.start();
    }
}
