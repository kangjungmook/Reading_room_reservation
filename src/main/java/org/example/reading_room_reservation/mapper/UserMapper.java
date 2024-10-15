package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.reading_room_reservation.entity.User;


@Mapper
public interface UserMapper {
    User getUserById(int id);
    User getUserByUsername(String username);
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
}

