package com.hyperdoid.hyperdroidbenchmark;

import java.math.BigInteger;

/**
 * Created by nikhil on 1/12/17.
 */

public class NQueens {

    static int count=0 ,x[];

    public static long TestNQueens(int n) {
        // Record the Current Time
        long start = System.currentTimeMillis();
        x = new int[n+1];
        N_Queen( 1 , n );
        long end = System.currentTimeMillis();
        return (end-start);
    }
    public static void N_Queen( int k , int n )
    {
        for( int i = 1 ; i <= n ; i++ )
        {
            if( Place( k , i ) )
            {
                x[k] = i;
                if( k == n )
                {
                    System.out.println();
                    count++;
                }
                else
                    N_Queen( k+1 , n );
            }
        }
    }
    public static boolean Place( int k , int i )
    {
        for( int j = 1 ; j < k ; j++ )
            if( x[j] == i || ( Math.abs(x[j]-i) ) == (Math.abs(j-k)) )
                return false;
        return true;
    }
}
