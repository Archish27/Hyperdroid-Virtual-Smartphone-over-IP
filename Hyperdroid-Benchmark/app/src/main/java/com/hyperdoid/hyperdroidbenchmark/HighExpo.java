package com.hyperdoid.hyperdroidbenchmark;

import java.math.BigInteger;

/**
 * Created by nikhil on 1/12/17.
 */

public class HighExpo {
    public static long TestExpo(int base , int power) {
        // Record the Current Time
        long start = System.currentTimeMillis();

        BigInteger answer = new BigInteger("1");
        while(power > 0)
        {
            answer = answer.multiply(new BigInteger(Integer.toString(base)));
            power--;
        }
        long end = System.currentTimeMillis();

        return (end-start);
    }
}