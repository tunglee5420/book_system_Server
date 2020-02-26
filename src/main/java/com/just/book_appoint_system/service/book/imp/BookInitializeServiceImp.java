package com.just.book_appoint_system.service.book.imp;
import	java.util.ArrayList;


import com.just.book_appoint_system.domain.*;
import com.just.book_appoint_system.exception.MyException;
import com.just.book_appoint_system.mapper.BookViewMapper;
import com.just.book_appoint_system.mapper.BorrowHistoryMapper;
import com.just.book_appoint_system.service.book.BookInitializeService;
import com.just.book_appoint_system.util.RandomUtil;
import com.just.book_appoint_system.util.RedisUtils;
import com.just.book_appoint_system.util.YearUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@Service
public class BookInitializeServiceImp implements BookInitializeService {
    @Autowired
    private BookViewMapper bookViewMapper;

    @Autowired
    private BorrowHistoryMapper borrowHistoryMapper;


    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取热门书籍
     * @return
     */
    @Override
    public List<BookView> getHotBookList() {
        BookViewExample example=new BookViewExample();
        BookViewExample.Criteria criteria=example.createCriteria();
        criteria.andDeletedEqualTo(0);
        example.setOrderByClause("count desc");
        List<BookView> booklist=bookViewMapper.selectByExample(example);
        List<BookView> list=null;
        if(booklist.size()>6){
            list=booklist.subList(0,6);//取前六条

        }else {
            list=booklist;
        }

        boolean res=redisUtils.set("hotBook",list,60*60*3);
        if(!res){
            throw new MyException("缓存异常");
        }
        return list;
    }

    /**
     *最新书籍推荐
     * @return
     */
    @Override
    public List<BookView> getLatestBookList() {
        BookViewExample example=new BookViewExample();
        BookViewExample.Criteria criteria=example.createCriteria();
        criteria.andDeletedEqualTo(0);
        List<BookView> booklist=bookViewMapper.selectByExample(example);
        List<BookView> list=null;
        Collections.sort(booklist, new Comparator<BookView>() {
            @Override
            public int compare(BookView o1, BookView o2) {
                String t1=o1.getPublishTime();
                String t2=o2.getPublishTime();
                int []arr1=YearUtils.getTime(t1);
                int []arr2=YearUtils.getTime(t2);
                int k=arr1[0]-arr2[0];
                if(k==0){
                    return  arr1[1]-arr2[1];
                }
                return k;
            }
        });

        if(booklist.size()>6){
            list=booklist.subList(0,6);
        }else {
            list=booklist;
        }

        boolean res=redisUtils.set("latestBook",list,24*60*60);
        if(!res){
            throw new MyException("缓存异常");
        }
        return list;
    }

    /**
     * 获取轮播数据
     * @return
     */
    @Override
    public List<BookView> getSlideShowBookList() {
        BookViewExample example=new BookViewExample();
        BookViewExample.Criteria criteria=example.createCriteria();
        criteria.andDeletedEqualTo(0);
        List<BookView> booklist=bookViewMapper.selectByExample(example);
        List<BookView> list=new ArrayList<>();
        int size=booklist.size();
        if(size>4){
            int []arr1 =RandomUtil.getDiffNum(size,4);//生成四个不重复的随机数
            for(int i=0;i<arr1.length;i++){
                list.add(booklist.get(arr1[i]));
            }
        }else {
            list.addAll(booklist);
        }

        boolean res=redisUtils.set("slideShow",list,12*60*60);//放到缓存中
        if(!res){
            throw new MyException("缓存异常");
        }
        return list;
    }

    /**
     * 获取最近书目数据
     * @return
     */
    @Override
    public List<BookView> getRecentBookList() {
        //根据借阅时间查询借阅历史
        BorrowHistoryExample borrow=new BorrowHistoryExample();
        BorrowHistoryExample.Criteria criteria =borrow.createCriteria();
        criteria.andDeletedEqualTo(0);

        borrow.setOrderByClause("borrow_time desc");
        List<BorrowHistory> borrowHistoryList=borrowHistoryMapper.selectByExample(borrow);

        //根据记录中的id查询书籍的详细信息
        List<BookView> booklist=new ArrayList<>();
        for (BorrowHistory b:borrowHistoryList) {
            BookViewExample example=new BookViewExample();
            BookViewExample.Criteria criteria1=example.createCriteria();
            criteria1.andDeletedEqualTo(0);
            criteria1.andIdEqualTo( b.getBookId());
            List<BookView>ls=bookViewMapper.selectByExample(example);
            booklist.add(ls.get(0));
        }
        List<BookView> list=null;
        if(booklist.size()>6){
            list=booklist.subList(0,6);
        }else {
            list=booklist;
        }
        return list;


    }


}
