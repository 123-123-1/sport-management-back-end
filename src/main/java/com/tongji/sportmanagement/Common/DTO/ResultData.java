package com.tongji.sportmanagement.Common.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultData<T> {
    T data;
    Integer state;
    static public <T> ResultData<T> success(T data) {
        return new ResultData<T>(data,1);
    }

    static public <T> ResultData<T> error(T data) {
        return new ResultData<T>(data,0);
    }
}
