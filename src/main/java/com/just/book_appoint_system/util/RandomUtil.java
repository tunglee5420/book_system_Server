package com.just.book_appoint_system.util;



import java.util.Calendar;
import java.util.Random;


public class RandomUtil {
    /**
     * 生成count个小于seed的不同的随机数
     * @param seed
     * @param count
     * @return
     */
    public static int[] getDiffNum(int seed,int count){
        int n = seed;
        if(n<count){
            throw new RuntimeException("生成个数大于种子数，不能生成");
        }
        Random rand = new Random();
        int []arr=new int[count];
        boolean[] bool = new boolean[n];
        int randInt = 0;

        for(int i = 0; i < count ; i++) {
            do {
                randInt = rand.nextInt(n);
            }while(bool[randInt]);
            bool[randInt] = true;
            arr[i]=randInt;
        }
        return arr;
    }


    /**
     * @param isbn: 书本同意编号
     * 生成订单编号
     * 订单编号规则：(10位)：(年末尾*月，取后2位)+（ISBN后三位%3.33*日取整后2位）+(timestamp*10000以内随机数，取前6位)
     * @return
     */
    public static String generateOrderSn(String isbn){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        year = year % 10;
        if(year == 0) year = 10;
        int month = calendar.get(Calendar.MONTH)+1;
        int yearMonth  =  year * month;
        String yearMonthPart = "0"+yearMonth;
        yearMonthPart = yearMonthPart.substring(yearMonthPart.length() - 2 );

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int id=Integer.parseInt(isbn.substring(isbn.length() -3));
        int dayNum = (int)((id % 3.33) * day);
        String dayPart = "0"+dayNum;
        dayPart = dayPart.substring(dayPart.length() - 2);

        String timestampPart = ""+(Math.random() * 10000) * (System.currentTimeMillis()/10000);
        timestampPart = timestampPart.replace(".", "").replace("E", "");
        timestampPart = timestampPart.substring(0,6);
        return yearMonthPart+dayPart+timestampPart;
    }
}
