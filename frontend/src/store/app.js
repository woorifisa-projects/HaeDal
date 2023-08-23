import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isLoggedIn: false,
    accessToken: null, // 추가: 토큰 값을 저장할 상태 추가
    username: null
  }),
  actions: {
    loginSuccess(token) {
      this.isLoggedIn = true;
      this.accessToken = token; // 로그인 성공 시 토큰 값 저장
    },
    logout() {
      this.isLoggedIn = false;
      this.accessToken = null; // 로그아웃 시 토큰 값 제거
      localStorage.removeItem('accessToken'); // 추가: 로컬 스토리지에서도 제거
      this.username = null;
    },
    setUserName(username){
      this.username = username;
    }
  },
});