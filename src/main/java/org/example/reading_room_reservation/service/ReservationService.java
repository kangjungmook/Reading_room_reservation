package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    public List<Reservation> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    public Reservation getReservationById(int id) {
        return reservationMapper.getReservationById(id);
    }

    public void createReservation(Reservation reservation) {
        reservationMapper.createReservation(reservation);
    }

    public void deleteReservation(int id) {
        reservationMapper.deleteReservation(id);
    }

    public void extendReservation(int reservationId, Timestamp newReservedUntil) {
        reservationMapper.updateReservationTime(reservationId, newReservedUntil);
    }
}
