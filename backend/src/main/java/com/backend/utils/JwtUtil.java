package com.backend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * JWT主工具
 */
@Component // 标记为Spring组件，可以被其他Service或Filter注入使用
public class JwtUtil {

    // 注入配置文件中的秘钥字符串
    @Value("${jwt.secret}")
    private String SECRET;

    // 注入配置文件中的过期时间 (毫秒)
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    // ------------------- 内部工具方法 -------------------

    // 获取用于签名的安全秘钥
    private Key getSigningKey() {
        // 使用 Base64 解码秘钥，生成 Key 对象
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 从 Token 中提取 Claims (载荷)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 使用秘钥进行解析和签名验证
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ------------------- 核心方法：生成 Token -------------------

    /**
     * 根据用户名生成 JWT Token
     * @param username 用户的唯一标识 (如用户名或用户ID)
     * @return 生成的 JWT 字符串
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)        // sub: Token的主题，通常是用户名
                .setIssuedAt(now)            // iat: 签发时间
                .setExpiration(expirationDate) // exp: 过期时间
                // 这里可以添加更多自定义信息 (例如用户角色)
                // .claim("role", "admin")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 用秘钥签名
                .compact(); // 压缩成字符串
    }

    // ------------------- 核心方法：验证和提取 -------------------

    /**
     * 从 Token 中提取用户名
     * @param token JWT 字符串
     * @return 用户名
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 检查 Token 是否有效（签名、格式、未过期）
     * @param token JWT 字符串
     * @return 有效返回 true，否则返回 false
     */
    public boolean validateToken(String token) {
        try {
            // 尝试解析 Token，如果签名无效、格式错误或过期，都会抛出异常
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            // Token 过期
            System.err.println("JWT Token has expired: " + e.getMessage());
        } catch (JwtException e) {
            // 其他JWT异常，如签名无效
            System.err.println("Invalid JWT Token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Token 字符串为空
            System.err.println("JWT Token is empty: " + e.getMessage());
        }
        return false;
    }

    // 通用方法：提取特定 Claims (例如：过期时间)
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}