package com.just.book_appoint_system.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用来接受前端传来的参数
 */
@Getter
@Setter
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties //json序列化时将bean中的一些属性忽略掉，序列化和反序列化都受影响。
public class BookViewReceiveDto implements Serializable {

    private Integer id;

    @JsonAlias({"book_name","bookName"})
    private String bookName;


    @NotBlank(message = "图书编号不能为空")
    @Length(max = 14,min = 9,message = "请输入正规书本编号")
    private String isbn;


    private String classification;


    private String press;

    @JsonAlias({"publish_time","publishTime"})
    private String publishTime;


    private String author;


    private String description;

    @JsonAlias({"book_imgs","bookImgs"})
    private String bookImgs;


    private Integer total;

    @JsonAlias({"borrow_number","borrowNumber"})
    private Integer borrowNumber;


    private Integer inventory;//库存


    private Integer count;


    private String location;//存放位置

}
