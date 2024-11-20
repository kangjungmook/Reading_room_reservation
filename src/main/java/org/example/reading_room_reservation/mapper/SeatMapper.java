package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.*;
import org.example.reading_room_reservation.entity.Seat;

import java.util.List;

@Mapper
public interface SeatMapper {

    // 모든 좌석을 조회할 때 예약된 사용자 정보도 포함하도록 수정
    @Select("SELECT s.id, s.seat_number AS seatNumber, s.is_available AS isAvailable, s.reserved_by AS reservedBy " +
            "FROM seats s")
    List<Seat> getAllSeats();

    // 특정 좌석 조회 시 예약된 사용자 정보 포함
    @Select("SELECT s.id, s.seat_number AS seatNumber, s.is_available AS isAvailable, s.reserved_by AS reservedBy " +
            "FROM seats s WHERE s.id = #{id}")
    Seat getSeatById(int id);

    // 좌석의 예약 상태를 업데이트할 때 reservedBy 정보도 함께 업데이트
    @Update("UPDATE seats SET is_available = #{isAvailable}, reserved_by = #{reservedBy} WHERE id = #{id}")
    void updateSeat(Seat seat);
}
