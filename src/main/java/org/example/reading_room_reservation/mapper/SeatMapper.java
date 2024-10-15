package org.example.reading_room_reservation.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.example.reading_room_reservation.entity.Seat;

import java.util.List;

@Mapper
public interface SeatMapper {
    List<Seat> getAllSeats();
    Seat getSeatById(int id);
    void updateSeatAvailability(int id, boolean isAvailable);
}
