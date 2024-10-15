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

    public List<Seat> getAllSeats(){
        return seatMapper.getAllSeats();
    }

    public Seat getSeatById(int id){
        return seatMapper.getSeatById(id);
    }

    public void updateSeatAvailability(int id, boolean isAvailable){
        seatMapper.updateSeatAvailability(id,isAvailable);
    }
}
