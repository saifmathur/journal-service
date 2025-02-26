package com.journal.journal_service;

import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args){
        System.out.println(fibo(40));
    }

    public static int fibo(int n) {
        Map<Integer, Integer> memo = new HashMap<>();
        if(n<=1) return n;
        if(memo.containsKey(n)) return memo.get(n);

        int result = (fibo(n-1)) + fibo(n-2);
        memo.put(n, result);
        return result;
    }
}
