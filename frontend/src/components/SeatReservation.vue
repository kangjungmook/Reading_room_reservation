<template>
  <div class="container seat-reservation">
    <h2 class="my-4">좌석 예약</h2>
    <p v-if="message" class="alert alert-info">{{ message }}</p>
    <div class="seats row">
      <div
        v-for="seat in seats"
        :key="seat.id"
        class="col-3 seat mb-3"
        :class="{ reserved: seat.isAvailable === 0 }"
        @click="openReservationDialog(seat)"
      >
        {{ seat.seatNumber }}
        <span v-if="seat.isAvailable === 0">
          (예약됨: {{ getUserName(seat.id) }} - 남은 시간: {{ getRemainingTime(seat.id) }})
          <button class="btn btn-danger btn-sm" @click.stop="cancelReservationById(seat.id)">예약 취소</button>
        </span>
      </div>
    </div>

    <!-- 예약 다이얼로그 -->
    <div v-if="showDialog" class="dialog-overlay">
      <div class="dialog">
        <h3>{{ selectedSeat?.seatNumber }} 예약하기</h3>
        <label for="duration">예약 시간 (시간):</label>
        <input type="number" id="duration" v-model="reservationDuration" min="1" />
        <button class="btn btn-primary" @click="reserveSeat">예약하기</button>
        <button class="btn btn-secondary" @click="cancelReservation">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      seats: [],                  // 좌석 목록
      message: '',                // 사용자 메시지
      token: localStorage.getItem('token'),  // JWT 토큰
      userId: localStorage.getItem('userId'),  // 사용자 ID
      showDialog: false,          // 예약 다이얼로그 표시 여부
      selectedSeat: null,         // 선택된 좌석
      reservationDuration: 1,     // 예약 시간 (시간 단위)
      reservedSeats: [],          // 예약된 좌석 정보
      timer: null,                // 타이머 핸들러
    };
  },
  mounted() {
    this.fetchSeats(); // 좌석 목록 불러오기
    this.fetchReservedSeats(); // 예약된 좌석 목록 불러오기
    this.startTimer(); // 타이머 시작
  },
  beforeUnmount() {
    clearInterval(this.timer); // 컴포넌트 언마운트 시 타이머 정리
  },
  methods: {
    async fetchSeats() {
      try {
        const response = await axios.get('/api/seats', {
          headers: { Authorization: `Bearer ${this.token}` },
        }); // 좌석 목록 API 호출
        this.seats = response.data.map(seat => ({
          ...seat,
          isAvailable: seat.available ? 1 : 0, // 좌석의 예약 가능 여부 설정
        }));
      } catch (error) {
        console.error('Error fetching seats:', error);
        this.message = "좌석 정보를 가져오는 데 실패했습니다.";
      }
    },
    async fetchReservedSeats() {
      try {
        const response = await axios.get('/api/reservations', {
          headers: { Authorization: `Bearer ${this.token}` },
        }); // 예약된 좌석 목록 API 호출
        this.reservedSeats = response.data;
        this.updateSeatInfo(); // 좌석 정보 업데이트
      } catch (error) {
        console.error('Error fetching reserved seats:', error);
        this.message = "예약 정보를 가져오는 데 실패했습니다.";
      }
    },
    openReservationDialog(seat) {
      if (seat.isAvailable === 0) {
        this.message = '이미 예약된 좌석입니다.';
        return;
      }
      this.selectedSeat = seat;
      this.showDialog = true;
    },
    async reserveSeat() {
      if (!this.selectedSeat || this.reservationDuration < 1) {
        this.message = '올바른 예약 시간을 입력하세요.';
        return;
      }

      const reservedUntil = new Date();
      reservedUntil.setHours(reservedUntil.getHours() + this.reservationDuration);

      try {
        // 예약 API 호출
        const response = await axios.post(
          '/api/reservations',
          {
            seatId: this.selectedSeat.id,
            userId: this.userId,
            reservedUntil: reservedUntil.toISOString(),
          },
          {
            headers: { Authorization: `Bearer ${this.token}` },
          }
        );

        // 예약 성공 시 로컬 상태 및 좌석 정보 업데이트
        this.message = '좌석이 예약되었습니다!';
        this.selectedSeat.isAvailable = 0;
        this.selectedSeat.reservedUntil = reservedUntil;
        this.reservedSeats.push(response.data);

        this.showDialog = false;
        this.updateSeatInfo(); // UI 업데이트
      } catch (error) {
        console.error('Error reserving seat:', error);
        this.message = "좌석 예약에 실패했습니다.";
      }
    },
    updateSeatInfo() {
      this.reservedSeats.forEach(reservation => {
        const seat = this.seats.find(seat => seat.id === reservation.seatId);
        if (seat) {
          seat.isAvailable = 0; // 좌석을 예약 상태로 설정
          seat.reservedUntil = new Date(reservation.reservedUntil); // 예약 만료 시간 설정
        }
      });
    },
    async cancelReservationById(seatId) {
      try {
        // 예약 취소 API 호출
        await axios.delete(`/api/reservations/${seatId}`, {
          headers: { Authorization: `Bearer ${this.token}` },
        });

        // 예약 취소 후 상태 및 좌석 정보 업데이트
        const reservationIndex = this.reservedSeats.findIndex(reservation => reservation.seatId === seatId);
        if (reservationIndex !== -1) {
          this.reservedSeats.splice(reservationIndex, 1);
        }

        const seat = this.seats.find(seat => seat.id === seatId);
        if (seat) {
          seat.isAvailable = 1; // 좌석을 다시 사용 가능 상태로 설정
          delete seat.reservedUntil; // 예약 정보 삭제
        }

        this.message = '예약이 취소되었습니다!';
        this.updateSeatInfo(); // UI 업데이트
      } catch (error) {
        console.error('Error canceling reservation:', error);
        this.message = '예약 취소에 실패했습니다.';
      }
    },
    getUserName(seatId) {
      const reservation = this.reservedSeats.find(res => res.seatId === seatId);
      return reservation ? reservation.userName : '';
    },
    getRemainingTime(seatId) {
      const reservation = this.reservedSeats.find(res => res.seatId === seatId);
      return reservation ? this.calculateRemainingTime(reservation.reservedUntil) : '';
    },
    calculateRemainingTime(reservedUntil) {
      const now = new Date();
      const remainingTime = new Date(reservedUntil) - now;
      if (remainingTime < 0) {
        return '시간 만료';
      }
      const hours = Math.floor((remainingTime / (1000 * 60 * 60)) % 24);
      const minutes = Math.floor((remainingTime / (1000 * 60)) % 60);
      const seconds = Math.floor((remainingTime / 1000) % 60);
      return `${hours}시간 ${minutes}분 ${seconds}초 남음`;
    },
    startTimer() {
      this.timer = setInterval(() => {
        this.reservedSeats.forEach(reservation => {
          reservation.remainingTime = this.calculateRemainingTime(reservation.reservedUntil);
        });
      }, 1000); // 1초마다 갱신
    },
  },
};
</script>

<style scoped>
.seat-reservation {
  text-align: center;
}

.seats {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.seat {
  border: 1px solid #007bff;
  padding: 20px;
  margin: 10px;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.seat.reserved {
  background-color: #f8d7da; /* 예약된 좌석 색상 */
  cursor: not-allowed;
}

/* 다이얼로그 스타일 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.dialog {
  background: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}

.list-group-item {
  display: flex;
  justify-content: space-between;
}
</style>
