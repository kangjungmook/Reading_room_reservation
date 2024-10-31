package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.Reservation;
import org.example.reading_room_reservation.entity.Seat;
import org.example.reading_room_reservation.mapper.ReservationMapper;
import org.example.reading_room_reservation.service.SeatService;
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
    public void createReservation(Reservation reservation) {
        System.out.println("reservation 확인  reservation 확인  reservation 확인  -----------------------" + reservation);
        reservationMapper.createReservation(reservation);
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
        return seat != null && seat.getIsAvailable() == 1; // 좌석이 존재하고 사용 가능할 때 true 반환
    }

    // 좌석 ID로 예약 취소
    public void cancelReservationBySeatId(int seatId) {
        reservationMapper.deleteReservationBySeatId(seatId);
    }
}
