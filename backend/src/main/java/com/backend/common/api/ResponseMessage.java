package com.backend.common.api;

import com.backend.controller.LoginResponse;
import org.springframework.http.HttpStatus;

/**
 * 通信响应Response,模板类
 * @param <T>
 */
public class ResponseMessage <T>{
    //成员: 响应码code,响应信息message,响应体data
    private Integer code;
    private String message;
    private T data;


    //构造函数
    public ResponseMessage(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    //接口请求成功调用方法
    public static <T> ResponseMessage<T> success(T data){
        return new ResponseMessage<>(HttpStatus.OK.value(),"success!",data);
    }
    //请求失败调用方法
    public static <T> ResponseMessage<T> failure(Integer code,String msg,T data){
        return new ResponseMessage<>(code,msg,data);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
