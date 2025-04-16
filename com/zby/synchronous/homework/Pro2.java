package com.zby.synchronous.homework;

import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Pro2 {
    Semaphore empty_1 = new Semaphore(1);
    Semaphore full_1 = new Semaphore(0);
    Semaphore empty_2 = new Semaphore(1);
    Semaphore full_2 = new Semaphore(0);

    public Pro2() {
    }

    public static void main(String[] args) {
        Pro2 pro2 = new Pro2();
        Read read = pro2.new Read();
        Copy copy = pro2.new Copy();
        Print print = pro2.new Print();
        read.start();
        copy.start();
        print.start();
    }

    class Read extends Thread {
        Read() {
        }

        public void run() {
            while(true) {
                try {
                    Pro2.this.empty_1.acquire();
                    System.out.println("从磁盘中读出了数据");
                    sleep(1500L);
                    Pro2.this.full_1.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class Copy extends Thread {
        Copy() {
        }

        public void run() {
            while(true) {
                try {
                    Pro2.this.full_1.acquire();
                    System.out.println("从缓冲区1中取出数据");
                    sleep(1000L);
                    Pro2.this.empty_1.release();
                    Pro2.this.empty_2.acquire();
                    System.out.println("将数据放入缓冲区2中");
                    sleep(1000L);
                    Pro2.this.full_2.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class Print extends Thread {
        Print() {
        }

        public void run() {
            while(true) {
                try {
                    Pro2.this.full_2.acquire();
                    System.out.println("从缓冲区2中取出数据进行打印");
                    sleep(1500L);
                    Pro2.this.empty_2.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
