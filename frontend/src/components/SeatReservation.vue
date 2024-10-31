<template>
  <div class="seat-reservation">
    <h2>좌석 예약</h2>
    <div class="seat-map">
      <div v-for="seat in seats" :key="seat.id" class="seat-row">
        <div
          :class="{
            seat: true,
            available: seat.isAvailable === 1,
            reserved: seat.isAvailable === 0 && seat.reservedBy === userId,
            selected: selectedSeat && selectedSeat.id === seat.id,
          }"
          @click="selectSeat(seat)"
        >
          {{ seat.seatNumber || 'N/A' }}
        </div>
      </div>
    </div>

    <button @click="cancelReservation" :disabled="!selectedSeat || !selectedSeat.reservedBy">예약 취소하기</button>
    <p v-if="message">{{ message }}</p>

    <!-- Reservation Modal -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <h3>예약 세부사항</h3>
        <label for="duration">예약 시간 (시간):</label>
        <input
          type="number"
          v-model.number="reservationDuration"
          min="1"
          placeholder="최소 1시간"
        />
        <p>남은 시간: {{ remainingTime }} 시간</p>
        <button @click="confirmReservation">확인</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      seats: [],
      selectedSeat: null,
      message: "",
      showModal: false,
      reservationDuration: 1,
      remainingTime: 0,
      userId: localStorage.getItem("userId"), // Get userId from localStorage
    };
  },
  async mounted() {
    console.log("컴포넌트가 마운트되었습니다. 좌석을 불러오는 중...");
    await this.fetchSeats();
  },
  watch: {
    reservationDuration(newVal) {
      this.calculateRemainingTime(newVal);
    },
  },
  methods: {
    async fetchSeats() {
      try {
        const response = await fetch("/api/seats");
        if (!response.ok) throw new Error("좌석 정보를 불러올 수 없습니다.");
        this.seats = await response.json();
        console.log("좌석이 성공적으로 불러와졌습니다:", this.seats);
      } catch (error) {
        this.message = error.message;
        console.error("Fetch error:", error);
      }
    },
    selectSeat(seat) {
      if (seat.isAvailable === 0 && seat.reservedBy !== this.userId) {
        this.message = "이미 다른 사용자가 예약한 좌석입니다.";
        console.warn("이미 예약된 좌석을 선택하려고 시도했습니다.");
        return; // 선택하지 않고 메서드 종료
      }

      // 이미 선택된 좌석이 있는 경우 선택 해제
      if (this.selectedSeat) {
        this.selectedSeat.selected = false; // 이전 선택 해제
      }

      this.selectedSeat = seat; // 새로 선택된 좌석 저장

      // Show modal only if the seat is available or reserved by the user
      if (seat.isAvailable === 1 || seat.reservedBy === this.userId) {
        this.showModal = true; // 모달 열기
      }
    },
    calculateRemainingTime(duration) {
      this.remainingTime = duration; // 남은 시간 계산
    },
    async confirmReservation() {
      // Check if a seat is selected
      if (!this.selectedSeat) {
        this.message = "좌석을 선택해주세요.";
        return;
      }

      if (this.reservationDuration < 1) {
        this.message = "유효한 시간을 입력해주세요.";
        return;
      }

      if (!this.userId) {
        this.message = "로그인 후 예약을 진행해주세요.";
        console.warn("로컬 스토리지에서 사용자 ID를 찾을 수 없습니다.");
        return;
      }

      // Check if the user already has a reservation
      const userHasReservation = await this.checkUserReservation(this.userId);
      if (userHasReservation) {
        this.message = "이미 예약된 좌석이 있습니다. 예약 취소 후 다시 시도해주세요.";
        return;
      }

      this.reserveSeats(this.userId, this.selectedSeat.id, this.reservationDuration);
      this.closeModal();
    },
    async checkUserReservation(userId) {
      try {
        const response = await fetch(`/api/users/${userId}/reservations`);
        if (!response.ok) throw new Error("예약 정보를 불러올 수 없습니다.");
        const reservations = await response.json();
        return reservations.length > 0; // 예약이 있으면 true
      } catch (error) {
        console.error("예약 확인 오류:", error);
        return false; // 오류 발생 시 예약이 없는 것으로 간주
      }
    },
    async reserveSeats(userId, seatId, duration) {
      try {
        const response = await fetch(`/api/seats/${seatId}/reserve`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ userId, duration }),
        });

        if (!response.ok) throw new Error("예약에 실패했습니다.");

        this.message = "예약이 완료되었습니다.";
        console.log("예약이 성공적으로 완료되었습니다:", seatId);
        await this.fetchSeats(); // 좌석 상태 업데이트
      } catch (error) {
        this.message = "예약에 실패했습니다: " + error.message;
        console.error("예약 오류:", error);
      }
    },
    async cancelReservation() {
      if (!this.selectedSeat) {
        this.message = "취소할 예약을 선택해주세요.";
        return;
      }

      // Check if the selected seat is reserved by the current user
      if (this.selectedSeat.reservedBy !== this.userId) {
        this.message = "이 좌석은 다른 사용자에 의해 예약되었습니다. 취소할 수 없습니다.";
        return;
      }

      try {
        const response = await fetch(`/api/seats/${this.selectedSeat.id}/cancel`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ userId: this.userId }),
        });

        if (!response.ok) throw new Error("예약 취소에 실패했습니다.");

        this.message = "예약이 취소되었습니다.";
        console.log("예약이 성공적으로 취소되었습니다:", this.selectedSeat.id);
        await this.fetchSeats(); // 좌석 상태 업데이트
        this.closeModal(); // 모달 닫기
      } catch (error) {
        this.message = "예약 취소에 실패했습니다: " + error.message;
        console.error("취소 오류:", error);
      }
    },
    closeModal() {
      this.showModal = false;
      this.selectedSeat = null; // 선택된 좌석 초기화
      this.reservationDuration = 1; // 예약 시간 초기화
      this.remainingTime = 0; // 남은 시간 초기화
      
   
   
   
   
   
    },
  },
};
</script>

<style scoped>
.seat-map {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 20px;
}
.seat-row {
  display: flex;
}
.seat {
  width: 50px;
  height: 50px;
  margin: 5px;
  text-align: center;
  line-height: 50px;
  cursor: pointer;
  border: 1px solid #333;
  transition: background-color 0.3s ease;
}
.seat.available {
  background-color: #4caf50;
}
.seat.reserved {
  background-color: #f44336;
  cursor: not-allowed;
}
.seat.selected {
  background-color: #ffeb3b;
}

.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}
.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  text-align: center;
}
.close {
  cursor: pointer;
  float: right;
}
</style>
