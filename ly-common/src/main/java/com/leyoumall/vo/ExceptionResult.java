package com.leyoumall.vo;

import com.leyoumall.common.enums.ExceptionEnum;
import lombok.Data;
/*
异常返回结果对象
 */
@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnum exceptionEnum) {
        this.status = exceptionEnum.getCode();
        this.message = exceptionEnum.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
