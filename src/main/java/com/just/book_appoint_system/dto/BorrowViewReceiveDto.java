package com.just.book_appoint_system.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@ToString
@Getter
@Setter
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties //json序列化时将bean中的一些属性忽略掉，序列化和反序列化都受影响。
public class BorrowViewReceiveDto implements Serializable {

    private Integer id;

    @Length(max = 10,min = 10,message = "订单号为10位")
    @NotBlank(message = "订单号不能为空")
    @JsonAlias({"orderNum","order_num"})
    private String orderNum;

    @NotBlank(message = "学号不能为空")
    @JsonAlias({"school_number","schoolNumber"})
    @Length(min=12, max = 13, message = "学号长度在12到13个字符之间")
    private String schoolNumber;

    private String author;
    private String name;

    @Length(max = 10,min = 10,message = "订单号为10位")
    private String isbn;

    @JsonAlias({"book_imgs","bookImgs"})
    private String bookImgs;

    @JsonAlias({"book_name","bookName"})
    private String bookName;

    @JsonIgnore
    @JsonAlias({"appoint_time","appointTime"})
    private Date appointTime;

    @JsonIgnore
    @JsonAlias({"borrow_time","borrowTime"})
    private Date borrowTime;

    @JsonIgnore
    @JsonAlias({"return_time","returnTime"})
    private Date returnTime;

    @JsonIgnore
    @JsonAlias({"fail_time","failTime"})
    private Date failTime;

    @JsonIgnore
    private Integer status;//状态：1表示预约成功，-1表示预约失效，2表示借出成功，3表示归还成功

    @JsonIgnore
    private String location;

}
