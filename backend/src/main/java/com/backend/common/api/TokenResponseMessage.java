package com.backend.common.api;

import com.backend.controller.LoginResponse;
import com.backend.dto.UserDto;
import org.springframework.http.HttpStatus;

/**
 * 针对于用户认证(带有token)的通信响应Response,附加了token字段
 * @param
 */
public class TokenResponseMessage {
    //成员: 响应码code,响应信息message,响应体data
    private Integer code;
    private String message;
    private UserDto data;
    private String token;//添加了token字段

    //构造函数
    public TokenResponseMessage(Integer code, String message, UserDto data, String token) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    //构造函数2号,直接接收LoginResponse并拆解
    public TokenResponseMessage(Integer code, String message, LoginResponse response) {
        this.code = code;
        this.message = message;
        this.data = response.getUser();
        this.token = response.getToken();
    }

    //接口请求成功调用方法
    public static TokenResponseMessage success(LoginResponse response){
        return new TokenResponseMessage(HttpStatus.OK.value(),"success!",response);
    }
    //请求失败调用方法
    public static TokenResponseMessage failure(Integer code,String msg,LoginResponse response){
        return new TokenResponseMessage(code,msg,response);
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

    public UserDto getData() {
        return data;
    }

    public void setData(UserDto data) {
        this.data = data;
    }
}

