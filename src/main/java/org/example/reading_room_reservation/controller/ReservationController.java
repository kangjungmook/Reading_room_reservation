package org.example.reading_room_reservation.controller;

import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable int id) {
        return reservationService.getReservationById(id);
    }

    @PostMapping
    public void createReservation(@RequestBody Reservation reservation) {
        reservationService.createReservation(reservation);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable int id) {
        reservationService.deleteReservation(id);
    }

    @PutMapping("/{id}/extend")
    public void extendReservation(@PathVariable int id, @RequestParam Timestamp newReservedUntil) {
        reservationService.extendReservation(id, newReservedUntil);
    }
}
