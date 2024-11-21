<template>
  <div class="container mt-5">
    <h1 class="text-center mb-4">좌석 예약 시스템</h1>

    <!-- 로그인 상태 -->
    <div v-if="isLoggedIn">
      <h2>환영합니다, {{ userName }}님!</h2>
      <p>원하는 좌석을 선택하여 예약하세요.</p>
      <router-link to="/seat-reservation">
        <button class="btn btn-primary">좌석 예약하기</button>
      </router-link>
      <button class="btn btn-danger" @click="logout">로그아웃</button>
    </div>

    <!-- 로그인 및 회원가입 -->
    <div v-else>
      <h2>회원가입 및 로그인</h2>
      <router-link to="/login">
        <button class="btn btn-success mb-2">로그인</button>
      </router-link>
      <router-link to="/signup">
        <button class="btn btn-secondary mb-2">회원가입</button>
      </router-link>
      <router-link to="/seat-reservation">
        <button class="btn btn-secondary mb-2">좌석 미리 보기</button>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isLoggedIn: false, 
      userName: '',      
    };
  },
  mounted() {
    this.checkLoginStatus(); 
  },
  methods: {
    // 로그인 상태를 확인하는 메소드
    checkLoginStatus() {
      const token = localStorage.getItem('token'); 
      const name = localStorage.getItem('name');   

      if (token && name) {
        this.isLoggedIn = true; 
        this.userName = name;   
      }
    },
    // 로그아웃을 처리하는 메소드
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('name');  
      this.isLoggedIn = false;        
      this.userName = '';             
      alert('로그아웃 성공!');           
      this.$router.push('/');          
    },
  },
};
</script>


<style scoped>
.container {
  max-width: 600px; 
  background-color: #f8f9fa; 
  padding: 30px;
  border-radius: 10px; 
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); 
}

h1 {
  font-size: 2.5rem; 
  color: #343a40;     
}

h2 {
  font-size: 1.75rem; 
  color: #495057;    
}

p {
  font-size: 1.1rem;
  color: #6c757d;    
}

button {
  width: 100%; 
  padding: 10px;
  margin-top: 10px; 
  font-size: 1rem; 
  border-radius: 5px; 
  transition: background-color 0.3s ease;
}

button:hover {
  opacity: 0.9;
}
</style>