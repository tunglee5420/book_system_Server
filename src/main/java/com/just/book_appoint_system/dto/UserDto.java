package com.just.book_appoint_system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.just.book_appoint_system.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;


/**
 * 用于返回数据
 */
@Getter
@Setter
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties //json序列化时将bean中的一些属性忽略掉，序列化和反序列化都受影响。
public class UserDto implements Serializable {
    @JsonIgnore
    private Integer id;


    @JsonProperty("school_number")
    private String schoolNumber;


    private String school;

    private String grade;


    private String major;


    private String name;


    private String phone;

    private String email;

    @JsonIgnore
    private String password;


    private Integer permission;


    @JsonProperty("head_portrait")
    private String headPortrait;


    private UserDto(){super();}

    public UserDto(User user) {
        this.id=user.getId();
        this.schoolNumber = user.getSchoolNumber();
        this.school = user.getSchool();
        this.grade = user.getGrade();
        this.major = user.getMajor();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.permission = user.getPermission();
        this.headPortrait = user.getHeadPortrait();
    }

}
