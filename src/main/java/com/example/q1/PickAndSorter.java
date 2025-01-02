package com.example.q1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PickAndSorter implements Runnable{
    private List<Integer> inputArray, outputArray;
    private boolean isOdd = true;

    public PickAndSorter(List<Integer> inputArray, boolean isOdd) {
        this.inputArray = inputArray;
        this.isOdd = isOdd;
    }

    public List<Integer> getOutputArray() {
        return outputArray;
    }

    @Override
    public void run() {
        if(inputArray == null || inputArray.size() == 0) return;
        outputArray = inputArray.stream().filter(
                isOdd ? (i -> i % 2 != 0):
                        (i -> i % 2 == 0)
        ).sorted().
                collect(Collectors.toList());
    }
}
