package org.example.reading_room_reservation.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    private int id;
    private String seatNumber;
    private boolean isAvailable;
}
