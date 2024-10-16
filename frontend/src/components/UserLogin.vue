<template>
  <div>
    <h2>Login</h2>
    <form @submit.prevent="handleSubmit">
      <input v-model="email" type="email" placeholder="Email" required />
      <input v-model="password" type="password" placeholder="Password" required />
      <button type="submit">확인</button>
      <p>{{ message }}</p>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      email: "", // 사용자 입력 이메일
      password: "", // 사용자 입력 비밀번호
      message: "", // 로그인 성공 실패 메시지
    };
  },
  methods: {
    async handleSubmit() {
      try {
        const response = await fetch(`/api/users/login`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email: this.email, // 이메일로 변경
            password: this.password, // 비밀번호
          }),
        });

        if (!response.ok) {
          throw new Error("Login failed: " + (await response.text()));
        }

        const userData = await response.json();
        console.log("Logged in user:", userData);
        this.message = "Login successful!";

        // 추가 작업 (예: 토큰 저장, 리디렉션 등)

      } catch (error) {
        console.error("Error:", error);
        this.message = error.message;
      }
    }
  }
};
</script>
