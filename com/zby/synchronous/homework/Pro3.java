package com.zby.synchronous.homework;

import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Pro3 {
    Semaphore resource = new Semaphore(100);
    Semaphore mutex = new Semaphore(1);

    public Pro3() {
    }

    public static void main(String[] args) {
        Pro3 pro3 = new Pro3();

        for(int i = 1; i <= 120; ++i) {
            Reader reader = pro3.new Reader(i);
            reader.start();
        }

    }

    class Reader extends Thread {
        private final int id;

        Reader(int id) {
            this.id = id;
        }

        public void run() {
            while(true) {
                try {
                    Pro3.this.mutex.acquire();
                    System.out.println("读者 " + this.id + " 正在登记...");
                    sleep(1000L);
                    Pro3.this.mutex.release();
                    Pro3.this.resource.acquire();
                    System.out.println("读者 " + this.id + " 获得了一个座位。");
                    System.out.println("读者 " + this.id + " 正在读书...");
                    sleep(2000L);
                    System.out.println("读者 " + this.id + " 离开了座位。");
                    Pro3.this.resource.release();
                    Pro3.this.mutex.acquire();
                    System.out.println("读者 " + this.id + " 正在注销...");
                    sleep(1000L);
                    Pro3.this.mutex.release();
                    sleep(2000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
