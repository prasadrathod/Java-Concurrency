package com.example.queue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ThreadSafeBlockingQueueTest {

    private static Stream<Arguments> getArguments() {
        return Stream.of(
          Arguments.of(1),
          Arguments.of(10),
          Arguments.of(1000)
        );
    }
    @ParameterizedTest
    @MethodSource("getArguments")
    void testThreadSafeBlockingQueueMultiThreaded(final Integer capacity) throws InterruptedException {
        CustomQueue<Integer> q = new ThreadSafeBlockingQueue<>(capacity);

        IntStream.range(0, capacity).forEach(
                i -> {
                    CompletableFuture.supplyAsync(
                            () -> {
                                try {
                                    q.enqueue(i);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                return true;
                            }
                    );
                }
        );

        CountDownLatch latch = new CountDownLatch(capacity);
        IntStream.range(0, capacity).forEach(
                i -> {
                    CompletableFuture.supplyAsync(
                            () -> {
                                try {
                                    Integer obj = q.deqeue();
                                    System.out.println(obj);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                latch.countDown();
                                return true;
                            }
                    );
                }
        );
        latch.await();
    }
}
