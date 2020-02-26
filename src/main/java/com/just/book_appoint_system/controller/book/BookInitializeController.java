package com.just.book_appoint_system.controller.book;

import com.just.book_appoint_system.service.book.BookInitializeService;
import com.just.book_appoint_system.util.JsonData;
import com.just.book_appoint_system.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/pub/book")
@ResponseBody
@RestController
public class BookInitializeController {
    @Autowired
    private BookInitializeService bookInitializeService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取热门书籍
     * @return
     */
    @RequestMapping("/getHotBookList")
    public JsonData getHotBookList(){
        List list= (List) redisUtils.get("hotBook");

        if(list!=null){
            return JsonData.buildSuccess(list);
        }

        list=bookInitializeService.getHotBookList();

        return JsonData.buildSuccess(list);

    }

    /**
     * 获取最新书籍
     * @return
     */
    @RequestMapping("/getlatestBookList")
    public JsonData getlatestBookList(){
        List list= (List) redisUtils.get("latestBook");

        if(list!=null){
            return JsonData.buildSuccess(list);
        }
        list=bookInitializeService.getLatestBookList();

        return JsonData.buildSuccess(list);

    }

    /**
     * 获取轮播图书籍
     * @return
     */
    @RequestMapping("/getSlideShowBookList")
    public JsonData getSlideShowBookList(){
        List list= (List) redisUtils.get("slideShow");
        if(list!=null){
            return JsonData.buildSuccess(list);
        }
        list=bookInitializeService.getSlideShowBookList();

        return JsonData.buildSuccess(list);

    }

    /**
     * 获取最近全网阅读书籍
     * @return
     */
    @RequestMapping("/getRecentBookList")
    public JsonData getRecentBookList(){
        List list=bookInitializeService.getRecentBookList();
        return JsonData.buildSuccess(list);

    }


}
