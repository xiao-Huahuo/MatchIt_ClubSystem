package com.backend.service;

import com.backend.pojo.User;
import com.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // 引入 SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 负责JWT认证中com.backend.config.SecurityConfig中UserDetailsService接口的实现
 * User实体中必须有三个域:username,password,authority
 */
@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 从数据库查找用户实体
        User myUserEntity = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with username: " + username));

        // 2. 权限转换：将 authority 字符串转换为 GrantedAuthority 集合
        Collection<? extends GrantedAuthority> authorities = createAuthorities(myUserEntity.getAuthority());

        // 3. 关键：返回 Spring Security 的 User 实例
        // 注意：这里使用的是 org.springframework.security.core.userdetails.User
        return new org.springframework.security.core.userdetails.User(
                myUserEntity.getUsername(),   // 用户名
                myUserEntity.getPassword(),   // 加密后的密码
                authorities                   // 转换后的权限集合
        );
    }

    /**
     * 辅助方法：将单个权限/角色字符串转换为 GrantedAuthority 集合
     * @param authorityString 用户权限字符串 (如 "ADMIN")
     * @return 包含该权限的集合
     */
    private Collection<? extends GrantedAuthority> createAuthorities(String authorityString) {
        if (authorityString == null || authorityString.isEmpty()) {
            return Collections.emptyList();
        }

        // 规范：Spring Security 推荐角色以 "ROLE_" 开头。
        String roleName = authorityString.toUpperCase();
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        // 将单个权限字符串包装成 SimpleGrantedAuthority 对象，并放入列表中
        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        return List.of(authority); // Java 9+ 语法
        // 如果使用 Java 8: return Collections.singletonList(authority);
    }
}
