package org.example.reading_room_reservation.controller;

import org.example.reading_room_reservation.Dto.LoginDto;
import org.example.reading_room_reservation.entity.User;
import org.example.reading_room_reservation.service.JwtService;
import org.example.reading_room_reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        String token = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());
        if (token != null) {
            User user = userService.getUserByEmail(loginDto.getEmail()); // 이메일로 사용자 정보 조회
            if (user != null) { // user가 null인지 체크
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);         // JWT 토큰
                response.put("name", user.getUsername()); // 사용자 이름
                response.put("id", user.getId());     // 사용자 ID
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "사용자 정보를 찾을 수 없습니다."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "이메일 또는 비밀번호가 잘못되었습니다."));
        }
    }


    // 특정 회원 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
        }
    }
}
