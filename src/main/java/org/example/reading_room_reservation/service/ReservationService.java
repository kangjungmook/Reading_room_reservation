package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    public void createReservation(Reservation reservation) {
        reservationMapper.createReservation(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    public Reservation getReservationById(int id) {
        return reservationMapper.getReservationById(id);
    }

    public void cancelReservation(int id) {
        reservationMapper.cancelReservation(id);
    }

    public List<Reservation> getReservationsByUserId(int userId) {
        return reservationMapper.getReservationsByUserId(userId);
    }

    public List<Reservation> getReservationsBySeatId(int seatId) {
        return reservationMapper.getReservationsBySeatId(seatId);
    }
}
