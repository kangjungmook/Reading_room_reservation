package org.example.reading_room_reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private int id; // 예약 ID
    private int userId; // 예약하는 사용자 ID
    private int seatId; // 예약하는 좌석 ID
    private Timestamp reservedAt; // 예약 시간
    private Timestamp reservedUntil; // 예약 만료 시간
}
