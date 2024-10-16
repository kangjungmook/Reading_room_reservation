package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.User;
import org.example.reading_room_reservation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    public void createUser(User user) {
        userMapper.createUser(user);
    }

    // UserService.java
    public User loginUser(String email, String password) {
        User user = userMapper.getUserByEmail(email); // 이메일로 사용자 조회
        if (user != null && user.getPassword().equals(password)) {
            return user; // 비밀번호가 일치하면 사용자 정보 반환
        }
        return null; // 일치하는 사용자가 없으면 null 반환
    }

}
