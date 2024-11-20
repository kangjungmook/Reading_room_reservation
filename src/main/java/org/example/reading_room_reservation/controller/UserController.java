package org.example.reading_room_reservation.controller;

import org.example.reading_room_reservation.Dto.LoginDto;
import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.entity.User;
import org.example.reading_room_reservation.service.JwtService;
import org.example.reading_room_reservation.service.ReservationService;
import org.example.reading_room_reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;


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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        String token = userService.loginUser(loginDto.getEmail(), loginDto.getPassword());
        if (token != null) {
            User user = userService.getUserByEmail(loginDto.getEmail());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("name", user.getUsername());
            response.put("id", user.getId());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "이메일 또는 비밀번호가 잘못되었습니다."));
    }

    // 특정 회원 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return ResponseEntity.of(Optional.ofNullable(user));
    }

    @GetMapping("/{userId}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable int userId) {
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);

        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(reservations);
        }
        return ResponseEntity.ok(reservations);
    }

}
