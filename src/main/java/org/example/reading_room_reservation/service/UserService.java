package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.User;
import org.example.reading_room_reservation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserById(int id){
        return userMapper.getUserById(id);
    }

    public User getUserByUsername(String username){
        return userMapper.getUserByUsername(username);
    }

    public void insertUser(User user){
        userMapper.insertUser(user);
    }

    public void updateUser(User user){
        userMapper.updateUser(user);
    }

    public void deleteUser(int id){
        userMapper.deleteUser(id);
    }

}
