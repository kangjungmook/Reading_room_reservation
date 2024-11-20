package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.User;

public interface JwtService {
    String generateToken(User user);
    String getEmailFromToken(String token);
}
