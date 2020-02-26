package com.just.book_appoint_system.service.user.imp;

import com.just.book_appoint_system.domain.User;
import com.just.book_appoint_system.domain.UserExample;
import com.just.book_appoint_system.dto.UserDto;
import com.just.book_appoint_system.dto.UserDto1;
import com.just.book_appoint_system.mapper.UserMapper;
import com.just.book_appoint_system.service.user.UserService;
import com.just.book_appoint_system.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class UserSevriceImp implements UserService {


    @Autowired
    private UserMapper userMapper;

    private EncryptUtil encryptUtil=EncryptUtil.getInstance();
    /**
     * 根据Sid 获取用户信息
     * @param sid
     * @return
     */
    @Override
    public UserDto getUserInfo(String sid) {

        UserExample example=new UserExample();
        UserExample.Criteria criteria=example.createCriteria();
        criteria.andSchoolNumberEqualTo(sid);

        List<User>userList=userMapper.selectByExample(example);
         if(userList.size()==0){
            return null;
         }
         UserDto userDto =new UserDto(userList.get(0));//同构造参数进行转化
        return userDto;
    }

    /**
     * 更新用户信息
     * @param userDto1
     * @return
     */
    @Override
    public boolean updateUserInfo(UserDto1 userDto1) {

        User user=new User();
        user.setSchoolNumber(userDto1.getSchoolNumber());
        user.setSchool(userDto1.getSchool());
        user.setGrade(userDto1.getGrade());
        user.setPassword(encryptUtil.MD5(userDto1.getPassword()));
        user.setMajor(userDto1.getMajor());
        user.setEmail(userDto1.getEmail());
        user.setName(userDto1.getName());
        user.setId(userDto1.getId());
        user.setPhone(userDto1.getPhone());
        user.setHeadPortrait(userDto1.getHeadPortrait());

        UserExample userExample=new UserExample();
        UserExample.Criteria criteria=userExample.createCriteria();
        criteria.andDeletedEqualTo(0);
        criteria.andSchoolNumberEqualTo(user.getSchoolNumber());
        int res=userMapper.updateByExampleSelective(user,userExample);
        if(res==0){
            return false;
        }
        return true;
    }

    /**
     * 检测密码
     * @param userDto1
     * @return
     */
    @Override
    public boolean checkPassword(UserDto1 userDto1) {
        UserExample userExample=new UserExample();
        UserExample.Criteria criteria=userExample.createCriteria();
        criteria.andDeletedEqualTo(0);
        criteria.andSchoolNumberEqualTo(userDto1.getSchoolNumber());
        List<User>userList=userMapper.selectByExample(userExample);
        if(userList.size()==0){
            return false;
        }
        User user=userList.get(0);
        if(encryptUtil.MD5(userDto1.getPassword()).equals(user.getPassword())){
            return true;
        }
        return false;
    }
}
