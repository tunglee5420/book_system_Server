package com.just.book_appoint_system.controller.user;

import com.just.book_appoint_system.domain.User;
import com.just.book_appoint_system.dto.UserDto;
import com.just.book_appoint_system.dto.UserDto1;
import com.just.book_appoint_system.service.user.UserService;
import com.just.book_appoint_system.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;



@RestController
@RequestMapping("/api/online/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private FileUpload fileUpload;
    /**
     * 根据Sid 获取用户信息
     * @param user
     * @return
     */

    @PostMapping("/getUserInfo")
    public JsonData getUerInfo( @RequestBody UserDto1 user){

        String sid=(String)DataThreadLocal.get();//获取token 解析的学号

        if(user.getSchoolNumber()==null){
            return JsonData.buildError("参数有误");
        }
        if(sid.equals(user.getSchoolNumber())){
            UserDto user1 = userService.getUserInfo(user.getSchoolNumber());


            if(user1==null){
                return JsonData.buildError("没有此人信息");
            }
            return JsonData.buildSuccess(user1);
        }
        return JsonData.buildError("你暂时没有权限");

    }


    @PostMapping("/updateImg")
    public JsonData updateImgs(@RequestParam("img") MultipartFile[] file){
        String sid=(String)DataThreadLocal.get();//获取token 解析的学号
        try {
            String path =fileUpload.uploadFileByFiles(file,sid);
            if(path==null||path.isEmpty()){
                return JsonData.buildError("上传失败");
            }
            User user=new User();
            user.setSchoolNumber(sid);
            user.setHeadPortrait("http://175.24.77.161/public"+path);
            UserDto1 userDto1=new UserDto1(user);
            boolean res=userService.updateUserInfo(userDto1);
            if(res){
                return JsonData.buildSuccess("http://175.24.77.161/public"+path);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonData.buildError("上传失败");


    }
    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @PostMapping("/updateUserInfo")
    public JsonData updateUserInfo(@RequestBody UserDto1 user){


        String sid=(String)DataThreadLocal.get();//获取token 解析的学号
        if(!(sid.equals(user.getSchoolNumber()))){
            return JsonData.buildError("你暂时没有权限");
        }

        if(user.getSchoolNumber()==null){
            return JsonData.buildError("参数有误");
        }
        boolean res=userService.updateUserInfo(user);
        if(res){

            return JsonData.buildSuccess("更新成功");
        }
        return  JsonData.buildError("更新失败");
    }

    /**
     * 验证用户身份密码
     * @param user
     * @return
     */
    @PostMapping("/checkPassword")
    public JsonData checkPassword(@Valid @RequestBody UserDto1 user){

        String sid=(String)DataThreadLocal.get();//获取token 解析的学号
        if(!(sid.equals(user.getSchoolNumber()))){
            return JsonData.buildError("你暂时没有权限");
        }


        boolean flag=userService.checkPassword(user);
        if(!flag){
            return  JsonData.buildError("原密码验证失败");
        }
        return  JsonData.buildSuccess("验证成功");

    }

    /**
     * 更新用户密码
     * @param user
     * @return
     */
    @PostMapping("/updatePassword")
    public JsonData updatePassword(@Valid @RequestBody UserDto1 user){

        String sid=(String)DataThreadLocal.get();//获取token 解析的学号
        if(!(sid.equals(user.getSchoolNumber()))){
            return JsonData.buildError("你暂时没有权限");
        }

//        boolean flag=userService.checkPassword(user);
//
//        if(!flag){
//            return  JsonData.buildError("原密码验证失败");
//        }
        boolean res=userService.updateUserInfo(user);

        if(res){
            redisUtils.del("login:"+user.getSchoolNumber());
            return JsonData.buildSuccess("更新成功");
        }
        return  JsonData.buildError("更新失败");
    }
}
