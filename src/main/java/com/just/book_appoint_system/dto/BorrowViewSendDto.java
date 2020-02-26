package com.just.book_appoint_system.dto;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.just.book_appoint_system.domain.BorrowView;
import com.just.book_appoint_system.exception.MyException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties //json序列化时将bean中的一些属性忽略掉，序列化和反序列化都受影响。
public class BorrowViewSendDto {

    @JsonIgnore
    private Integer id;


    @JsonProperty("order_num")
    private String orderNum;

    @JsonProperty("book_imgs")
    private String bookImgs;

    @JsonProperty("school_number")
    private String schoolNumber;

    private String author;
    private String name;


    private String isbn;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("book_name")
    private String bookName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("appoint_time")
    private Date appointTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("borrow_time")
    private Date borrowTime;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("return_time")
    private Date returnTime;


    @JsonProperty("fail_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date failTime;


    private String status;//状态：1表示预约成功，-1表示预约失效，2表示借出成功，3表示归还成功

    private String location;
    public BorrowViewSendDto() {
        super();
    }

    public BorrowViewSendDto(BorrowView borrowView) {
        this.id = borrowView.getId();
        this.orderNum = borrowView.getOrderNum();
        this.schoolNumber = borrowView.getSchoolNumber();
        this.name = borrowView.getName();
        this.isbn = borrowView.getIsbn();
        this.bookName = borrowView.getBookName();
        this.bookImgs=borrowView.getBookImgs();
        this.author = borrowView.getAuthor();
        this.appointTime = borrowView.getAppointTime();
        this.borrowTime = borrowView.getBorrowTime();
        this.returnTime = borrowView.getReturnTime();
        this.failTime = borrowView.getFailTime();

        this.location = borrowView.getLocation();
        switch (borrowView.getStatus()){
            case -1:
                this.status = "预约失效";
                break;
            case 1:
                this.status="预约成功";
                break;
            case 2:
                this.status="借阅成功";
                break;
            case 3:
                this.status="归还成功";
                break;
            default:
                throw new MyException("参数异常");

        }

    }
}
