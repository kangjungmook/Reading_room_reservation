package org.example.reading_room_reservation.controller;


import org.example.reading_room_reservation.entity.Seat;
import org.example.reading_room_reservation.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        return new ResponseEntity<>(seatService.getAllSeats(), HttpStatus.OK);
    }
}
