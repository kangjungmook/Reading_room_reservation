<template>
  <div class="container mt-5">
    <h2 class="text-center mb-4">로그인</h2>
    <form @submit.prevent="login">
      <div class="mb-3">
        <label for="email" class="form-label">이메일</label>
        <input type="email" class="form-control" id="email" v-model="email" required />
      </div>
      <div class="mb-3">
        <label for="password" class="form-label">비밀번호</label>
        <input
          type="password"
          class="form-control"
          id="password"
          v-model="password"
          required
        />
      </div>
      <button type="submit" class="btn btn-success">로그인</button>
    </form>
    <p class="mt-3">
      회원가입이 필요하신가요? <router-link to="/signup">회원가입</router-link>
    </p>
    <p v-if="errorMessage" class="text-danger">{{ errorMessage }}</p>
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
      userId: '', 
    };
  },
  mounted() {
    this.checkLoginStatus();
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
        localStorage.setItem('userId', response.data.id); 
        
        // 로그인 상태 업데이트
        this.isLoggedIn = true;
        this.userName = response.data.name;
        this.userId = response.data.id; 
        
        alert('로그인 성공!');
        this.$router.push('/');
      
      } catch (error) {
        alert('로그인 실패: ' + error.response.data.message); 
      }
    },
    logout() {
      // 로컬 스토리지에서 정보 삭제
      localStorage.removeItem('token');
      localStorage.removeItem('name');
      localStorage.removeItem('userId');

      // 로그인 상태 업데이트
      this.isLoggedIn = false;
      this.userName = '';
      this.userId = '';
      
      alert('로그아웃 성공!');
      this.$router.push('/'); 
    },
    checkLoginStatus() {
      const token = localStorage.getItem('token');
      const name = localStorage.getItem('name');
      const userId = localStorage.getItem('userId'); 

      if (token && name && userId) {
        this.isLoggedIn = true;
        this.userName = name;
        this.userId = userId; 
      }
    }
  },
};
</script>
<style scoped>
h2 {
  font-size: 2rem;
}
form {
  max-width: 400px;
  margin: 0 auto;
}
</style>
