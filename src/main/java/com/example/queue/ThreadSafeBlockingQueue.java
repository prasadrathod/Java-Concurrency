package com.example.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeBlockingQueue<T> implements CustomQueue<T>{

    private int capacity = 0;

    private Queue<T> queue;

    private Lock lock;
    private Condition notEmpty, notFull;

    public ThreadSafeBlockingQueue(int size) {
        this.capacity = size;
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
        queue = new LinkedList<>();
    }
    @Override
    public void enqueue(T obj) throws InterruptedException {
        lock.lock();
        try {
            while(queue.size() == capacity) {
                notFull.await();
            }
            queue.add(obj);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T deqeue() throws InterruptedException{
        lock.lock();
        try {
            while(queue.size() == 0) {
                notEmpty.await();
            }
            T front = queue.poll();
            notFull.signal();
            return front;
        } finally {
            lock.unlock();
        }
    }
}
