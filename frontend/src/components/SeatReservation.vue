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
          {{ seat.seatNumber || "N/A" }}
        </div>
      </div>
    </div>

    <button
      @click="cancelReservation"
      :disabled="!selectedSeat || selectedSeat.reservedBy !== userId"
    >
      예약 취소하기
    </button>
    <p v-if="message">{{ message }}</p>

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
      seats: [], // 좌석 정보
      selectedSeat: null, // 선택된 좌석
      userReservation: null, // 사용자의 기존 예약
      message: "", // 알림 메시지
      showModal: false, // 모달 표시 여부
      reservationDuration: 1, // 예약 시간
      remainingTime: 0, // 남은 시간
      userId: localStorage.getItem("userId"), // 로컬 스토리지에서 사용자 ID 가져오기
    };
  },
  async mounted() {
    await this.fetchSeats();
    await this.checkUserReservation(); // 사용자의 기존 예약 확인
  },
  watch: {
    reservationDuration(newVal) {
      this.remainingTime = newVal;
    },
  },
  methods: {
    // 좌석 정보 가져오기
    async fetchSeats() {
      try {
        const response = await fetch("/api/seats");
        if (!response.ok) throw new Error("좌석 정보를 불러올 수 없습니다.");
        this.seats = await response.json();
      } catch (error) {
        this.message = error.message;
      }
    },

    // 사용자의 기존 예약 확인
    async checkUserReservation() {
      try {
        const response = await fetch(`/api/users/${this.userId}/reservations`);
        if (!response.ok) throw new Error("사용자 예약 정보를 불러올 수 없습니다.");
        const reservations = await response.json();

        // 사용자 예약 정보 설정 (1개의 좌석만 허용)
        this.userReservation = reservations.length > 0 ? reservations[0] : null;

        if (this.userReservation) {
          this.message = `이미 예약된 좌석이 있습니다: ${this.userReservation.seatNumber}`;
        }
      } catch (error) {
        this.message = "기존 예약 확인 중 오류가 발생했습니다: " + error.message;
      }
    },

    // 좌석 선택
    selectSeat(seat) {
      console.log("선택된 좌석:", seat); // 선택된 좌석 정보 확인
      console.log("좌석 사용 가능 여부:", seat.isAvailable); // 해당 좌석의 사용 가능 여부 확인
      console.log("예약된 사용자:", seat.reservedBy); // 좌석을 예약한 사용자 확인

      if (this.userReservation && this.userReservation.seatId !== seat.id) {
        this.message = "이미 예약된 좌석이 있습니다. 취소 후 다시 시도해주세요.";
        console.log("이미 예약된 좌석입니다.");
        return;
      }

      if (seat.isAvailable === 0 && seat.reservedBy !== this.userId) {
        this.message = "이미 다른 사용자가 예약한 좌석입니다.";
        console.log("다른 사용자가 예약한 좌석입니다.");
        return;
      }

      // 예약된 사용자 정보가 없는 경우를 대비하여 조건 추가
      if (seat.reservedBy === undefined || seat.reservedBy === null) {
        console.log("예약된 사용자 없음");
      }

      this.selectedSeat = seat;
      this.showModal = true;
      console.log("선택된 좌석:", this.selectedSeat); // 선택된 좌석 정보 출력
    },

    // 예약 확인 및 요청
    async confirmReservation() {
      console.log("예약 확인 시작");
      console.log("선택된 좌석:", this.selectedSeat); // 선택된 좌석 정보 확인
      console.log("예약 시간:", this.reservationDuration); // 예약 시간 확인
      console.log("사용자 ID:", this.userId); // 사용자 ID 확인

      if (!this.selectedSeat || this.reservationDuration < 1 || !this.userId) {
        this.message = "좌석 및 예약 시간을 확인 후 진행해주세요.";
        console.log("좌석 또는 예약 시간 오류");
        return;
      }

      if (this.userReservation) {
        this.message = "이미 예약된 좌석이 있습니다. 취소 후 다시 시도해주세요.";
        console.log("이미 예약된 좌석이 있습니다.");
        return;
      }

      await this.reserveSeat();
      this.closeModal();
    },
    // 좌석 예약
    async reserveSeat() {
      try {
        const response = await fetch(`/api/seats/${this.selectedSeat.id}/reserve`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            userId: this.userId,
            duration: this.reservationDuration,
          }),
        });

        if (!response.ok) throw new Error("좌석 예약에 실패했습니다.");
        this.message = "좌석 예약이 완료되었습니다.";

        // 예약 성공 후 상태 갱신
        await this.fetchSeats();
        await this.checkUserReservation();
      } catch (error) {
        this.message = "좌석 예약 중 오류가 발생했습니다: " + error.message;
      }
    },

    // 예약 취소
    async cancelReservation() {
      // 좌석을 선택한 상태여야 하고, 선택된 좌석이 예약된 좌석이어야 함
      if (!this.selectedSeat || this.selectedSeat.reservedBy !== this.userId) {
        this.message = "취소할 예약을 선택하거나 자신의 예약만 취소할 수 있습니다.";
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

        // 상태 초기화
        await this.fetchSeats();
        await this.checkUserReservation(); // 예약 정보를 새로고침
        this.selectedSeat = null; // 선택된 좌석 초기화
      } catch (error) {
        this.message = "예약 취소 중 오류가 발생했습니다: " + error.message;
      }
    },

    // 모달 닫기
    closeModal() {
      this.showModal = false;
      this.selectedSeat = null;
      this.reservationDuration = 1;
      this.remainingTime = 0;
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
