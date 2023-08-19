/**
 * main.js
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Components
import App from './App.vue'

// Composables
import { createApp } from 'vue'
import pinia from './store';
// Plugins
import { registerPlugins } from '@/plugins'


const app = createApp(App)
registerPlugins(app)
app.use(pinia)
app.mount('#app');

// registerPlugins(app)

// const authStore = useAuthStore();

// app.mixin({
//     created() {
//       // 페이지 로딩 시 로그인 상태를 확인하여 로그인 상태 업데이트
//       const accessToken = localStorage.getItem('accessToken');
//       if (accessToken) {
//         authStore.loginSuccess();
//       }
//     },
//   });

// app.mount('#app')