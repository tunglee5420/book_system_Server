package com.just.book_appoint_system.service.pub;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface CommonService {
    List getClassificationList();
}
