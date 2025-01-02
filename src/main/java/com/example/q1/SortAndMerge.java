package com.example.q1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SortAndMerge {
    public SortAndMerge() {
    }

    public List<Integer> sortAndMergeArrayUsingThreads(List<Integer> inputArray) throws InterruptedException {
        if(inputArray == null || inputArray.size() == 0) return Collections.emptyList();

        final PickAndSorter evenArray = new PickAndSorter(inputArray, false);
        final PickAndSorter oddArray = new PickAndSorter(inputArray, true);

        Thread evenThread = new Thread(evenArray);
        Thread oddThread = new Thread(oddArray);

        evenThread.start();
        oddThread.start();

        evenThread.join();
        oddThread.join();

        final List<Integer> outputSortedArray = Stream.concat(evenArray.getOutputArray().stream(), oddArray.getOutputArray().stream()).
                collect(Collectors.toList());
        return outputSortedArray;
    }

    public List<Integer> sortAndMergeArrayUsingCompletableFuture(List<Integer> inputArray) throws ExecutionException, InterruptedException {
        final CompletableFuture<List<Integer>> future1 =
                CompletableFuture.supplyAsync(
                        () -> {
                            // even filter and sort
                            return inputArray.stream().filter(
                                    i -> i % 2 == 0
                            ). sorted().collect(Collectors.toList());
                        }
                ).thenCombine(
                        CompletableFuture.supplyAsync(
                        () -> {
                            // odd filter and sort
                            return inputArray.stream().filter(
                                    i -> i % 2 != 0
                            ). sorted().collect(Collectors.toList());
                        }),
                        (evenSorted, oddSorted) -> {
                            return Stream.concat(evenSorted.stream(), oddSorted.stream()).collect(
                                    Collectors.toList()
                            );
                        }
                );
        return future1.get();
    }
}