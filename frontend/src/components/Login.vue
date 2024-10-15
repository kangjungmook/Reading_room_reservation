<template>
    <div>
      <h2>Login</h2>
      <form @submit.prevent="login">
        <input v-model="username" placeholder="Username" />
        <input type="password" v-model="password" placeholder="Password" />
        <button type="submit">Login</button>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        username: '',
        password: ''
      };
    },
    methods: {
      async login() {
        try {
          const response = await axios.post('/api/login', {
            username: this.username,
            password: this.password
          });
          alert('Login successful');
          // 로그인 성공 후 페이지 리디렉션 예시
          this.$router.push('/seats');
        } catch (error) {
          console.error(error);
          alert('Login failed');
        }
      }
    }
  };
  </script>
  