package com.just.book_appoint_system.service.pub.imp;

import com.just.book_appoint_system.domain.Classification;
import com.just.book_appoint_system.domain.ClassificationExample;
import com.just.book_appoint_system.exception.MyException;
import com.just.book_appoint_system.mapper.ClassificationMapper;
import com.just.book_appoint_system.service.pub.CommonService;
import com.just.book_appoint_system.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Component
@Service
public class CommonServiceImp implements CommonService {
    @Autowired
    private ClassificationMapper classificationMapper;

    @Autowired
    private RedisUtils redis;
    /**
     * 获取所有的分类
     * @return
     */
    @Override
    public List getClassificationList() {


        List list1=(List) redis.get("classification");//先从缓存中取值，没有的话再去数据库查找
        if(list1 != null){
            return  list1;
        }
        //数据库中查找
        ClassificationExample classificationExample=new ClassificationExample();
        ClassificationExample.Criteria criteria = classificationExample.createCriteria();
        criteria.andDeletedEqualTo(0);

        List<Classification> list=classificationMapper.selectByExample(classificationExample);

        List classes=new ArrayList<>();
        for (Classification c: list) {
            c.setCreateTime(null);
            c.setDeleted(0);
            c.setUpdateTime(null);
            classes.add(c);
        }

        boolean res=redis.set("classification",classes,2*24*60*60);
        if(res){
            return  classes;
        }else {
            throw new MyException("缓存存放数据异常");
        }
    }
}
