package com.leyoumall.common.advice;

import com.leyoumall.common.exception.LyException;
import com.leyoumall.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/*
拦截所有@controller注解的嘞，只要有异常就会处理
通用异常处理器
 */

@ControllerAdvice
public class CommonExceptionHandler {

    //配置拦截哪种异常   可以实现异常分类处理
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handlerExcepyion(LyException e){
        return ResponseEntity.status(e.getExceptionEnum().getCode())
                .body(new ExceptionResult(e.getExceptionEnum()));
    }
}
