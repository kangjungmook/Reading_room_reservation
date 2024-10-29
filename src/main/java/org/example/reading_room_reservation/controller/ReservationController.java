package org.example.reading_room_reservation.controller;

import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.entity.User;
import org.example.reading_room_reservation.service.JwtService;
import org.example.reading_room_reservation.service.ReservationService;
import org.example.reading_room_reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    // 모든 예약 조회
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // 특정 예약 조회
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable int id) {
        Reservation reservation = reservationService.getReservationById(id);
        return reservation != null
                ? ResponseEntity.ok(reservation)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // 예약 추가
    @PostMapping
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation,
                                                    @RequestHeader("Authorization") String token) {
        try {
            // JWT 토큰에서 사용자 정보 추출
            String email = jwtService.getEmailFromToken(token.replace("Bearer ", ""));
            User user = userService.getUserByEmail(email);

            // 사용자 ID 설정
            reservation.setUserId(user.getId());
            reservationService.createReservation(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body("예약이 성공적으로 생성되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 생성 실패: " + e.getMessage());
        }
    }

    // 사용자 ID로 예약 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable int userId) {
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    // 좌석 ID로 예약 조회
    @GetMapping("/seat/{seatId}")
    public ResponseEntity<List<Reservation>> getReservationsBySeatId(@PathVariable int seatId) {
        List<Reservation> reservations = reservationService.getReservationsBySeatId(seatId);
        return ResponseEntity.ok(reservations);
    }

}
