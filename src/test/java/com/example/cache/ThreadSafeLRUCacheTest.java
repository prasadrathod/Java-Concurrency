package com.example.cache;

import org.junit.jupiter.api.Test;


import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ThreadSafeLRUCacheTest {
    private ThreadSafeLRUCache<Integer, Integer> cache;

    @Test
    public void happyPathTestSingleThread() {
        cache = new ThreadSafeLRUCache<>(5);

        IntStream.range(0,8).forEach(
                i -> cache.put(i, i)
        );

        assert(cache.get(1) == null);
        assert(cache.get(4) == 4);
    }

    @Test
    public void happyPathTestMultiThread() throws InterruptedException {
        int capacity = 50;
        ExecutorService executor = Executors.newFixedThreadPool(5);
        cache = new ThreadSafeLRUCache<>(capacity);

        CountDownLatch latch = new CountDownLatch(5);
        IntStream.range(1,6).forEach(
                i -> {
                    executor.submit(() -> {
                                for (int j = (i - 1) * 10; j < i * 10; j++) {
                                    cache.put(j, j);
                                }
                                latch.countDown();
                            }
                    );
                }
        );

        latch.await();

        IntStream.range(0, capacity).forEach(
               i -> {
                   assert (((Integer)i).equals(cache.get(i)));
               }
        );
    }
}
