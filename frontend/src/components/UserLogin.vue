<template>
  <div class="login-page">
    <h2>로그인</h2>
    <form @submit.prevent="login" v-if="!isLoggedIn">
      <input type="email" v-model="email" placeholder="이메일" required />
      <input type="password" v-model="password" placeholder="비밀번호" required />
      <button type="submit">로그인</button>
    </form>

    <div v-else>
      <p>환영합니다, {{ userName }}!</p>
      <button @click="logout">로그아웃</button>
    </div>

    <router-link to="/signup">회원가입 하시겠습니까?</router-link>
  </div>
</template>
  
<script>
import axios from 'axios';

export default {
  data() {
    return {
      email: '',
      password: '',
      isLoggedIn: false,
      userName: '',
      userId: '', // 사용자 ID 저장
    };
  },
  mounted() {
    this.checkLoginStatus(); // 페이지 로드 시 로그인 상태 확인
  },
  methods: {
    async login() {
      try {
        const response = await axios.post('/api/users/login', {
          email: this.email,
          password: this.password,
        });

        // 토큰과 이름, ID 저장
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('name', response.data.name);
        localStorage.setItem('userId', response.data.id); // 사용자 ID 저장
        
        // 로그인 상태 업데이트
        this.isLoggedIn = true;
        this.userName = response.data.name;
        this.userId = response.data.id; // 사용자 ID 업데이트
        
        alert('로그인 성공!');
        this.$router.push('/'); // 성공 시 홈으로 리다이렉션
      } catch (error) {
        alert('로그인 실패: ' + error.response.data.message); // 오류 메시지 수정
      }
    },
    logout() {
      // 로컬 스토리지에서 정보 삭제
      localStorage.removeItem('token');
      localStorage.removeItem('name');
      localStorage.removeItem('userId'); // 사용자 ID 삭제

      // 로그인 상태 업데이트
      this.isLoggedIn = false;
      this.userName = '';
      this.userId = ''; // 사용자 ID 초기화
      
      alert('로그아웃 성공!');
      this.$router.push('/'); // 로그아웃 후 홈으로 리다이렉션
    },
    checkLoginStatus() {
      const token = localStorage.getItem('token');
      const name = localStorage.getItem('name');
      const userId = localStorage.getItem('userId'); // 사용자 ID 추가 확인

      if (token && name && userId) {
        this.isLoggedIn = true;
        this.userName = name;
        this.userId = userId; // 사용자 ID 업데이트
      }
    }
  },
};
</script>

<style scoped>
.login-page {
  max-width: 400px;
  margin: 0 auto;
}

input {
  display: block;
  margin: 10px 0;
  padding: 10px;
  width: 100%;
  box-sizing: border-box;
}

button {
  margin-top: 10px;
}

p {
  font-size: 1.2em;
}
</style>
