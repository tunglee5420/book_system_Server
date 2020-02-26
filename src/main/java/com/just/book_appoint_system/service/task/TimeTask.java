package com.just.book_appoint_system.service.task;

import com.just.book_appoint_system.domain.BookView;
import com.just.book_appoint_system.service.book.BookInitializeService;
import com.just.book_appoint_system.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TimeTask {

    @Autowired
    private BookInitializeService bookService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 定时初始化热门书籍
     */
    @Scheduled(fixedRate =1000*60*60*3)//每隔三个小时更新热门书籍
    public void hotInitialize() {
       List<BookView>list= bookService.getHotBookList();
//        for (BookView book:  list) {
//            System.out.println(book);
//        }

    }

    @Scheduled(cron = "0 0 0 * * ?")//每天晚上0点00分执行一次热门书籍更换
    public void latestInitialize() {
        List<BookView>list= bookService.getLatestBookList();
//        for (BookView book:  list) {
//            System.out.println(book.getPublishTime());
//        }
    }
//        @Scheduled(fixedRate =1000*60*60*3)//每隔三个小时更新热门书籍


    @Scheduled(cron = "0 0 6,17 * * ?")//每6点，17点更换一次轮播图展示书籍
    public void slideShowInitialize() {
        List<BookView>list= bookService.getSlideShowBookList();

    }
}
