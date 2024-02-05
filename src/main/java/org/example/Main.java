package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) raiseError(new IllegalArgumentException());
        try {
            int[] nums = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
            System.out.println(Arrays.stream(nums).sum());
        } catch (IllegalArgumentException e) {
            raiseError(e);
        } // changes
    }

    private static void raiseError(Exception e) {
        System.out.println(e.toString());
        System.exit(1);
    }
}