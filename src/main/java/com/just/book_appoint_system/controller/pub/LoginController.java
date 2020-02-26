package com.just.book_appoint_system.controller.pub;

import com.just.book_appoint_system.domain.User;

import com.just.book_appoint_system.dto.UserDto;
import com.just.book_appoint_system.dto.UserDto1;
import com.just.book_appoint_system.service.pub.LoginService;
import com.just.book_appoint_system.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping("/api/pub")
//@Validated
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public JsonData login(@Valid @RequestBody UserDto1 user) {

        User user1=new User();
        user1.setSchoolNumber(user.getSchoolNumber());
        user1.setPassword(user.getPassword());
        Map<String,Object> res=loginService.login(user1);

        if(res!=null){
            return JsonData.buildSuccess(res);
        }
        return JsonData.buildError("登录失败");
    }

}
