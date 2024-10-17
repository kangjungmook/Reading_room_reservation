package org.example.reading_room_reservation.controller;

import org.example.reading_room_reservation.entity.Seat;
import org.example.reading_room_reservation.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    @GetMapping("/{id}")
    public Seat getSeatById(@PathVariable int id) {
        return seatService.getSeatById(id);
    }

    // 좌석의 예약 가능 상태를 토글
    @PostMapping("/{id}/toggle")
    public ResponseEntity<String> toggleSeat(@PathVariable int id) {
        seatService.toggleSeatAvailability(id);
        return ResponseEntity.ok("좌석 상태가 변경되었습니다.");
    }
}
