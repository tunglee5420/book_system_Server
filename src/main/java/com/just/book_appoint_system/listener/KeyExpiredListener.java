package com.just.book_appoint_system.listener;


import java.nio.charset.StandardCharsets;

import com.just.book_appoint_system.domain.BorrowView;
import com.just.book_appoint_system.service.borrow.BorrowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyExpiredListener.class);
    @Autowired
    private BorrowService borrowService;

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 监听redis中失效的key
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        //String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        //过期的key
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        if(key.startsWith("task:order")){
            String order = key.substring(10,20);
            String isbn=key.substring(25);
            System.out.println(order+" "+isbn);
            BorrowView borrowView=new BorrowView();
            borrowView.setOrderNum(order);
            borrowView.setStatus(-1);
            borrowView.setIsbn(isbn);
            borrowService.updateBorrowHistory(borrowView);

        }

    }
}
