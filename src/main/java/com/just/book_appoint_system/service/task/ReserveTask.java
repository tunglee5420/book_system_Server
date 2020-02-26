package com.just.book_appoint_system.service.task;

import com.just.book_appoint_system.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 预定系统
 */
//@Component
public class ReserveTask {

    @Autowired
    private RedisUtils redisUtils;
    //遍历可删除任务链
    private  List<String> taskList=new CopyOnWriteArrayList<>();


    //取出redis任务列表初始化taskList
    public ReserveTask() {
        super();
        List list= (List) redisUtils.get("task");
        if(list!=null){
            taskList.addAll(list);
        }
    }

    /**
     * 获取任务列表
     * @return
     */
    public List<String> getTaskList(){
        return taskList;
    }

    //加入任务
    public boolean addTaskList(String orderNum,Date date){
        taskList.add(orderNum);
        boolean res=redisUtils.set("task",taskList);
        boolean res1=redisUtils.set(orderNum,date,2*60*60);
        return res&&res1;

    }
    //移除任务
    public boolean removeTaskList(String orderNum){
        boolean res=false;
        for (String str : taskList) {
            if (orderNum.equals(str)) {
                res=taskList.remove(str);
                redisUtils.del(orderNum);
                break;
            }

        }
        return res;
    }

    /**
     * 获取任务
     * @param orderNum
     * @return
     */
    public Date getTask(String orderNum){
        Date date= (Date) redisUtils.get(orderNum);
        return date;
    }






}
