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

    public User getUserById(int id) {
        return userMapper.getUserById(id);

    }

    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email); // 이메일로 사용자 조회
    }

    public void createUser(User user) {
        userMapper.createUser(user);
    }

    // 로그인 기능 추가
    public String loginUser(String email, String password) {
        User user = userMapper.getUserByEmail(email); // 이메일로 사용자 조회
        if (user != null && user.getPassword().equals(password)) {
            return jwtService.getToken(email); // 비밀번호가 일치하면 JWT 반환
        }
        return null; // 일치하는 사용자가 없으면 null 반환
    }

    // 이메일로 사용자 이름 조회
    public String getNameByEmail(String email) {
        User user = userMapper.getUserByEmail(email);
        return (user != null) ? user.getUsername() : null; // 사용자 이름 반환
    }
}
