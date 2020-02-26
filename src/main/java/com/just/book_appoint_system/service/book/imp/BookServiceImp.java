package com.just.book_appoint_system.service.book.imp;

import com.just.book_appoint_system.domain.*;
import com.just.book_appoint_system.dto.BookViewReceiveDto;
import com.just.book_appoint_system.mapper.BookMapper;
import com.just.book_appoint_system.mapper.BookViewMapper;
import com.just.book_appoint_system.mapper.ClassificationMapper;
import com.just.book_appoint_system.service.book.BookService;
import com.just.book_appoint_system.service.pub.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
public class BookServiceImp implements BookService {
    @Autowired
    private BookViewMapper bookViewMapper;
    @Autowired
    private CommonService commonService;

    @Autowired
    private BookMapper bookMapper;

    /**
     * 根据书本统一编号Isbn来检索书的信息
     * @param isbn
     * @return
     */
    @Override
    public BookView getBookInfoByIsbn(String isbn){

        BookViewExample bookViewExample=new BookViewExample();
        BookViewExample.Criteria criteria=bookViewExample.createCriteria();
        criteria.andIsbnEqualTo(isbn);
        List<BookView> bookViewList=bookViewMapper.selectByExample(bookViewExample);
        if(bookViewList.size()==0){
            return null;
        }
        BookView bookView=bookViewList.get(0);
        return bookView;

    }

    /**
     * 根据分类或作者或者书名来检索书籍
     * @param bookView
     * @return
     */
    @Override
    public List<BookView> getBookList(BookView bookView) {

        BookViewExample example=new BookViewExample();
        BookViewExample.Criteria criteria=example.createCriteria();
        criteria.andDeletedEqualTo(0);
        if(bookView.getBookName()!=null){
            criteria.andBookNameLike("%"+bookView.getBookName()+"%");
        }
        if(bookView.getAuthor()!=null){
            criteria.andAuthorEqualTo(bookView.getAuthor());
        }
        if(bookView.getClassification()!=null){
            criteria.andClassificationEqualTo(bookView.getClassification());
        }
        List<BookView> bookViewList=bookViewMapper.selectByExample(example);
        return bookViewList;
    }

    /**
     * 更改书的信息（包括数量）
     * @param bookView
     * @return
     */
    @Transactional
    @Override
    public boolean updateBookInfo(BookView bookView) {

        if(bookView.getIsbn()==null){
            return false;
        }
        //根据分类名取Id
        int cla=0;
        if(bookView.getClassification()!=null){
            List<Classification>clst=commonService.getClassificationList();
            for (Classification c:clst) {
                if(c.getName().equals(bookView.getClassification())){
                    cla=c.getId();
                    break;
                }
            }
        }


        //初始化要更新的数据
        Book book=new Book();
        book.setAuthor(bookView.getAuthor());
        book.setBookImgs(bookView.getBookImgs());
        book.setBookName(bookView.getBookName());
        book.setClassId(cla);
        book.setBorrowNumber(bookView.getBorrowNumber());
        book.setDescription(bookView.getDescription());
        book.setLocation(bookView.getLocation());
        book.setTotal(bookView.getTotal());
        book.setBorrowNumber(bookView.getBorrowNumber());
        book.setCount(bookView.getCount());
        book.setInventory(bookView.getInventory());
        book.setDeleted(bookView.getDeleted());

        //更新数据
        BookExample bookExample=new BookExample();
        BookExample.Criteria criteria=bookExample.createCriteria();
        criteria.andIsbnEqualTo(bookView.getIsbn());
        int res=bookMapper.updateByExampleSelective(book,bookExample);

        return res>0?true:false;
    }

    /**
     * 修改书的数量，库存量等
     * @param bookView 要更新的数据
     * @param bookView1 库存数据
     * @return
     */
    @Override
    @Transactional
    public boolean updateBookAmount(BookView bookView,BookView bookView1) {
        String isbn=bookView.getIsbn();
        if(isbn==null||bookView1==null){
            return false;
        }

        //初始化要更新的数据
        Book book=new Book();
        book.setIsbn(isbn);
        book.setBorrowNumber(bookView1.getBorrowNumber()+bookView.getBorrowNumber());//一般来讲 借数加1，库存减少1,总次数也加1
        book.setInventory(bookView1.getInventory()+bookView.getInventory());
        book.setTotal(bookView1.getTotal()+bookView.getTotal());
        book.setCount(bookView1.getCount()+bookView.getCount());
        //更新数据
        BookExample bookExample=new BookExample();
        BookExample.Criteria criteria=bookExample.createCriteria();
        criteria.andIsbnEqualTo(isbn);
        int res=bookMapper.updateByExampleSelective(book,bookExample);

        return res>0?true:false;
    }


}
