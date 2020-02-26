package com.just.book_appoint_system.exception;

import com.just.book_appoint_system.util.JsonData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Component
@ControllerAdvice
public class MyExceptionAdvice {

    public static Log log = LogFactory.getLog(MyExceptionAdvice.class);


    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MyException.class)
    public JsonData myErrorHandler(MyException ex) {

        return JsonData.buildException(500,ex.getMessage());
    }


    /**
     * 拦截捕捉自定义异常 ConstraintViolationException.class
     * @param exception
     * @return
     */
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public JsonData validateExpecetion(ValidationException exception) {
//        System.out.println("ValidationException");
//        List<String> errorMsg = new ArrayList<>();
//        if (exception instanceof ConstraintViolationException) {
//            ConstraintViolationException exs = (ConstraintViolationException) exception;
//
//            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
//            for (ConstraintViolation<?> item : violations) {
//                errorMsg.add(item.getMessage());
//            }
//        }
//
//        return JsonData.buildException(400,JSON.toJSONString(errorMsg));
//    }


    /**
     * validate异常处理
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonData exception(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        System.out.println("MethodArgumentNotValidException");
        StringBuilder sb=new StringBuilder();

        for (FieldError error : fieldErrors) {
            sb.append(error.getDefaultMessage()+" ");
        }

        return JsonData.buildException(400,sb.toString());
    }

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
//    @ExceptionHandler(value = Exception.class)
    public JsonData errorHandler(Exception ex) {

        return JsonData.buildException(500,ex.getMessage());
    }
}
