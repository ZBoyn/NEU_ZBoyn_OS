package com.zby.synchronous.homework;

import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Pro1 {
    Semaphore empty = new Semaphore(1);
    Semaphore apple = new Semaphore(0);
    Semaphore orange = new Semaphore(0);

    public Pro1() {
    }

    public static void main(String[] args) {
        Pro1 pro1 = new Pro1();
        Father father = pro1.new Father();
        Son son = pro1.new Son();
        Daughter daughter = pro1.new Daughter();
        father.start();
        son.start();
        daughter.start();
    }

    class Father extends Thread {
        Father() {
        }

        public void run() {
            while(true) {
                try {
                    Pro1.this.empty.acquire();
                    if (Math.random() > (double)0.5F) {
                        System.out.println("父亲放入橘子");
                        sleep(1500L);
                        Pro1.this.orange.release();
                    } else {
                        System.out.println("父亲放入苹果");
                        sleep(1500L);
                        Pro1.this.apple.release();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class Son extends Thread {
        Son() {
        }

        public void run() {
            while(true) {
                try {
                    Pro1.this.apple.acquire();
                    System.out.println("儿子取出苹果");
                    sleep(1500L);
                    Pro1.this.empty.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class Daughter extends Thread {
        Daughter() {
        }

        public void run() {
            while(true) {
                try {
                    Pro1.this.orange.acquire();
                    System.out.println("女儿取出橘子");
                    sleep(1500L);
                    Pro1.this.empty.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
