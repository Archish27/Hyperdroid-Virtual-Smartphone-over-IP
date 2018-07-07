package com.hyperdoid.hyperdroidbenchmark;

import java.math.BigInteger;

/**
 * Created by nikhil on 1/12/17.
 */

public class BigFact {
    public static long TestExpo(int n) {
        // Record the Current Time
        long start = System.currentTimeMillis();

        BigInteger fact = BigInteger.valueOf(1);
        for (int i = 1; i <= n; i++)
            fact = fact.multiply(BigInteger.valueOf(i));
        long end = System.currentTimeMillis();

        return (end-start);
    }
}
