package org.example.reading_room_reservation.controller;

import org.example.reading_room_reservation.entity.Seat;
import org.example.reading_room_reservation.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping("/{id}/availability")
    public void updateSeatAvailability(@PathVariable int id, @RequestParam boolean isAvailable) {
        seatService.updateSeatAvailability(id, isAvailable);
    }
}
