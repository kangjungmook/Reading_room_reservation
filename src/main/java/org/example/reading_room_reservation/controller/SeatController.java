package org.example.reading_room_reservation.controller;

import org.example.reading_room_reservation.entity.Seat;
import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.service.SeatService;
import org.example.reading_room_reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    @GetMapping("/{id}")
    public Seat getSeatById(@PathVariable int id) {
        return seatService.getSeatById(id);
    }


    //좌석예약
    @PostMapping("/{id}/reserve")
    public ResponseEntity<String> reserveSeat(@PathVariable int id, @RequestBody Reservation reservation) {
        try {
            Seat seat = seatService.getSeatById(id);
            if (seat.getIsAvailable() != 1) {  // 예약 가능 여부 확인
                return ResponseEntity.badRequest().body("해당 좌석은 이미 예약되었습니다.");
            }

            // 예약 생성
            reservation.setSeatId(id);
            reservationService.createReservation(reservation);

            // 좌석의 사용 가능 상태를 변경
            seatService.toggleSeatAvailability(id);

            return ResponseEntity.ok("좌석 예약이 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 좌석의 예약 가능 상태를 토글
    @PostMapping("/{id}/toggle")
    public ResponseEntity<String> toggleSeat(@PathVariable int id) {
        seatService.toggleSeatAvailability(id);
        return ResponseEntity.ok("좌석 상태가 변경되었습니다.");
    }
}
