package com.backend.service;

import com.backend.common.constants.Constants;
import com.backend.dto.UserDto;
import com.backend.pojo.User;
import com.backend.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户服务层
 */
@Service
public class UserService implements IUserService{
    @Autowired
    UserRepository user_repository;
    @Override
    public UserDto registerUser(UserDto usrNew){
        User user_new= new User();
        BeanUtils.copyProperties(user_new,usrNew);
        User user_ret=user_repository.save(user_new);
        UserDto usrRetDto=new UserDto();
        BeanUtils.copyProperties(usrRetDto,user_ret);
        return usrRetDto;
    }

    public UserDto getUserByUsername(String username){
        User user_ret= user_repository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User with username "+username+" Not Found"));
        UserDto user_dto=new UserDto();
        BeanUtils.copyProperties(user_dto,user_ret);
        return user_dto;
    }

    public UserDto modifyPassword(UserDto usrModified){
        User usr_modified=user_repository.modifyPasswordByUsername(
                usrModified.getUsername(),
                usrModified.getPassword(),
                Constants.USER_TABLE_NAME
        ).orElseThrow(()->new UsernameNotFoundException("User with username "+usrModified.getUsername()+" Not Found"));
        UserDto user_ret=new UserDto();
        BeanUtils.copyProperties(user_ret,usr_modified);
        return user_ret;
    }
}
