package com.example.cache;

public class LinkNode<K,V> {
    private LinkNode next, prev;
    K key;
    V value;

    public LinkNode(LinkNode next, LinkNode prev) {
        this.next = next;
        this.prev = prev;
    }

    public LinkNode getNext() {
        return next;
    }

    public void setNext(LinkNode next) {
        this.next = next;
    }

    public LinkNode getPrev() {
        return prev;
    }

    public void setPrev(LinkNode prev) {
        this.prev = prev;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
