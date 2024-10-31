package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.*;
import org.example.reading_room_reservation.entity.User;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (username, password, email) VALUES (#{username}, #{password}, #{email})")
    void createUser(User user);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User getUserByEmail(String email);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(int id);
}
