package com.hyperdoid.hyperdroidbenchmark;

import java.util.Scanner;

/**
 * Created by nikhil on 1/12/17.
 */

public class Sorting{


    public static long TestingSorting(int n) {
        // Record the Current Time
        long start = System.currentTimeMillis();

        int Arr[] = new int[n];

        // initalization
        for( int i = 0 ; i < n ; i++ )
            Arr[i] = (n-i);

        //Sorting

        for( int i = 0 ; i < n ; i++ )
            for( int j = 0 ; j < (n-i-1) ; j++ )
                if( Arr[j+1] > Arr[j] )
                {
                    int temp;
                    temp = Arr[j+1];
                    Arr[j+1] = Arr[j];
                    Arr[j] = temp;
                }

        // Record the Endding time
        long end = System.currentTimeMillis();

        return (end-start);
    }

}
