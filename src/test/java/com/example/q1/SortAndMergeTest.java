package com.example.q1;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class SortAndMergeTest {
    private final SortAndMerge sortAndMerge = new SortAndMerge();

    @ParameterizedTest
    @MethodSource("getInputArrays")
    public void testSortAndMergeUsingMultiThread(final List<Integer> arrayList) throws InterruptedException {
        final List<Integer> resultArrayList = sortAndMerge.sortAndMergeArrayUsingThreads(arrayList);
        System.out.println(resultArrayList.toString());
    }

    @ParameterizedTest
    @MethodSource("getInputArrays")
    public void testSortAndMergeUsingCompletableFuture(final List<Integer> arrayList) throws InterruptedException, ExecutionException {
        final List<Integer> resultArrayList = sortAndMerge.sortAndMergeArrayUsingCompletableFuture(arrayList);
        System.out.println(resultArrayList.toString());
    }

    private static Stream<Arguments> getInputArrays() {
        return Stream.of(
                Arguments.of(Arrays.asList(2, 7, 9, 11, 8, 6, 4, 1)),
                Arguments.of(Arrays.asList(11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1)),
                        Arguments.of(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8,9,10,11))
        );
    }
}
