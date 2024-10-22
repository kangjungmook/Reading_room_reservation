package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.*;
import org.example.reading_room_reservation.entity.Seat;

import java.util.List;

@Mapper
public interface SeatMapper {

    @Select("SELECT * FROM seats")
    @Results({
            @Result(property = "seatNumber", column = "seat_number"),
            @Result(property = "isAvailable", column = "is_available")
    })
    List<Seat> getAllSeats();

    @Select("SELECT * FROM seats WHERE id = #{id}")
    @Results({
            @Result(property = "seatNumber", column = "seat_number"),
            @Result(property = "isAvailable", column = "is_available")
    })
    Seat getSeatById(int id);

    @Select("SELECT is_available FROM seats WHERE id = #{id}")
    boolean getSeatAvailability(int id);  // 현재 좌석의 사용 가능 상태를 조회

    @Update("UPDATE seats SET is_available = NOT is_available WHERE id = #{id}")
    void toggleSeatAvailability(int id);  // 사용 가능 상태를 토글



}
