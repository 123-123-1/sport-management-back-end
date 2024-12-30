package com.tongji.sportmanagement.Common.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultMsg {
    private String msg;
    private int state;
    static public ResultMsg success(String msg) {
        return new ResultMsg(msg,1);
    }
    static public ResultMsg error(String msg) {
        return new ResultMsg(msg,0);
    }
}
