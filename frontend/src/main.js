// main.js

import { createApp } from 'vue';
import App from './App.vue';
import router from './router'; // Vue 라우터를 사용한다면 추가

// Vue 인스턴스 생성 및 라우터 연결 후 애플리케이션 마운트
createApp(App).use(router).mount('#app');
