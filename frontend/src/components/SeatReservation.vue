<template>
  <div>
    <h2>고정석</h2>
    <h3>좌석 배치</h3>
    <div class="seats-container">
      <div
        v-for="seat in seats"
        :key="seat.id"
        :class="['seat', { 'available': seat.isAvailable, 'reserved': !seat.isAvailable }]"
        @click="openModal(seat)"
      >
        {{ seat.name }}
        <div v-if="!seat.isAvailable" class="reservation-info">
          <p>이용자: {{ seat.reservedBy }}</p>
          <p>예약일: {{ seat.reservedDate }}</p>
          <p v-if="seat.remainingTime">남은 시간: {{ seat.remainingTime }}</p>
        </div>
      </div>
    </div>
    
    <button @click="goBack" class="back-button">돌아가기</button>

    <!-- 예약 모달 -->
    <div v-if="isModalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <h3>{{ selectedSeat.name }} 예약하기</h3>
        <p>예약 수량: 
          <select v-model="reservationQuantity">
            <option v-for="i in maxReservation" :key="i" :value="i">{{ i }}</option>
          </select>
        </p>
        <button @click="reserveSeat(selectedSeat)">예약</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'; // axios를 사용하여 API 호출

export default {
  name: 'SeatReservation',
  data() {
    return {
      // 20개의 좌석을 초기화합니다.
      seats: Array.from({ length: 20 }, (v, i) => ({
        id: i + 1,
        name: `좌석 ${i + 1}`,
        isAvailable: true, // 모든 좌석을 예약 가능 상태로 초기화합니다.
        reservedBy: null,   // 예약자 초기화
        reservedDate: null, // 예약 날짜 초기화
        remainingTime: null  // 남은 시간 초기화
      })),
      hasReserved: false, // 사용자가 이미 좌석을 예약했는지 여부
      isModalOpen: false, // 모달 열림 상태
      selectedSeat: null, // 선택된 좌석
      reservationQuantity: 1, // 예약 수량 (기본값 1)
      maxReservation: 5, // 최대 예약 수량
    };
  },
  mounted() {
    this.checkReservationStatus(); // 컴포넌트가 마운트될 때 예약 상태를 확인합니다.
    this.startTimer(); // 예약이 있는 경우 남은 시간 타이머 시작
  },
  methods: {
    checkReservationStatus() {
      const userId = localStorage.getItem('userId'); // 로컬 스토리지에서 사용자 ID 가져오기

      // 사용자 예약 정보를 API로부터 가져오는 로직을 추가해야 합니다.
      axios.get(`/api/reservations?userId=${userId}`)
        .then(response => {
          if (response.data.length > 0) {
            this.hasReserved = true; // 사용자가 이미 예약한 경우
            response.data.forEach(reservation => {
              const reservedSeat = this.seats.find(seat => seat.id === reservation.seatId);
              if (reservedSeat) {
                reservedSeat.isAvailable = false; // 좌석 상태 업데이트
                reservedSeat.reservedBy = reservation.userName; // 예약한 사람 이름
                reservedSeat.reservedDate = new Date(reservation.reservedAt).toLocaleDateString(); // 예약 날짜 형식 변환
                reservedSeat.remainingTime = this.calculateRemainingTime(reservation.reservedUntil); // 남은 시간 계산
              }
            });
          }
        })
        .catch(error => {
          console.error('예약 정보를 가져오는 데 실패했습니다:', error);
        });
    },
    calculateRemainingTime(reservedUntil) {
      const now = new Date();
      const end = new Date(reservedUntil);
      const difference = end - now;

      if (difference <= 0) return null; // 시간이 지났다면 null 반환

      const hours = Math.floor((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      const minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));

      return `${hours}시간 ${minutes}분 남음`;
    },
    startTimer() {
      setInterval(() => {
        this.seats.forEach(seat => {
          if (!seat.isAvailable) {
            seat.remainingTime = this.calculateRemainingTime(seat.reservedUntil);
          }
        });
      }, 60000); // 1분마다 남은 시간 업데이트
    },
    openModal(seat) {
      if (seat.isAvailable && !this.hasReserved) {
        this.selectedSeat = seat; // 선택된 좌석 설정
        this.isModalOpen = true; // 모달 열기
        this.reservationQuantity = 1; // 예약 수량 초기화
      } else if (!seat.isAvailable) {
        alert(`${seat.name}은 이미 예약된 좌석입니다.`);
      } else {
        alert(`이미 하나의 좌석을 예약하셨습니다.`);
      }
    },
    closeModal() {
      this.isModalOpen = false; // 모달 닫기
      this.selectedSeat = null; // 선택된 좌석 초기화
    },
    async reserveSeat(seat) {
      if (seat.isAvailable) {
        try {
          const userId = localStorage.getItem('userId'); // 로컬 스토리지에서 사용자 ID 가져오기
          const reservationData = {
            userId: userId,
            seatId: seat.id,
            reservedAt: new Date().toISOString(), // 현재 시간
            reservedUntil: new Date(new Date().getTime() + 60 * 60 * 1000).toISOString(), // 1시간 후
          };

          console.log('예약 요청 데이터:', reservationData); // 요청 데이터 출력

          // 예약 요청을 보내는 API 호출
          const response = await axios.post('/api/reservations', reservationData);
          console.log('서버 응답:', response.data); // 서버 응답 출력

          // 좌석 상태 업데이트
          seat.isAvailable = false; // 좌석 상태 업데이트
          seat.reservedBy = response.data.userName; // 예약한 사람 이름
          seat.reservedDate = new Date().toLocaleDateString(); // 예약 날짜 (오늘 날짜)
          seat.reservedUntil = reservationData.reservedUntil; // 예약 종료 시간 추가
          seat.remainingTime = this.calculateRemainingTime(reservationData.reservedUntil); // 남은 시간 계산

          this.hasReserved = true; // 사용자가 좌석을 예약했음을 표시
          alert(`${seat.name}가 예약되었습니다.`);
          this.closeModal(); // 모달 닫기
        } catch (error) {
          console.error('예약 요청 에러:', error); // 오류 정보 출력
          const errorMessage = error.response?.data?.message || error.message || '좌석 예약에 실패했습니다.';
          alert('좌석 예약에 실패했습니다: ' + errorMessage);
        }
      }
    },
    goBack() {
      this.$router.push('/'); // 홈 페이지로 돌아가기
    },
  },
};
</script>

<style scoped>
.seats-container {
  display: grid;
  grid-template-columns: repeat(5, 1fr); /* 5개의 열로 구성 */
  gap: 10px; /* 좌석 간격 */
  margin-top: 20px;
}

.seat {
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #ccc;
  cursor: pointer;
  position: relative;
  font-size: 18px;
}

.seat.available {
  background-color: #d4edda; /* 예약 가능한 좌석 */
}

.seat.reserved {
  background-color: #ffeeba; /* 예약된 좌석 - 노란색 */
  cursor: not-allowed; /* 클릭할 수 없게 */
}

.reservation-info {
  position: absolute;
  top: 10px;
  left: 10px;
  font-size: 14px;
  background-color: white;
  border: 1px solid #ccc;
  padding: 5px;
  z-index: 1;
}

.back-button {
  margin-top: 20px;
  padding: 10px 15px;
  font-size: 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.back-button:hover {
  background-color: #0056b3;
}

/* 모달 스타일 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5); /* 반투명 배경 */
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.close {
  cursor: pointer;  
  float: right;
  font-size: 20px;
}
</style>
