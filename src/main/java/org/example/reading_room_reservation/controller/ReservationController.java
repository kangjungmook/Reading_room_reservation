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

import java.util.List;
import java.util.Optional;

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
        return ResponseEntity.of(Optional.ofNullable(reservation));
    }

    // 예약 추가
    @PostMapping
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation,
                                                    @RequestHeader("Authorization") String token) {
        try {
            String email = jwtService.getEmailFromToken(token.replace("Bearer ", ""));
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
            }

            // 예약 처리
            boolean reservationCreated = reservationService.createReservation(reservation);
            if (!reservationCreated) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 좌석은 이미 예약되어 있습니다.");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("예약이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 생성 실패: " + e.getMessage());
        }
    }


    // 예약 취소
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(
            @PathVariable int id,
            @RequestHeader("Authorization") String token) {
        try {
            // 토큰에서 이메일 추출
            String email = jwtService.getEmailFromToken(token.replace("Bearer ", ""));
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자를 찾을 수 없습니다.");
            }

            // 예약 정보 확인
            Reservation reservation = reservationService.getReservationById(id);
            if (reservation == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 예약을 찾을 수 없습니다.");
            }

            // 로그인한 사용자와 예약자 비교
            if (reservation.getUserId() != user.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("본인이 예약한 좌석만 취소할 수 있습니다.");
            }

            // 예약 취소 처리
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("예약이 성공적으로 취소되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 취소 실패: " + e.getMessage());
        }
    }

    // 사용자 ID로 예약 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable int userId) {
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }
}
