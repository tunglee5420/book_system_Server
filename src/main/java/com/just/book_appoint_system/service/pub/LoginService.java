package com.just.book_appoint_system.service.pub;

import com.just.book_appoint_system.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Component
public interface LoginService {
    Map<String,Object> login(User user);
}
