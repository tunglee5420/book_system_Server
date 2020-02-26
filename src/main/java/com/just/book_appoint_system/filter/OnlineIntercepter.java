package com.just.book_appoint_system.filter;

import com.alibaba.fastjson.JSON;

import com.just.book_appoint_system.util.DataThreadLocal;
import com.just.book_appoint_system.util.JsonData;
import com.just.book_appoint_system.util.JwtUtil;
import com.just.book_appoint_system.util.RedisUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class OnlineIntercepter implements HandlerInterceptor {
    /**
     * 进入controller 之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */

    @Autowired
    RedisUtils redisUtils;


    private static Logger logger = LogManager.getLogger(OnlineIntercepter.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token= request.getHeader("token");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if(token==null||token.equals("")){

            printJson(response,-1,"token 为空，请登陆！");
            return false;
        }


        if(JwtUtil.checkJWT(token)!=null){
            String sid= (String) JwtUtil.checkJWT(token).get("sid");
            if(redisUtils.get("login:"+sid)!=null){
               String token1= (String)redisUtils.get("login:"+sid);
               if(token1.equals(token)){
                   //将用户信息存入UserThreadLocal
                   DataThreadLocal.set(sid);
                   return true;
               }
               printJson(response,-1,"第三方登陆,token失效");
               return false;

            }
            printJson(response,-1,"token解析验证失败");
            return false;
        }

        printJson(response,-1,"token过期,请重新登陆");
        return false;
    }

    /**
     * 调用controller之后，视图渲染之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 完成之后，用于资源清理
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //将DataThreadLocal中的信息清除
        System.out.println("=====================完成============================");
        DataThreadLocal.set(null);
    }

    static void printJson(HttpServletResponse response, int code, String message) {
        JsonData responseResult = new JsonData(code,false,message);
        String content = JSON.toJSONString(responseResult);
        printContent(response, content);
    }
    static void printContent(HttpServletResponse response, String content) {
        try {
            response.reset();
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "no-store");
            response.setCharacterEncoding("UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(content);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
