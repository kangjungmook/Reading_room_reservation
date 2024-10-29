<template>
  <div class="seat-reservation">
    <h2>좌석 예약</h2>
    <div class="seat-map">
      <div v-for="seat in seats" :key="seat.id" class="seat-row">
        <div
          :class="{
            seat: true, 
            available: seat.isAvailable === 1, // 예약 가능한 좌석
            reserved: seat.isAvailable === 0, // 예약된 좌석
            selected: seat.selected,
          }"
          @click="selectSeat(seat)"
        >
          {{ seat.seatNumber }}
        </div>
      </div>  
    </div>
    <button @click="reserveSeats" :disabled="selectedSeats.length === 0">예약하기</button>
    <p v-if="message">{{ message }}</p>
  </div>
</template>
<script>
export default {
  data() {
    return {
      seats: [],
      selectedSeats: [],
      message: "",
    };
  },
  async mounted() {
    await this.fetchSeats();
  },
  methods: {
    async fetchSeats() {
      try {
        const response = await fetch("/api/seats");
        if (!response.ok) {
          throw new Error("좌석 정보를 불러올 수 없습니다.");
        }
        this.seats = await response.json();
        this.seats.forEach((seat) => {
          seat.selected = false; // 좌석 선택 초기화
          console.log(`Seat ID: ${seat.id}, is_available: ${seat.is_available}`);
        });
      } catch (error) {
        this.message = error.message;
      }
    },
    selectSeat(seat) {
      if (seat.is_available === 0) return; // 예약된 좌석 클릭 방지
      seat.selected = !seat.selected; // 선택 상태 토글
      if (seat.selected) {
        this.selectedSeats.push(seat.id); // 선택된 좌석 추가
      } else {
        this.selectedSeats = this.selectedSeats.filter((id) => id !== seat.id); // 선택 해제
      }
    },
    async reserveSeats() {
      try {
        const userId = localStorage.getItem("userId"); 

        // 선택된 각 좌석에 대해 예약 요청을 보냅니다.
        for (const seatId of this.selectedSeats) {
          const response = await fetch(`/api/seats/${seatId}/reserve`, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              userId: userId,
            }),
          });

          if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.error || "예약에 실패했습니다.");
          }
        }

        this.message = "예약이 완료되었습니다.";
        await this.fetchSeats(); // 좌석 정보 다시 가져오기
        this.selectedSeats = []; // 선택된 좌석 초기화
      } catch (error) {
        this.message = error.message; // 에러 메시지 설정
      }
    },
  },
};
</script>

<style scoped>
.seat-map {
  display: flex;
  flex-wrap: wrap;
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
}

.seat.available {
  background-color: #4caf50 !important; /* 예약 가능 좌석 색상 */
}

.seat.reserved {
  background-color: #f44336 !important; /* 예약 완료 좌석 색상 */
  cursor: not-allowed; /* 마우스 커서 사용 불가 표시 */
}

.seat.selected {
  background-color: #ffeb3b; /* 선택된 좌석 색상 */
}
</style>
