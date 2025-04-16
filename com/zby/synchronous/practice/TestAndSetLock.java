package com.zby.synchronous.practice;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestAndSetLock {
    // 模拟硬件同步中的TestAndSet
    private static final AtomicBoolean locked = new AtomicBoolean(false);

    public boolean TestAndSet() {
        return locked.getAndSet(true);
    }

    public void unlock() {
        locked.set(false);
    }


    public static void main(String[] args) {

        TestAndSetLock lock = new TestAndSetLock();

        Runnable runnable = () -> {
            while (lock.TestAndSet()) {
                // 如果锁已经被占用, 则自旋等待
                // System.out.println(Thread.currentThread().getName() + " : 锁已被占用，正在等待...");
            }
            // critical section
            System.out.println(Thread.currentThread().getName() + " : 进入临界区");
            // 模拟临界区的操作
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                // lock = FALSE
                System.out.println(Thread.currentThread().getName() + " : 离开临界区");
                lock.unlock();
            }
        };

        // 创建多个线程测试互斥效果
        for (int i = 0; i < 10; i++) {
            new Thread(runnable, "线程-" + i).start();
        }
    }
}
