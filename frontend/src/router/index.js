import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../views/HomePage.vue';
import UserLogin from '../components/UserLogin.vue';
import UserSignup from '../components/UserSignup.vue';
import SeatReservation from '../components/SeatReservation.vue';

// 라우터 설정
const routes = [
    {
        path: '/',
        name: 'HomePage',
        component: HomePage,
    },
    {
        path: '/login',
        name: 'UserLogin',
        component: UserLogin,
    },
    {
        path: '/signup',
        name: 'UserSignup',
        component: UserSignup,
    },
    {
        path: '/seat-reservation',
        name: 'SeatReservation',    
        component: SeatReservation,
    },
];

// 라우터 생성
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),  
    routes,
});

export default router;
