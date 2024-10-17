package org.example.reading_room_reservation.service;

public interface JwtService {
    String getToken(String email);
    String getEmailFromToken(String token);
}
