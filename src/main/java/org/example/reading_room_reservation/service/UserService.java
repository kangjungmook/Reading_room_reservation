package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.User;
import org.example.reading_room_reservation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;

    public void createUser(User user) {
        userMapper.createUser(user);
    }

    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    public String loginUser(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return jwtService.generateToken(user);
        }
        return null;
    }
}
