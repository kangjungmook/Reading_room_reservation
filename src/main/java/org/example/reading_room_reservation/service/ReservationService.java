package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.entity.Seat;
import org.example.reading_room_reservation.mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private SeatService seatService;

    // 모든 예약 조회
    public List<Reservation> getAllReservations() {
        return reservationMapper.getAllReservations();
    }

    // 특정 예약 조회
    public Reservation getReservationById(int id) {
        return reservationMapper.getReservationById(id);
    }

    // 예약 추가
    public String createReservation(Reservation reservation) {
        List<Reservation> existingReservations = reservationMapper.getReservationsByUserId(reservation.getUserId());
        if (!existingReservations.isEmpty()) {
            return "해당 사용자는 이미 예약이 있습니다.";
        }

        // 좌석 유효성 검사
        if (!isSeatAvailable(reservation.getSeatId())) {
            return "해당 좌석은 이미 예약되었습니다.";
        }

        // 예약 생성
        reservationMapper.createReservation(reservation);
        return "예약이 성공적으로 생성되었습니다.";
    }

    // 사용자 ID로 예약 조회
    public List<Reservation> getReservationsByUserId(int userId) {
        return reservationMapper.getReservationsByUserId(userId);
    }

    // 예약 취소
    public void cancelReservation(int id) {
        reservationMapper.deleteReservation(id);
    }

    // 좌석 유효성 검사
    public boolean isSeatAvailable(int seatId) {
        Seat seat = seatService.getSeatById(seatId);
        return seat != null && seat.getIsAvailable() == 1;
    }

    // 좌석 ID로 예약 취소
    public void cancelReservationBySeatId(int seatId) {
        reservationMapper.deleteReservationBySeatId(seatId);
    }

    public int countUserReservationsForSeat(int userId, int seatId) {
        return reservationMapper.countUserReservationsForSeat(userId, seatId);
    }


}