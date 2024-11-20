package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.*;
import org.example.reading_room_reservation.entity.Reservation;

import java.util.List;

@Mapper
public interface ReservationMapper {

    @Select("SELECT * FROM reservations")
    List<Reservation> getAllReservations();

    @Select("SELECT * FROM reservations WHERE id = #{id}")
    Reservation getReservationById(int id);

    @Insert("INSERT INTO reservations (user_id, seat_id, reserved_at, reserved_until) " +
            "VALUES (#{userId}, #{seatId}, #{reservedAt}, #{reservedUntil})")
    void createReservation(Reservation reservation);

    @Select("SELECT * FROM reservations WHERE user_id = #{userId}")
    List<Reservation> getReservationsByUserId(int userId);

    @Delete("DELETE FROM reservations WHERE id = #{id}")
    void deleteReservation(int id);

    @Delete("DELETE FROM reservations WHERE seat_id = #{seatId}")
    void deleteReservationBySeatId(int seatId);

    @Select("SELECT COUNT(*) FROM reservations WHERE user_id = #{userId} AND seat_id = #{seatId}")
    int countUserReservationsForSeat(@Param("userId") int userId, @Param("seatId") int seatId);

}
