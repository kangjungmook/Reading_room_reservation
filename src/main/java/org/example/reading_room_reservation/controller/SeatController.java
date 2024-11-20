package org.example.reading_room_reservation.controller;

import org.example.reading_room_reservation.entity.Seat;
import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.service.SeatService;
import org.example.reading_room_reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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
    public ResponseEntity<Seat> getSeatById(@PathVariable int id) {
        Seat seat = seatService.getSeatById(id);
        return ResponseEntity.ok(seat);
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<String> reserveSeat(@PathVariable int id, @RequestBody Reservation reservation) {
        try {
            Seat seat = seatService.getSeatById(id);
            if (seat.getIsAvailable() != 1) {
                return ResponseEntity.badRequest().body("해당 좌석은 이미 예약되었습니다.");
            }

            reservation.setSeatId(id);
            reservation.setReservedAt(new Timestamp(System.currentTimeMillis()));
            reservation.setReservedUntil(new Timestamp(System.currentTimeMillis() + 3600000));

            reservationService.createReservation(reservation);

            // 예약된 사용자 ID 설정
            seatService.toggleSeatAvailability(id, reservation.getUserId());

            return ResponseEntity.ok("좌석 예약이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 실패: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelSeatReservation(@PathVariable int id) {
        try {
            reservationService.cancelReservationBySeatId(id);
            seatService.freeSeat(id); // 예약 취소 시 좌석을 사용 가능 상태로 설정
            return ResponseEntity.ok("좌석 예약이 성공적으로 취소되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 취소 실패: " + e.getMessage());
        }
    }
}
