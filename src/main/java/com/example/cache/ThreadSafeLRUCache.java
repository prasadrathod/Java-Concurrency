package com.example.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeLRUCache<K,V> implements Cache<K,V> {
    private LinkNode<K, V> head, tail;
    private Map<K, LinkNode<K,V>> hashmap;

    private Lock lock = new ReentrantLock();
    private int capacity;
    public ThreadSafeLRUCache(int capacity) {
        this.capacity = capacity;
        this.head = new LinkNode<>(null, null);
        this.tail = new LinkNode<>(null, null);
        this.head.setNext(this.tail);
        this.tail.setPrev(this.head);
        this.hashmap = new ConcurrentHashMap<>();
    }

    @Override
    public V get(K key) {
        // null key not supported.
        // TODO: Throw exception with appropriate msg
        if (key == null) {
            return null;
        }
        lock.lock();
        try{
            if(!hashmap.containsKey(key)) {
                return null;
            }
            LinkNode<K,V> node = hashmap.get(key);
            removeNode(node);
            insertHead(node);
            return node.getValue();
        }
        finally {
            lock.unlock();
        }
    }

    private void insertHead(LinkNode node) {
        if(node == null) return;
        lock.lock();
        try {
            node.setNext(head.getNext());
            node.setPrev(head);

            node.getNext().setPrev(node);
            head.setNext(node);
        } finally {
            lock.unlock();
        }
    }

    /*
    * Removes current Node from Doubly Linked List
    * */
    private void removeNode(LinkNode node) {
        if(node == null) return;
        lock.lock();
        try {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        // null key/value not supported.
        // TODO: Throw exception with appropriate msg
        if(key == null || value == null) {
            return;
        }

        lock.lock();
        try{
            LinkNode<K,V> node;
            if(hashmap.containsKey(key)) {
                node = hashmap.get(key);
                removeNode(node);
                node.setValue(value);
            } else {
                node = new LinkNode<>(null, null);
                node.setKey(key);
                node.setValue(value);
                if(hashmap.size() == capacity) {
                    evictNode();
                }
                hashmap.put(key,node);
            }
            insertHead(node);
        }
        finally {
            lock.unlock();
        }
    }

    private void evictNode() {
        lock.lock();
        try {
            if(hashmap.size() == 0) return;
            LinkNode<K, V> node = tail.getPrev();
            hashmap.remove(node.getKey());
            removeNode(node);
        } finally {
            lock.unlock();
        }
    }
}
