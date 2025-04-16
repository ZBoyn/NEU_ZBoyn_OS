package com.zby.experiment.ex1;

import java.util.LinkedList;

// 缓冲区类（管程核心）
class Buffer {
    private final LinkedList<String> items = new LinkedList<>();
    private final int capacity;
    private int productionCount = 0;  // 生产总量统计
    private int consumptionCount = 0; // 消费总量统计

    public Buffer(int capacity) {
        this.capacity = capacity;
    }

    // 生产方法（同步）
    public synchronized void put(String item) throws InterruptedException {
        while (items.size() == capacity) {
            System.out.println("缓冲区已满！" + Thread.currentThread().getName() + " 等待中...");
            wait();
        }

        items.add(item);
        productionCount++;
        System.out.println(Thread.currentThread().getName() + " 生产: " + item
                + " | 缓冲区大小: " + items.size()
                + " | 总生产量: " + productionCount);

        notifyAll();  // 通知所有等待线程
    }

    // 消费方法（同步）
    public synchronized String take() throws InterruptedException {
        while (items.isEmpty()) {
            System.out.println("缓冲区为空！" + Thread.currentThread().getName() + " 等待中...");
            wait();
        }

        String item = items.removeFirst();
        consumptionCount++;
        System.out.println(Thread.currentThread().getName() + " 消费: " + item
                + " | 缓冲区剩余: " + items.size()
                + " | 总消费量: " + consumptionCount);

        notifyAll();  // 通知所有等待线程
        return item;
    }
}

// 生产者类
class Producer implements Runnable {
    private final Buffer buffer;
    private final int producerId;
    private int itemCount = 0;

    public Producer(int producerId, Buffer buffer) {
        this.producerId = producerId;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String item = "生产者" + producerId + "-产品" + (++itemCount);
                buffer.put(item);
                Thread.sleep((int)(Math.random() * 1000)); // 随机生产间隔
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 消费者类
class Consumer implements Runnable {
    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.take();
                Thread.sleep((int)(Math.random() * 2000)); // 随机消费间隔
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// 主程序
public class ProducerConsumerMonitor {
    public static void main(String[] args) {
        System.out.println("======= 生产者消费者演示开始 =======");
        Buffer buffer = new Buffer(5);  // 创建容量为5的缓冲区

        // 创建2个生产者
        for (int i = 1; i <= 2; i++) {
            new Thread(new Producer(i, buffer), "生产者" + i).start();
        }

        // 创建3个消费者
        for (int i = 1; i <= 3; i++) {
            new Thread(new Consumer(buffer), "消费者" + i).start();
        }

        // 运行10秒后自动结束
        try {
            Thread.sleep(10000);
            System.out.println("\n======= 演示结束，停止所有线程 =======");
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
