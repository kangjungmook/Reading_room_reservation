// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '../components/Login.vue';
import RegisterPage from '../components/Register.vue';  // 경로와 이름이 정확한지 확인
import SeatsPage from '../components/Seats.vue';        // 경로와 이름이 정확한지 확인
import ReservationPage from '../components/Reservation.vue'; // 경로와 이름이 정확한지 확인

const routes = [
  { path: '/login', component: LoginPage },
  { path: '/register', component: RegisterPage },
  { path: '/seats', component: SeatsPage },
  { path: '/reservations', component: ReservationPage }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
