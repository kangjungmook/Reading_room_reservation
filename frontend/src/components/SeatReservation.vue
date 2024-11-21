<template>
  <div class="seat-reservation container mt-5">
    <h2 class="text-center">좌석 예약</h2>
    <div class="seat-map d-flex flex-wrap justify-content-center mb-4">
      <div 
        v-for="seat in seats" 
        :key="seat.id" 
        class="seat card p-2 m-2"
        :class="{
          available: seat.isAvailable === 1,
          reserved: seat.isAvailable === 0 && seat.reservedBy !== userId,
          selected: selectedSeat?.id === seat.id,
        }"
        @click="selectSeat(seat)"
      >
        <h5 class="seat-number">{{ seat.seatNumber || "N/A" }}</h5>
      </div>
    </div>

    <!-- 선택된 좌석 표시 -->
    <div v-if="selectedSeat" class="alert alert-info">
      <p>선택된 좌석: {{ selectedSeat.seatNumber }}</p>
    </div>

    <!-- 예약 버튼: 선택된 좌석이 있고 예약 시간이 설정되면 예약 창 띄우기 -->
    <button 
      @click="openReservationPopup"
      :disabled="!selectedSeat"
      class="btn btn-primary">
      예약하기
    </button>
    
    <!-- 예약 취소 버튼 -->
    <button 
      @click="cancelReservation" 
      :disabled="!selectedSeat || String(selectedSeat.reservedBy) !== String(userId)"
      class="btn btn-danger">
      예약 취소
    </button>

    <!-- 사용자 메시지 -->
    <p v-if="message" class="alert alert-warning">{{ message }}</p>

    <!-- 팝업 창 -->
    <div v-if="isPopupOpen" class="popup">
      <div class="popup-content card p-4">
        <h3>예약 시간 선택</h3>
        <label for="reservationTime">예약 시간 (시간):</label>
        <input
          type="number"
          id="reservationTime"
          v-model="reservationTime"
          :min="1"
          :max="24"
          placeholder="예약 시간을 입력하세요"
          class="form-control"
        />
        <button @click="reserveSeat" class="btn btn-success mt-3">예약</button>
        <button @click="closePopup" class="btn btn-secondary mt-3">취소</button>
      </div>
    </div>

    <!-- 예약된 좌석 시간 표시 -->
    <div v-if="reservedSeats.length" class="mt-4">
      <h5>예약된 좌석</h5>
      <ul class="list-group">
        <li v-for="seat in reservedSeats" :key="seat.id" class="list-group-item">
          좌석 {{ seat.seatNumber }} - 예약 시간: {{ seat.reservationTime }} 시간
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      seats: [], // 좌석 데이터
      selectedSeat: null, // 선택된 좌석
      userId: localStorage.getItem("userId"), 
      message: "", 
      reservationTime: null, // 예약 시간
      isPopupOpen: false, 
      reservedSeats: [], // 예약된 좌석 목록
    };
  },
  async mounted() {
    await this.fetchSeats(); 
    this.loadReservedSeats(); 
  },
  methods: {
    // 서버에서 좌석 정보 가져오기
    async fetchSeats() {
      try {
        const response = await fetch("/api/seats");
        if (!response.ok) throw new Error("좌석 정보를 불러올 수 없습니다.");
        this.seats = await response.json();
      } catch (error) {
        this.message = error.message;
      }
    },

    // 예약된 좌석 정보 로드
    loadReservedSeats() {
      const reservedSeatsFromStorage = JSON.parse(localStorage.getItem('reservedSeats')) || [];
      this.reservedSeats = reservedSeatsFromStorage;
    },

    // 좌석 선택
    selectSeat(seat) {
      console.log("클릭한 좌석:", seat);

      if (seat.isAvailable === 1 || String(seat.reservedBy) === String(this.userId)) {
        this.selectedSeat = seat;
        this.message = "";
        console.log("현재 선택된 좌석:", this.selectedSeat);
      } else {
        this.message = "선택할 수 없는 좌석입니다.";
      }
    },

    // 예약하기 버튼을 눌렀을 때 팝업 창 열기
    openReservationPopup() {
      if (!this.selectedSeat) return;
      this.isPopupOpen = true;
      this.reservationTime = null;
    },

    // 팝업 창 닫기
    closePopup() {
      this.isPopupOpen = false;
      this.reservationTime = null;
    },

    // 좌석 예약
    async reserveSeat() {
      if (!this.selectedSeat || !this.reservationTime) return;

      try {
        const response = await fetch(`/api/seats/${this.selectedSeat.id}/reserve`, {
          method: "POST",
          headers: { 
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            userId: this.userId,
            reservationTime: this.reservationTime,
          }),
        });

        if (!response.ok) throw new Error("예약 실패: 이미 예약된 좌석입니다.");

        // 예약 성공 시 처리
        this.message = `좌석 ${this.selectedSeat.seatNumber}이(가) 예약되었습니다. 예약 시간: ${this.reservationTime} 시간`;

        // 예약된 좌석을 로컬 스토리지에 저장
        this.reservedSeats.push({
          seatNumber: this.selectedSeat.seatNumber,
          reservationTime: this.reservationTime,
        });
        localStorage.setItem('reservedSeats', JSON.stringify(this.reservedSeats));

        await this.fetchSeats(); 
        this.selectedSeat = this.seats.find(seat => seat.id === this.selectedSeat.id);
        this.isPopupOpen = false; 
      } catch (error) {
        this.message = error.message;
      }
    },

    // 좌석 예약 취소
    async cancelReservation() {
      if (!this.selectedSeat || String(this.selectedSeat.reservedBy) !== String(this.userId)) {
        this.message = "자신이 예약한 좌석만 취소할 수 있습니다.";
        return;
      }

      try {
        const response = await fetch(`/api/seats/${this.selectedSeat.id}/cancel`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ userId: this.userId }),
        });
        if (!response.ok) throw new Error("예약 취소 실패");

        this.reservedSeats = this.reservedSeats.filter(seat => seat.seatNumber !== this.selectedSeat.seatNumber);
        localStorage.setItem('reservedSeats', JSON.stringify(this.reservedSeats));

        this.message = `좌석 ${this.selectedSeat.seatNumber}의 예약이 취소되었습니다.`;
        await this.fetchSeats(); 
      } catch (error) {
        this.message = error.message;
      }
    },
  },
};
</script>

<style scoped>
.seat-reservation {
  padding: 20px;
}

.seat-map {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.seat {
  width: 100px;
  height: 100px;
  border: 1px solid #ccc;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background-color: #f8f9fa;
}

.seat.available {
  background-color: #d4edda;
}

.seat.reserved {
  background-color: #f8d7da;
}

.seat.selected {
  background-color: #cce5ff;
}

button {
  margin-right: 10px;
  padding: 10px 20px;
}

.popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup-content {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  width: 300px;
  text-align: center;
}

.popup-content input {
  width: 100%;
  margin: 10px 0;
}

.popup-content button {
  margin-top: 10px;
}
</style>
