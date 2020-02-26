package com.just.book_appoint_system.service.pub.imp;

import com.just.book_appoint_system.domain.User;
import com.just.book_appoint_system.domain.UserExample;
import com.just.book_appoint_system.exception.MyException;
import com.just.book_appoint_system.mapper.UserMapper;
import com.just.book_appoint_system.service.pub.LoginService;
import com.just.book_appoint_system.util.EncryptUtil;
import com.just.book_appoint_system.util.JwtUtil;
import com.just.book_appoint_system.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component
public class LoginServiceImp implements LoginService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public Map<String,Object> login(User user) {
        //下面三条语句是创建sql 查询
        UserExample userExample=new UserExample();
        UserExample.Criteria cri= userExample.createCriteria();
        cri.andSchoolNumberEqualTo(user.getSchoolNumber());
        cri.andDeletedEqualTo(0);
        List<User>list =userMapper.selectByExample(userExample);
        if(list.size()==0){
            return null;
        }
        User user1=list.get(0);
        EncryptUtil encryptUtil=EncryptUtil.getInstance();//MD5加密
        Map<String,Object>map =new HashMap<>();
        if (encryptUtil.MD5(user.getPassword()).equals(user1.getPassword())){
            String token=JwtUtil.creatJwt(user1);
            map.put("token",token);
            map.put("name",user1.getName());
            map.put("major",user1.getMajor());
            map.put("headImg",user1.getHeadPortrait());
            map.put("sid",user1.getSchoolNumber());
            map.put("permission",user1.getPermission());
            boolean res=redisUtils.set("login:"+user1.getSchoolNumber(),token,2*24*60*60);
            if(res){
                return map;
            }else {
                throw new MyException("缓存异常");
            }


        }else {
            return null;
        }

    }
}
