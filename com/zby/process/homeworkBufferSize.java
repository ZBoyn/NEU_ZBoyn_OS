package com.zby.process;

public class homeworkBufferSize {
    private int[] buffer;
    private int front;
    private int rear;
    private int count;
    private final int size;

    public homeworkBufferSize(int k) {
        this.buffer = new int[k];
        this.front = 0;
        this.rear = 0;
        this.count = 0;
        this.size = k;
    }

    public boolean enQueue(int value) {
        if (this.isFull()) {
            return false;
        } else {
            this.buffer[this.rear] = value;
            this.rear = (this.rear + 1) % this.size;
            ++this.count;
            return true;
        }
    }

    public boolean deQueue() {
        if (this.isEmpty()) {
            return false;
        } else {
            this.front = (this.front + 1) % this.size;
            --this.count;
            return true;
        }
    }

    public int Front() {
        return this.isEmpty() ? -1 : this.buffer[this.front];
    }

    public int Rear() {
        return this.isEmpty() ? -1 : this.buffer[(this.rear - 1 + this.size) % this.size];
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public boolean isFull() {
        return this.count == this.size;
    }

    public static void main(String[] args) {
        homeworkBufferSize q = new homeworkBufferSize(3);
        System.out.println(q.enQueue(1));
        System.out.println(q.enQueue(2));
        System.out.println(q.enQueue(3));
        System.out.println(q.enQueue(4));
        System.out.println(q.Front());
        System.out.println(q.Rear());
        System.out.println(q.deQueue());
        System.out.println(q.enQueue(4));
        System.out.println(q.Rear());
    }
}
