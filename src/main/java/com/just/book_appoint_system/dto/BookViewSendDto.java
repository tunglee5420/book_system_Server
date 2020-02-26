package com.just.book_appoint_system.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.just.book_appoint_system.domain.BookView;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Getter
@Setter
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties //json序列化时将bean中的一些属性忽略掉，序列化和反序列化都受影响。
public class BookViewSendDto implements Serializable {
    @JsonIgnore
    private Integer id;

    @JsonProperty("book_name")
    private String bookName;


    @NotNull
    private String isbn;


    private String classification;


    private String press;


    @JsonProperty("publish_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String publishTime;


    private String author;


    private String description;

    @JsonProperty("book_imgs")
    private String bookImgs;


    private Integer total;


    @JsonProperty("borrow_number")
    private Integer borrowNumber;



    private Integer inventory;//库存


    private Integer count;


    private String location;//存放位置


    public BookViewSendDto() {
        super();
    }

    public BookViewSendDto(BookView bookView) {
        this.bookName = bookView.getBookName();
        this.isbn = bookView.getIsbn();
        this.classification = bookView.getClassification();
        this.press = bookView.getPress();
        this.publishTime = bookView.getPublishTime();
        this.author = bookView.getAuthor();
        this.description = bookView.getDescription();
        this.bookImgs = bookView.getBookImgs();
        this.total = bookView.getTotal();
        this.borrowNumber = bookView.getBorrowNumber();
        this.inventory = bookView.getInventory();
        this.count = bookView.getCount();
        this.location = bookView.getLocation();
    }
}
