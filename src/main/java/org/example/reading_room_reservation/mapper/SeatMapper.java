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
    Integer getSeatAvailability(int id);

    @Update("UPDATE seats SET is_available = 0 WHERE id = #{id}")
    void reserveSeat(int id);

    @Update("UPDATE seats SET is_available = 1 WHERE id = #{id}")
    void freeSeat(int id);
}
