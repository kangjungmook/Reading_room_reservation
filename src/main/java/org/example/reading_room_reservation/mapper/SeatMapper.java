package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.*;
import org.example.reading_room_reservation.entity.Seat;

import java.util.List;

@Mapper
public interface SeatMapper {

    @Select("SELECT * FROM seats")
    List<Seat> getAllSeats();

    @Select("SELECT * FROM seats WHERE id = #{id}")
    Seat getSeatById(int id);

    @Update("UPDATE seats SET is_available = #{isAvailable} WHERE id = #{id}")
    void updateSeatAvailability(@Param("id") int id, @Param("isAvailable") boolean isAvailable);
}
