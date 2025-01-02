package com.example.cache;

public interface Cache<K,V> {
    /*
    * Get key from cache if exists
    * Returns null if key id not present in cache
    */
    V get(K key);

    /*
    * Insert the key K and value V in cache if key is not present.
    * If key already exists, update the value V against key K.
    * If capacity of cache is full, evict the entry from existing cache based on eviction policy.
    * Policy implementation is upto Dev while implementing this interface.
    */
    void put(K key, V value);
}
