package com.leyoumall.common.exception;

import com.leyoumall.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LyException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

}
