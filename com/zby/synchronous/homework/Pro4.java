package com.zby.synchronous.homework;

import java.util.concurrent.Semaphore;

public class Pro4 {
    public static final int N = 5;
    Semaphore empty = new Semaphore(N);  // 定义有 n 把椅子
    Semaphore rest = new Semaphore(1);
    Semaphore mutex = new Semaphore(1);
    int count = N;  // 椅子数目

    class Customer extends Thread{
        public int id;
        public Customer(int id){
            this.id = id;
        }
        @Override
        public void run() {
            try {
                mutex.acquire();
                if (count == 0){
                    mutex.release();
                    System.out.println("顾客离开理发店");
                    return;
                }
                count -= 1;
                System.out.println("顾客坐在椅子上");
                mutex.release();
                mutex.acquire();
                System.out.println("顾客前去理发");
                rest.acquire();
                count += 1;
                mutex.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Barber extends Thread{
        @Override
        public void run() {
            while (true){
                if (rest.availablePermits() == 1){
                    System.out.println("理发师前去休息");
                }else {
                    try {
                        rest.acquire();
                        System.out.println("理发师给客人理发");
                        sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Pro4 pro4 = new Pro4();
        Barber barber = pro4.new Barber();
        barber.start();
        for (int i = 0; i < 10; i++){
            Customer customer = pro4.new Customer(i);
            customer.start();
        }
    }
}
