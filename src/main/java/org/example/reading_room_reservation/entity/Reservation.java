package org.example.reading_room_reservation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private int id;
    private int userId;
    private int seatId;
    private Timestamp reservedAt;
    private Timestamp reservedUntil;
    private boolean isActive;
}
