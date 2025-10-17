package com.backend.controller;

import com.backend.common.api.ResponseMessage;
import com.backend.common.api.TokenResponseMessage;
import com.backend.dto.UserDto;
import com.backend.service.IUserService;
import com.backend.utils.JwtUtil; // 导入 JwtUtil

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager; // 导入 AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.backend.common.exceptions.AuthFailureException;

/**
 * 用户认证控制层:注册,登录,重置密码
 */
@RestController
@RequestMapping("/auth")
public class UserAuthController {
    @Autowired
    IUserService user_service;

    // 注入Authentication Manager
    @Autowired
    private AuthenticationManager authenticationManager; // 用于认证

    @Autowired
    private JwtUtil jwtUtil; // 用于生成 Token

    /**
     * 用户/管理员 注册
     * 如果验证成功,用JwtUtil生成token,将token和用户信息一起返回
     * @param usr_new
     * @return
     */
    @PostMapping("/register")
    public ResponseMessage<UserDto>registerUser(@Validated @RequestBody UserDto usr_new){
        UserDto user_resp=user_service.registerUser(usr_new);
        return ResponseMessage.success(user_resp);

    }

    /**
     * 用户/管理员 登录
     * @param usr
     * @return
     */
    @PostMapping
    public TokenResponseMessage loginUser(@Validated @RequestBody UserDto usr){
        try {
            // 1. 使用 AuthenticationManager 进行身份验证
            // 它会调用我们配置的 UserDetailsService 和 PasswordEncoder 进行验证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usr.getUsername(), // 用户名
                            usr.getPassword()  // 明文密码
                    )
            );

            // 2. 身份验证成功，获取用户名
            String username = authentication.getName();

            // 3. 从数据库重新加载完整的用户信息
            UserDto user_resp = user_service.getUserByUsername(username);

            // 4. 生成 JWT Token
            String token = jwtUtil.generateToken(username);

            // 5. 返回 Token 和用户信息
            LoginResponse response = new LoginResponse(token, user_resp);
            return TokenResponseMessage.success(response);

        } catch (AuthenticationException e) {
            // 认证失败，捕获异常并抛出自定义异常
            throw new AuthFailureException("用户名或密码不正确！");
        }
    }

    /**
     * 忘记密码,修改密码
     * @param usr_modified
     * @return
     */
    @PutMapping
    public ResponseMessage<UserDto>modifyPassword(@Validated @RequestBody UserDto usr_modified){
        UserDto user_resp=user_service.modifyPassword(usr_modified);
        return ResponseMessage.success(user_resp);
    }
}
