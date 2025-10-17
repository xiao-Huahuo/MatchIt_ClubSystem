package com.backend.repository;

import com.backend.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * JPA仓库层
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);

    @Query(value= """
        UPDATE :tableName 
        SET password = :password 
        WHERE username = :username;
    """,
    nativeQuery = true)
    Optional<User> modifyPasswordByUsername(
            @Param("username") String username,
            @Param("password") String password,
            @Param("tableName") String user_table_name
    );











}
