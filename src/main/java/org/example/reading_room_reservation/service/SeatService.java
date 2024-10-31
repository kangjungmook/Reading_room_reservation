package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.Seat;
import org.example.reading_room_reservation.mapper.SeatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class    SeatService {

    @Autowired
    private SeatMapper seatMapper;


    // 모든 좌석 조회
    public List<Seat> getAllSeats() {
        return seatMapper.getAllSeats();
    }

    // 특정 좌석 조회
    public Seat getSeatById(int id) {
        return seatMapper.getSeatById(id);
    }

    // 좌석 예약 상태 토글
    public void toggleSeatAvailability(int id) {
        Seat seat = getSeatById(id);
        if (seat != null) {
            seat.setIsAvailable(seat.getIsAvailable() == 1 ? 0 : 1); // 예약 가능 상태 변경
            seatMapper.updateSeat(seat); // 데이터베이스에 변경 사항 저장
        }
    }

    // 좌석 해제
    public void freeSeat(int id) {
        Seat seat = getSeatById(id);
        if (seat != null) {
            seat.setIsAvailable(1); // 좌석을 예약 가능 상태로 변경
            seatMapper.updateSeat(seat); // 데이터베이스에 변경 사항 저장
        }
    }
}
