package com.tongji.sportmanagement.Common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tongji.sportmanagement.Common.DTO.ErrorMsg;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = ServiceException.class)
  public ResponseEntity<ErrorMsg> serviceExceptionHandler(ServiceException e){
    return ResponseEntity.status(e.getCode()).body(new ErrorMsg(e.getMessage()));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ErrorMsg> BaseExceptionHandler(Exception e){
    e.printStackTrace();
    return ResponseEntity.internalServerError().body(new ErrorMsg(e.getMessage()));
  }
}
