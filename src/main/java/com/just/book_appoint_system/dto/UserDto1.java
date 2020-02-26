package com.just.book_appoint_system.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.just.book_appoint_system.domain.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 由于接受前端发来的数据
 */
@Getter
@Setter
@ToString
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties //json序列化时将bean中的一些属性忽略掉，序列化和反序列化都受影响。
public class UserDto1 implements Serializable {

    private Integer id;


    @NotBlank(message = "学号不能为空")
    @Length(min=12, max = 13, message = "学号长度在12到13个字符之间")
    @JsonAlias({"school_number","schoolNumber"})
    private String schoolNumber;


    private String school;

    private String grade;


    private String major;


    private String name;


    @Pattern(regexp = "^((13[0-9])|(14[0-9])|(15([0-9]))|(17[013678])|(18[0-9]))\\\\d{8}$")
    private String phone;

    @Email
    private String email;

    @NotBlank(message = "密码不能为空")
    @Length(min=6, message = "密码长度大于6")
    private String password;


    private Integer permission;

    @JsonAlias({"head_portrait","headPortrait"})
    private String headPortrait;


    private UserDto1(){super();}

    public UserDto1(User user) {
        this.id = user.getId();
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
