<template>
  <div class="homepage">
    <h1>도서관 좌석 예약 시스템</h1>
    <div v-if="isLoggedIn">
      <h2>환영합니다, {{ userName }}님!</h2>
      <p>원하는 좌석을 선택하여 예약하세요.</p>
      <router-link to="/seat-reservation">
        <button class="action-button">좌석 예약하기</button>
      </router-link>
      <button class="action-button" @click="logout">로그아웃</button>
    </div>

    <div v-else>
      <h2>회원가입 및 로그인</h2>
      <router-link to="/login">
        <button class="action-button">로그인</button>
      </router-link>
      <router-link to="/signup">
        <button class="action-button">회원가입</button>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isLoggedIn: false, // 로그인 상태
      userName: '', // 사용자 이름
    };
  },
  mounted() {
    this.checkLoginStatus(); // 로그인 상태 확인
  },
  methods: {
    checkLoginStatus() {
      const token = localStorage.getItem('token');
      const name = localStorage.getItem('name');

      if (token && name) {
        this.isLoggedIn = true;
        this.userName = name;
      }
    },
    logout() {
      // 로컬 스토리지에서 정보 삭제
      localStorage.removeItem('token');
      localStorage.removeItem('name');

      // 로그인 상태 업데이트
      this.isLoggedIn = false;
      this.userName = '';
      
      alert('로그아웃 성공!');
      this.$router.push('/'); // 로그아웃 후 홈으로 리다이렉션
    },
  },
};
</script>

<style scoped>
.homepage {
  text-align: center;
  margin-top: 50px;
}

.action-button {
  margin: 10px;
  padding: 10px 20px;
  font-size: 16px;
  color: white;
  background-color: #007bff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.action-button:hover {
  background-color: #0056b3;
}
</style>
