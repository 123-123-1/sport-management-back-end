package com.tongji.sportmanagement.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultMsg {
    private String msg;
    private int code;
    public ResultMsg(String msg) {
        this.msg = msg;
        this.code=200;
    }
}
