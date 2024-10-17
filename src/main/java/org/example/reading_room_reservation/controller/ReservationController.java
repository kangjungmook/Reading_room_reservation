package org.example.reading_room_reservation.controller;

import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.service.ReservationService;
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
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation) {
        try {
            reservationService.createReservation(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body("예약이 성공적으로 생성되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 예약 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable int id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 예약 시간 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<String> updateReservationTime(@PathVariable int id, @RequestBody Timestamp newReservedUntil) {
        try {
            reservationService.updateReservationTime(id, newReservedUntil);
            return ResponseEntity.ok("예약 시간이 성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
