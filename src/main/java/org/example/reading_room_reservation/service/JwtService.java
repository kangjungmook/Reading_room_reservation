package org.example.reading_room_reservation.service;

import org.example.reading_room_reservation.entity.User;

public interface JwtService {
    String generateToken(User user); // JWT 토큰 생성 메서드
    String getEmailFromToken(String token); // 토큰에서 이메일 추출 메서드
}
