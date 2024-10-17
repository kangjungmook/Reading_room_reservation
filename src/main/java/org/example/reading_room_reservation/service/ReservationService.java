package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.mapper.ReservationMapper;
import org.example.reading_room_reservation.mapper.UserMapper;
import org.example.reading_room_reservation.mapper.SeatMapper; // 추가
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SeatMapper seatMapper; // 추가

    public List<Reservation> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    public Reservation getReservationById(int id) {
        return reservationMapper.getReservationById(id);
    }

    public void createReservation(Reservation reservation) {
        // 유저 ID 체크
        if (userMapper.getUserById(reservation.getUserId()) == null) {
            throw new IllegalArgumentException("User ID does not exist: " + reservation.getUserId());
        }

        // 좌석 ID 체크
        if (seatMapper.getSeatById(reservation.getSeatId()) == null) {
            throw new IllegalArgumentException("Seat ID does not exist: " + reservation.getSeatId());
        }

        // 예약 생성
        reservationMapper.createReservation(reservation);
    }

    public void deleteReservation(int id) {
        reservationMapper.deleteReservation(id);
    }

    public void updateReservationTime(int reservationId, Timestamp newReservedUntil) {
        // 새로운 예약 시간 유효성 검사
        if (newReservedUntil.before(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("ㄴㄴ");
        }

        reservationMapper.updateReservationTime(reservationId, newReservedUntil);
    }

    public List<Reservation> getReservationsByUserId(int userId) {
        return reservationMapper.getReservationsByUserId(userId);
    }

    public List<Reservation> getReservationsBySeatId(int seatId) {
        return reservationMapper.getReservationsBySeatId(seatId);
    }
}
