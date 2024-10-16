<template>
  <div class="seat-list">
    <h2>좌석 예약</h2>
    <ul>
      <li v-for="seat in seats" :key="seat.id">
        {{ seat.seatNumber }} -
        <span v-if="seat.isAvailable">Available</span>
        <span v-else>Occupied</span>
        <button v-if="seat.isAvailable" @click="reserveSeat(seat.id)">Reserve</button>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'SeatReservation',
  data() {
    return {
      seats: []
    };
  },
  created() {
    this.fetchSeats();
  },
  methods: {
    fetchSeats() {
      axios.get('/api/seats')
          .then(response => {
            this.seats = response.data;
          });
    },
    reserveSeat(seatId) {
      axios.post('/api/reservations', {
        userId: 1, // 예시로 유저 ID를 하드코딩
        seatId: seatId,
        reservedAt: new Date(),
        reservedUntil: new Date(new Date().getTime() + 60 * 60 * 1000) // 1시간 예약
      }).then(() => {
        this.fetchSeats();
      });
    }
  }
};
</script>
