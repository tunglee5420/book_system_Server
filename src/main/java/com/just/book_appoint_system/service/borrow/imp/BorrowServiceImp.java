package com.just.book_appoint_system.service.borrow.imp;
import	java.util.Date;

import com.just.book_appoint_system.domain.*;
import com.just.book_appoint_system.dto.UserDto;
import com.just.book_appoint_system.exception.MyException;
import com.just.book_appoint_system.mapper.BorrowHistoryMapper;
import com.just.book_appoint_system.mapper.BorrowViewMapper;
import com.just.book_appoint_system.service.book.BookService;
import com.just.book_appoint_system.service.borrow.BorrowService;
import com.just.book_appoint_system.util.RandomUtil;
import com.just.book_appoint_system.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
@Component
public class BorrowServiceImp implements BorrowService {


    @Autowired
    private BorrowHistoryMapper borrowHistoryMapper;
    @Autowired
    private BorrowViewMapper borrowViewMapper;

    @Autowired
    private  RedisUtils redisUtils;

    @Autowired
    private BookService bookService;

    /**
     * 预约书
     * @param bookView
     * @param userDto
     * @return
     */
    @Override
    @Transactional
    public BorrowView reserveBook(BookView bookView, UserDto userDto) {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.HOUR,2);
        Date date=new Date();
        Date after =calendar.getTime();//推迟两小时
        String isbn=bookView.getIsbn();
        String orderNum=RandomUtil.generateOrderSn(isbn);//生成订单号
        //查询库存
        BookView bookView1=bookService.getBookInfoByIsbn(isbn);
        if(bookView1==null||bookView1.getInventory()<1){
            return null;
        }
        //更改库存信息
        BookView bookView2=new BookView();
        bookView2.setIsbn(isbn);
        bookView2.setBorrowNumber(1);//借出数量+1
        bookView2.setInventory(-1);//库存数量-1
        bookView2.setCount(1);//累计借阅数+1
        bookView2.setTotal(0);
        boolean res=bookService.updateBookAmount(bookView2,bookView1);

        //插入记录


        BorrowHistory borrowHistory=new BorrowHistory();
        borrowHistory.setBookId(bookView1.getId());
        borrowHistory.setUserId(userDto.getId());
        borrowHistory.setOrderNum(orderNum);
        borrowHistory.setAppointTime(date);
        borrowHistory.setStatus(1);//1表示预约成功
        borrowHistory.setDeleted(0);
        int k=borrowHistoryMapper.insertSelective(borrowHistory);


        //开始计时任务
        boolean res1=redisUtils.set("task:order"+orderNum+"isbn:"+isbn,after,2*60*60);


        if(k>0&&res&&res1){//表示插入成功
            BorrowView borrowView=new BorrowView();
            borrowView.setIsbn(isbn);
            borrowView.setSchoolNumber(userDto.getSchoolNumber());
            borrowView.setAppointTime(date);
            borrowView.setStatus(1);
            borrowView.setLocation(bookView1.getLocation());
            borrowView.setBookName(bookView1.getBookName());
            borrowView.setName(userDto.getName());
            borrowView.setOrderNum(orderNum);
            return borrowView;
        }
        return null;
    }

    /**
     * 查看借阅历史
     * @param borrowView（查询条件isbn ,ordernum,schoolnumber,status）
     * @return
     */
    @Override
    @Transactional
    public List<BorrowView> getBorrowViewList(BorrowView borrowView) {
        borrowView.setDeleted(0);
        BorrowViewExample borrowViewExample=new BorrowViewExample();
        BorrowViewExample.Criteria criteria=borrowViewExample.createCriteria();
        if(borrowView.getOrderNum()!=null){
            criteria.andOrderNumEqualTo(borrowView.getOrderNum());
        }
        if(borrowView.getSchoolNumber()!=null){
            criteria.andSchoolNumberEqualTo(borrowView.getSchoolNumber());
        }
        if(borrowView.getStatus()!=0){

            criteria.andStatusEqualTo(borrowView.getStatus());

        }
        if (borrowView.getIsbn()!=null){
            criteria.andIsbnEqualTo(borrowView.getIsbn());
        }

        criteria.andDeletedEqualTo(borrowView.getDeleted());
        borrowViewExample.or(criteria);
        borrowViewExample.setOrderByClause("appoint_time desc");
        List<BorrowView> borrowViewList=borrowViewMapper.selectByExample(borrowViewExample);

        return borrowViewList;
    }

    /**
     * 更新借阅的状态（包括预约失效，借出，归还，通过status判断）
     * @param borrowView
     * @return
     */
    @Override
    public boolean updateBorrowHistory(BorrowView borrowView) {
        if(borrowView.getOrderNum()==null||borrowView.getStatus()==0||borrowView.getIsbn()==null)
            return false;
        int status =borrowView.getStatus();
        String isbn= borrowView.getIsbn();
        String orderNum=borrowView.getOrderNum();
        BorrowHistory borrowHistory=null;
        BookView bookView2=null;
        int k=0;
        boolean res=false;
        //查询库存
        BookView bookView1=bookService.getBookInfoByIsbn(isbn);
        if(bookView1==null){
            return false;
        }
        switch (status){
            case -1://预约失效
                //更改库存信息
                bookView2=new BookView();
                bookView2.setIsbn(isbn);
                bookView2.setBorrowNumber(-1);//借出数量-1
                bookView2.setInventory(1);//库存数量1
                bookView2.setCount(-1);//累计借阅数-1
                bookView2.setTotal(0);
                res=bookService.updateBookAmount(bookView2,bookView1);

                //更新预定历史数据
                borrowHistory=new BorrowHistory();
                borrowHistory.setStatus(-1);
                borrowHistory.setFailTime(new Date());
                BorrowHistoryExample borrowHistoryExample=new BorrowHistoryExample();
                BorrowHistoryExample.Criteria criteria=borrowHistoryExample.createCriteria();
                criteria.andOrderNumEqualTo(orderNum);
                k=borrowHistoryMapper.updateByExampleSelective(borrowHistory,borrowHistoryExample);
                if(k>0&&res){
                    return true;
                }
                return false;

            case 1: //暂无
                break;
            case 2://表示借阅成功
                //更新预定历史数据
                borrowHistory=new BorrowHistory();
                borrowHistory.setStatus(2);//更新状态
                borrowHistory.setBorrowTime(new Date());//更新借阅时间
                BorrowHistoryExample borrowHistoryExample1=new BorrowHistoryExample();
                BorrowHistoryExample.Criteria criteria1=borrowHistoryExample1.createCriteria();
                criteria1.andOrderNumEqualTo(orderNum);
                k=borrowHistoryMapper.updateByExampleSelective(borrowHistory,borrowHistoryExample1);


                if(k>0){
                    redisUtils.del("task:order"+orderNum);
                    return true;
                }
                return false;
            case 3://归还成功
                //更改库存信息
                bookView2=new BookView();
                bookView2.setIsbn(isbn);
                bookView2.setBorrowNumber(-1);//借出数量-1
                bookView2.setInventory(1);//库存数量1
                bookView2.setCount(1);//累计借阅数+1
                bookView2.setTotal(0);
                res=bookService.updateBookAmount(bookView2,bookView1);

                //更新预定历史数据
                borrowHistory=new BorrowHistory();
                borrowHistory.setStatus(3);
                borrowHistory.setReturnTime(new Date());
                BorrowHistoryExample borrowHistoryExample2=new BorrowHistoryExample();
                BorrowHistoryExample.Criteria criteria2=borrowHistoryExample2.createCriteria();
                criteria2.andOrderNumEqualTo(orderNum);
                k=borrowHistoryMapper.updateByExampleSelective(borrowHistory,borrowHistoryExample2);
                if(k>0&&res){
                    return true;
                }
                return false;
            default:
                throw new MyException("参数异常");
        }
        return false;
    }


}
