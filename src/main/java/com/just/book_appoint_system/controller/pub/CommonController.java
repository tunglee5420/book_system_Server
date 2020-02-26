package com.just.book_appoint_system.controller.pub;


import com.just.book_appoint_system.domain.Classification;
import com.just.book_appoint_system.service.pub.CommonService;
import com.just.book_appoint_system.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pub")
public class CommonController {
    @Autowired
    private CommonService commonService;

    /**
     * 获得所有书本分类
     * @return
     */
    @GetMapping("/getClassifications")
    public JsonData getClassifications() {
        List classes=commonService.getClassificationList();

        return JsonData.buildSuccess(classes);
    }
}
