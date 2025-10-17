package com.backend.filter;

import com.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // 注入我们之前编写的工具类

    @Autowired
    private UserDetailsService userDetailsService; // 注入用户详情服务，用于加载用户

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 从请求头中提取 Token
        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            // 2. 尝试从 Token 中提取用户名
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                // Token 解析失败或过期，日志记录即可，无需在此中断，让Spring Security处理
            }
        }

        // 3. 验证用户名和认证状态
        // 确保用户名不为空 且 当前安全上下文中没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 4. 从数据库加载用户详情
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5. 验证 Token 是否有效
            if (jwtUtil.validateToken(token)) {

                // 6. 构建认证对象 (Authentication)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // 设置认证详情 (可选)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. 将认证信息放入 Spring Security 上下文
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. 继续过滤器链，放行给下一个 Filter 或 Controller
        filterChain.doFilter(request, response);
    }
}