package com.just.book_appoint_system.util;


import com.just.book_appoint_system.dto.UserDto1;
import org.springframework.stereotype.Component;

/**
 * 用于存放数据
 */
@Component
public class DataThreadLocal {

    //把构造函数私有，外面不能new，只能通过下面两个方法操作
    private DataThreadLocal(){
    }

    private static final ThreadLocal<Object> LOCAL = new ThreadLocal<>();

    public static void set(Object obj){
        LOCAL.set(obj);
    }

    public static Object get(){
        return LOCAL.get();
    }
}
