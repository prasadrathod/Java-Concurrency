package com.example.queue;
public interface CustomQueue<T> {
    void enqueue(T obj) throws InterruptedException;
    T deqeue() throws InterruptedException;
}
