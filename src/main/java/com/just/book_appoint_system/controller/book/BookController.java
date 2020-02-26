package com.just.book_appoint_system.controller.book;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.just.book_appoint_system.domain.BookView;
import com.just.book_appoint_system.dto.BookViewReceiveDto;
import com.just.book_appoint_system.dto.BookViewSendDto;
import com.just.book_appoint_system.service.book.BookService;
import com.just.book_appoint_system.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pub/book")
public class BookController {
    @Autowired
    private BookService bookService;
    /**
     * 根据isbn来获取书籍的详细信息
     * @param book
     * @return
     */
    @RequestMapping("/getBookListByIsbn")
    public JsonData getBookListByIsbn(@Valid @RequestBody BookViewReceiveDto book) {
        if(book.getIsbn()==null){
            return JsonData.buildError("参数出错，无法获取");
        }
        BookView bookView=bookService.getBookInfoByIsbn(book.getIsbn());
        if(bookView==null){
            return JsonData.buildError("没有此书");
        }
        return JsonData.buildSuccess(bookView);
    }

    /**
     * 检索数据（分类，作者，书名）
     * @param book
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/getBookList")
    public JsonData getBookList(@RequestBody BookViewReceiveDto book,
                                @RequestParam(value = "page",defaultValue ="1") int page,
                                @RequestParam(value = "size",defaultValue ="10")int size){

        PageHelper.startPage(page,size);//分页插件设置参数

        BookView bookView=new BookView();
        if(book.getAuthor()!=null){
            bookView.setAuthor(book.getAuthor());
        }

        bookView.setBookName(book.getBookName());
        bookView.setClassification(book.getClassification());
        List<BookViewSendDto> list=new ArrayList<>();
        List<BookView> bookViewList=bookService.getBookList(bookView);
        BookViewSendDto bookViewSendDto=null;
        for (BookView b:bookViewList) {

            bookViewSendDto=new BookViewSendDto(b);
            list.add(bookViewSendDto);
        }
        PageInfo pageInfo=new PageInfo(list);
        return JsonData.buildSuccess(pageInfo);
    }




}
