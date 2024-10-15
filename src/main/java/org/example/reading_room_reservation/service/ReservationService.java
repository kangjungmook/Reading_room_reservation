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

    public void insertReservation(Reservation reservation) {
        reservationMapper.insertReservation(reservation);
    }

    public void updateReservation(Reservation reservation) {
        reservationMapper.updateReservation(reservation);
    }

    public void deleteReservation(int id) {
        reservationMapper.deleteReservation(id);
    }

    public List<Reservation> getUserReservations(int userId) {
        return reservationMapper.getUserReservations(userId);
    }

    public Reservation getActiveReservation(int userId) {
        return reservationMapper.getActiveReservation(userId);
    }
}
