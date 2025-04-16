package com.zby.synchronous.practice;

import java.util.concurrent.atomic.AtomicBoolean;

public class SwapLock {
    private final AtomicBoolean lock = new AtomicBoolean(false);

    // 交换锁
/*
    private void Swap(AtomicBoolean lock, AtomicBoolean key) {
        boolean temp = lock.get(); // 获取lock的当前值
        lock.set(key.get());        // 将lock设置为key的值
        key.set(temp);              // 将key设置为原lock的值
    }
*/

    private void Swap(AtomicBoolean key) {
        boolean temp = lock.getAndSet(key.get());
        key.set(temp);
    }

    public void lock(AtomicBoolean key) {
        while (key.get()) {
            Swap(key);
        }
    }

    public void unlock() {
        lock.set(false);
    }

    public static void main(String[] args) {
        SwapLock swapLock = new SwapLock();
        Runnable runnable = () -> {
            // 每个线程维护自己的key
            AtomicBoolean key = new AtomicBoolean(true);
            swapLock.lock(key);
            // critical section
            System.out.println(Thread.currentThread().getName() + " : 进入临界区");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                // lock = FALSE
                // 离开临界区，释放锁
                System.out.println(Thread.currentThread().getName() + " : 离开临界区");
                swapLock.unlock();
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(runnable, "线程-" + i).start();
        }
    }
}