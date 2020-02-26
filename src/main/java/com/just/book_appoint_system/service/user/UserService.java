package com.just.book_appoint_system.service.user;

import com.just.book_appoint_system.dto.UserDto;
import com.just.book_appoint_system.dto.UserDto1;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@Component
public interface UserService {

    UserDto getUserInfo(String sid);
    boolean updateUserInfo(UserDto1 userDto1);
    boolean checkPassword(UserDto1 userDto1);
}
