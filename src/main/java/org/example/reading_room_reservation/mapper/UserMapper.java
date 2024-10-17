package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.*;
import org.example.reading_room_reservation.entity.User;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(int id);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUserByUsername(String username);

    @Insert("INSERT INTO users (username, password, email) " +
            "VALUES (#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createUser(User user);

    @Select("SELECT id, username, email, password FROM users WHERE email = #{email}")
    User getUserByEmail(String email);

}
