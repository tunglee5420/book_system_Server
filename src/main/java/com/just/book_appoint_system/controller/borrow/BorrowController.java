package com.just.book_appoint_system.controller.borrow;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.just.book_appoint_system.domain.BookView;
import com.just.book_appoint_system.domain.BorrowView;
import com.just.book_appoint_system.dto.*;
import com.just.book_appoint_system.service.book.BookService;
import com.just.book_appoint_system.service.borrow.BorrowService;
import com.just.book_appoint_system.service.user.UserService;
import com.just.book_appoint_system.util.DataThreadLocal;
import com.just.book_appoint_system.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/online/borrow")
public class BorrowController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private UserService userService;


    /**
     * 预约书籍
     * @param bookViewReceiveDto
     * @return
     */
    @PostMapping("/reserve")
    public JsonData reserve(@RequestBody BookViewReceiveDto bookViewReceiveDto){
        String sid=(String)DataThreadLocal.get();//获取token 解析的学号
        UserDto user=userService.getUserInfo(sid);//获取userId
        if(bookViewReceiveDto.getIsbn()==null){
            return JsonData.buildError("没有此书");
        }
        BookView bookView=bookService.getBookInfoByIsbn(bookViewReceiveDto.getIsbn());//检测书本Id
        if(bookView==null){
            return JsonData.buildError("没有此书");
        }
        //预定
        BorrowView borrowView=borrowService.reserveBook(bookView,user);

        if(borrowView==null){
            return JsonData.buildError("预约失败");
        }
        BorrowViewSendDto borrowViewSendDto=new BorrowViewSendDto(borrowView);
        return JsonData.buildSuccess(borrowViewSendDto);
    }

    /**
     * 查看自己预约的书
     * @param userDto1（学号必须有）
     * @return
     */
    @PostMapping("/getReservations")
    public JsonData getReservations(@RequestBody UserDto1 userDto1,
                                    @RequestParam("page") int page,
                                    @RequestParam("size")int size){
        PageHelper.startPage(page,size);
        String sid=(String)DataThreadLocal.get();//获取token 解析的学号
        if(userDto1.getSchoolNumber()==null){
            return JsonData.buildError("参数有误");
        }

        if(!sid.equals(userDto1.getSchoolNumber())){
            return JsonData.buildError("你暂时没有权限");
        }

        BorrowView borrowView=new BorrowView();
        borrowView.setSchoolNumber(sid);
        borrowView.setStatus(1);
        List<BorrowView> borrowViewList=borrowService.getBorrowViewList(borrowView);//正在预约的

        if(borrowViewList==null){
            return JsonData.buildError("没有数据");
        }
        BorrowViewSendDto borrowViewSendDto=null;
        List<BorrowViewSendDto> reservationList=new ArrayList<>();
        for (BorrowView b:borrowViewList) {
            borrowViewSendDto=new BorrowViewSendDto(b);
            reservationList.add(borrowViewSendDto);
        }

        PageInfo pageInfo=new PageInfo(reservationList);

        return JsonData.buildSuccess(pageInfo);
    }

    /**
     * 查看自己预约失效的历史
     * @param userDto1（学号必须有）
     * @return
     */
    @PostMapping("/getFailReservations")
    public JsonData getFailReservations(@RequestBody UserDto1 userDto1,
                                  @RequestParam("page") int page,
                                  @RequestParam("size")int size){
        PageHelper.startPage(page,size);
        String sid=(String)DataThreadLocal.get();//获取token 解析的学号
        if(userDto1.getSchoolNumber()==null){
            return JsonData.buildError("参数有误");
        }

        if(!sid.equals(userDto1.getSchoolNumber())){
            return JsonData.buildError("你暂时没有权限");
        }

        BorrowView borrowView=new BorrowView();
        borrowView.setSchoolNumber(sid);
        borrowView.setStatus(-1);
        List<BorrowView> borrowViewList=borrowService.getBorrowViewList(borrowView);//正在预约的

        if(borrowViewList==null){
            return JsonData.buildError("没有数据");
        }
        BorrowViewSendDto borrowViewSendDto=null;
        List<BorrowViewSendDto> reservationList=new ArrayList<>();
        for (BorrowView b:borrowViewList) {
            borrowViewSendDto=new BorrowViewSendDto(b);
            reservationList.add(borrowViewSendDto);
        }

        PageInfo pageInfo=new PageInfo(reservationList);
        return JsonData.buildSuccess(pageInfo);
    }

    /**
     * 借阅已预约的书
     * @param borrowViewReceiveDto（学号和订单号必须有）
     * @return
     */
    @PostMapping("/borrowBook")
    public JsonData borrowBook(@RequestBody BorrowViewReceiveDto borrowViewReceiveDto){
        String sid=(String)DataThreadLocal.get();//获取token 解析的学号

        if(borrowViewReceiveDto.getSchoolNumber()==null ||borrowViewReceiveDto.getOrderNum()==null){
            return JsonData.buildError("参数错误");
        }
        if(!borrowViewReceiveDto.getSchoolNumber().equals(sid)){
            return JsonData.buildError("你暂无权限");
        }
        String orderNum=borrowViewReceiveDto.getOrderNum();

        //预约过的 根据订单号取查询是否有数据数据status必须是1
        BorrowView borrowView=new BorrowView();
        borrowView.setOrderNum(orderNum);
        List<BorrowView> borrowViewList=borrowService.getBorrowViewList(borrowView);
        if(borrowViewList==null||borrowViewList.size()==0){
            return JsonData.buildError("你还没预约");
        }
        if(borrowViewList.get(0).getStatus()==-1){
            return JsonData.buildError("预约失效");
        }

        //预约修改表信息
        BorrowView borrowView1=new BorrowView();
        borrowView1.setOrderNum(orderNum);
        borrowView1.setStatus(2);
        borrowView1.setIsbn(borrowViewList.get(0).getIsbn());
        boolean res=borrowService.updateBorrowHistory(borrowView1);
        if(res){
            return JsonData.buildSuccess("借阅成功，请取书");
        }
        return JsonData.buildError("借阅失败");
    }


    /**
     * 还书
     * @param borrowViewReceiveDto（学号和订单号必须有）
     * @return
     */
    @PostMapping("/returnBook")
    public JsonData returnBook(@RequestBody BorrowViewReceiveDto borrowViewReceiveDto){
        String sid=(String)DataThreadLocal.get();//获取token 解析的学号

        if(borrowViewReceiveDto.getSchoolNumber()==null ||borrowViewReceiveDto.getOrderNum()==null){
            return JsonData.buildError("参数错误");
        }
        if(!borrowViewReceiveDto.getSchoolNumber().equals(sid)){
            return JsonData.buildError("你暂无权限");
        }
        String orderNum=borrowViewReceiveDto.getOrderNum();

        //预约过的 根据订单号取查询是否有数据数据status必须是2
        BorrowView borrowView=new BorrowView();
        borrowView.setOrderNum(orderNum);
        borrowView.setStatus(2);
        List<BorrowView> borrowViewList=borrowService.getBorrowViewList(borrowView);
        if(borrowViewList==null||borrowViewList.size()==0){
            return JsonData.buildError("你没借阅记录");
        }

        //预约修改表信息
        BorrowView borrowView1=new BorrowView();
        borrowView1.setOrderNum(orderNum);
        borrowView1.setStatus(3);
        borrowView1.setIsbn(borrowViewList.get(0).getIsbn());
        boolean res=borrowService.updateBorrowHistory(borrowView1);
        if(res){
            return JsonData.buildSuccess("还书成功");
        }
        return JsonData.buildError("还书失败");
    }

    /**
     * 查阅已经借未还的书
     * @param userDto1
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getBorrowedList")
    public JsonData getBorrowedList(@RequestBody UserDto1 userDto1,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size",defaultValue = "10")int size){
        PageHelper.startPage(page,size);//分页插件
        String sid=(String)DataThreadLocal.get();//获取token 解析的学号
        if(userDto1.getSchoolNumber()==null){
            return JsonData.buildError("参数有误");
        }
        if(!sid.equals(userDto1.getSchoolNumber())){
            return JsonData.buildError("你暂时没有权限");
        }

        BorrowView borrowView=new BorrowView();
        borrowView.setSchoolNumber(sid);
        borrowView.setStatus(2);
        List<BorrowView> borrowViewList=borrowService.getBorrowViewList(borrowView);//已经借未还的书

        if(borrowViewList==null){
            return JsonData.buildError("没有数据");
        }
        BorrowViewSendDto borrowViewSendDto=null;
        List<BorrowViewSendDto> borrowedList=new ArrayList<>();
        for (BorrowView b:borrowViewList) {
            borrowViewSendDto=new BorrowViewSendDto(b);
            borrowedList.add(borrowViewSendDto);
        }
        PageInfo<BorrowViewSendDto> pageInfo=new PageInfo<>(borrowedList);
        return JsonData.buildSuccess(pageInfo);
    }


    /**
     * 查阅已还的书
     * @param userDto1
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/getReturnList")
    public JsonData getReturnList(@RequestBody UserDto1 userDto1,
                                    @RequestParam(value = "page",defaultValue = "1") int page,
                                    @RequestParam(value = "size",defaultValue = "10")int size){
        PageHelper.startPage(page,size);//分页插件
        String sid=(String)DataThreadLocal.get();//获取token 解析的学号
        if(userDto1.getSchoolNumber()==null){
            return JsonData.buildError("参数有误");
        }
        if(!sid.equals(userDto1.getSchoolNumber())){
            return JsonData.buildError("你暂时没有权限");
        }

        BorrowView borrowView=new BorrowView();
        borrowView.setSchoolNumber(sid);
        borrowView.setStatus(3);
        List<BorrowView> borrowViewList=borrowService.getBorrowViewList(borrowView);//已经借未还的书

        if(borrowViewList==null){
            return JsonData.buildError("没有数据");
        }
        BorrowViewSendDto borrowViewSendDto=null;
        List<BorrowViewSendDto> returnList=new ArrayList<>();
        for (BorrowView b:borrowViewList) {
            borrowViewSendDto=new BorrowViewSendDto(b);
            returnList.add(borrowViewSendDto);
        }
        PageInfo<BorrowViewSendDto> pageInfo=new PageInfo<>(returnList);
        return JsonData.buildSuccess(pageInfo);
    }

}
