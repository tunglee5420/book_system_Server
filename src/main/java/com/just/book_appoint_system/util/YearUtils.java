package com.just.book_appoint_system.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class YearUtils {
    public static int[] getTime(String time){
//        System.out.println(startTime);
        String []arr=time.trim().split("\\D");
//        System.out.println(Arrays.toString(arr));
        int year = Integer.parseInt(arr[0]);
//        System.out.println(year);
        int month = Integer.parseInt(arr[1]) ;
//        System.out.println(month);


        return new int[]{year,month};

    }

}
