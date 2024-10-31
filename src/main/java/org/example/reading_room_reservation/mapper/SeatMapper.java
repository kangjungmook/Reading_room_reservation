package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.*;
import org.example.reading_room_reservation.entity.Seat;

import java.util.List;

@Mapper
public interface SeatMapper {

    @Select("SELECT id, seat_number AS seatNumber, is_available AS isAvailable FROM seats") // seatNumber 필드 추가
    List<Seat> getAllSeats();

    @Select("SELECT id, seat_number AS seatNumber, is_available AS isAvailable FROM seats WHERE id = #{id}")
    Seat getSeatById(int id);

    @Update("UPDATE seats SET is_available = #{isAvailable} WHERE id = #{id}")
    void updateSeat(Seat seat);

}
