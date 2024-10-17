package org.example.reading_room_reservation.mapper;

import org.apache.ibatis.annotations.*;
import org.example.reading_room_reservation.entity.Reservation;

import java.sql.Timestamp;
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

    @Delete("DELETE FROM reservations WHERE id = #{id}")
    void deleteReservation(int id);

    @Update("UPDATE reservations SET reserved_until = #{newReservedUntil} WHERE id = #{reservationId}")
    void updateReservationTime(@Param("reservationId") int reservationId, @Param("newReservedUntil") Timestamp newReservedUntil);

    // 사용자 ID로 예약 조회
    @Select("SELECT * FROM reservations WHERE user_id = #{userId}")
    List<Reservation> getReservationsByUserId(int userId);

    // 좌석 ID로 예약 조회
    @Select("SELECT * FROM reservations WHERE seat_id = #{seatId}")
    List<Reservation> getReservationsBySeatId(int seatId);
}
