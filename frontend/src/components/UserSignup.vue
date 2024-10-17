<template>
  <div class="signup">
    <h2>회원가입</h2>
    <form @submit.prevent="handleSubmit">
      <div>
        <label for="username">사용자 이름:</label>
        <input type="text" v-model="username" required />
      </div>
      <div>
        <label for="email">이메일:</label>
        <input type="email" v-model="email" required />
      </div>
      <div>
        <label for="password">비밀번호:</label>
        <input type="password" v-model="password" required />
      </div>
      <button type="submit">가입하기</button>
    </form>
    <p v-if="message">{{ message }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "", // 사용자 이름
      email: "",    // 사용자 이메일
      password: "", // 사용자 비밀번호
      message: "",  // 메시지
    };  
  },
  methods: {
    async handleSubmit() {
      try {
        const response = await fetch("/api/users/signup", { // 서버 API 엔드포인트
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            username: this.username,
            email: this.email,
            password: this.password,
            
          }), 
        });

        if (!response.ok) {
          throw new Error('회원가입 실패'); // 에러 발생 시
        }

        const data = await response.json();
        this.message = "회원가입 성공!";
        console.log(data); // 성공 시 서버 응답 출력


        this.$router.push('/login'); 
      } catch (error) {
        this.message = error.message;
        console.error('Error:', error);
      }
    },
  },
};
</script>

<style scoped>
.signup {
  max-width: 400px;
  margin: auto;
}
</style>
