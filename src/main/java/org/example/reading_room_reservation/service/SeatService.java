package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.Seat;
import org.example.reading_room_reservation.mapper.SeatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SeatService {

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

    // 좌석 예약 가능 상태를 변경
    public void toggleSeatAvailability(int id) {
        // 현재 좌석의 사용 가능 상태를 조회
        int isAvailable = seatMapper.getSeatAvailability(id);

        // 사용 가능 상태에 따라 업데이트
        if (isAvailable == 1) {
            seatMapper.reserveSeat(id); // 좌석 예약 중으로 설정
        } else {
            seatMapper.freeSeat(id); // 좌석 사용 가능으로 설정
        }
    }
}
