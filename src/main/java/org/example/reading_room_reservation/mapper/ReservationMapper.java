package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.reading_room_reservation.entity.Reservation;

import java.util.List;
@Mapper
public interface ReservationMapper {
    void insertReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    void deleteReservation(int id);
    List<Reservation> getUserReservations(int userId);
    Reservation getActiveReservation(int userId);
}
