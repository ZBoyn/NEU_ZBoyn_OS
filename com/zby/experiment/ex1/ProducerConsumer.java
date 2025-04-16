package com.zby.experiment.ex1;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ProducerConsumer {

    // 缓冲区类
    static class Buffer {
        private final LinkedList<Integer> buffer = new LinkedList<>();
        private final int capacity;

        // 信号量
        private final Semaphore mutex = new Semaphore(1); // 互斥锁，保护缓冲区
        private final Semaphore empty; // 表示空缓冲区数量
        private final Semaphore full;  // 表示已占用缓冲区数量

        public Buffer(int capacity) {
            this.capacity = capacity;
            this.empty = new Semaphore(capacity); // 初始化为缓冲区容量
            this.full = new Semaphore(0);        // 初始化为0，表示没有产品
        }

        // 生产者放入产品
        public void produce(int producerId, int item) throws InterruptedException {
            empty.acquire(); // 减少一个空缓冲区
            mutex.acquire(); // 进入临界区
            try {
                buffer.add(item);
                System.out.println("Producer " + producerId + " produced: " + item);
                displayBuffer(); // 显示缓冲区状态
            } finally {
                mutex.release(); // 释放临界区
                full.release();  // 增加一个占用缓冲区
            }
        }

        // 消费者取出产品
        public int consume(int consumerId) throws InterruptedException {
            full.acquire(); // 减少一个占用缓冲区
            mutex.acquire(); // 进入临界区
            try {
                int item = buffer.removeFirst();
                System.out.println("Consumer " + consumerId + " consumed: " + item);
                displayBuffer(); // 显示缓冲区状态
                return item;
            } finally {
                mutex.release(); // 释放临界区
                empty.release(); // 增加一个空缓冲区
            }
        }

        // 显示缓冲区状态
        private void displayBuffer() {
            System.out.println("Buffer state: " + buffer);
        }
    }

    // 生产者线程
    static class Producer extends Thread {
        private final Buffer buffer;
        private final int producerId;

        public Producer(Buffer buffer, int producerId) {
            this.buffer = buffer;
            this.producerId = producerId;
        }

        @Override
        public void run() {
            int item = 0;
            try {
                while (true) {
                    Thread.sleep((long) (Math.random() * 500 + 200)); // 模拟生产时间 (0.2-0.7秒)
                    buffer.produce(producerId, item++); // 生产一个产品
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // 消费者线程
    static class Consumer extends Thread {
        private final Buffer buffer;
        private final int consumerId;

        public Consumer(Buffer buffer, int consumerId) {
            this.buffer = buffer;
            this.consumerId = consumerId;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep((long) (Math.random() * 2000 + 1000)); // 模拟消费时间 (1-3秒)
                    buffer.consume(consumerId); // 消费一个产品
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        // 创建缓冲区，容量为5
        Buffer buffer = new Buffer(5);

        // 创建2个生产者和3个消费者线程
        Producer producer1 = new Producer(buffer, 1);
        Producer producer2 = new Producer(buffer, 2);
        Consumer consumer1 = new Consumer(buffer, 1);
        Consumer consumer2 = new Consumer(buffer, 2);
        Consumer consumer3 = new Consumer(buffer, 3);

        // 启动线程
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();
        consumer3.start();
    }
}