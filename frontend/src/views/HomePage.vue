<template>
  <div>
    <h2>독서실</h2>
    <router-link to="/login">로그인</router-link><br>
    <router-link to="/signup">회원가입</router-link><br>
    <router-link to="/seat-reservation">좌석 예약</router-link><br>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
};
</script>
