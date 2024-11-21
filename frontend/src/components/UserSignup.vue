<template>
  <div class="container mt-5">
    <h2 class="text-center mb-4">회원가입</h2>
    <form @submit.prevent="signup">
      <div class="mb-3">
        <label for="username" class="form-label">사용자 이름</label>
        <input type="text" class="form-control" id="username" v-model="username" required />
      </div>
      <div class="mb-3">
        <label for="email" class="form-label">이메일</label>
        <input type="email" class="form-control" id="email" v-model="email" required />
      </div>
      <div class="mb-3">
        <label for="password" class="form-label">비밀번호</label>
        <input type="password" class="form-control" id="password" v-model="password" required />
      </div>
      <button type="submit" class="btn btn-primary">가입하기</button>
    </form>
    <p v-if="message" class="mt-3 text-center text-danger">{{ message }}</p>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      email: '',
      password: '',
      message: '',
    };
  },
  methods: {
    async signup() {
      try {
        const response = await fetch('/api/users/signup', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            username: this.username,
            email: this.email,
            password: this.password,
          }),
        });

        if (!response.ok) {
          throw new Error('회원가입 실패');
        }

        this.message = '회원가입 성공!';
        this.$router.push('/login');
      } catch (error) {
        this.message = error.message;
      }
    },
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
