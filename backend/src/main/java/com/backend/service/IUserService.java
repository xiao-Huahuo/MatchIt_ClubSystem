package com.backend.service;

import com.backend.dto.UserDto;

/**
 * 用户服务层接口
 */
public interface IUserService {
    public UserDto registerUser(UserDto usrNew);

    public UserDto getUserByUsername(String username);

    public UserDto modifyPassword(UserDto usrModified);
}
