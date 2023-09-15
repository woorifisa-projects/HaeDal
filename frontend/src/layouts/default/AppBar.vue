<template>
  <form @submit.prevent="submit">
    <v-app-bar class="bar" flat :style="{ backgroundColor: appBarColor }">
      <v-container class="mx-auto d-flex align-center justify-center ">
        <!-- <v-btn icon
          class="me-4"
          color="grey-darken-1"
          width="30000px"
          object-fit="cover"
        ></v-btn> -->
        <a href="https://haedal.store/home" aria-label="해달 홈"><img src='@/assets/img/HaeDalLogo.png' class=" logo"
            alt="해달 로고"></a>
        <v-btn class="nav-link" v-for="link in links" :key="link" :text="link" variant="text"
          :to="`/${link.toLowerCase()}`"></v-btn>
        <!-- 해당링크 소문자로 바꿔서 라우터로 이동시켜준다 -->
        <v-spacer></v-spacer>
        <div style="white-space: nowrap;">
          <!-- 로그인 상태에 따라 다른 내용을 표시 -->
          <div v-if="authStore.isLoggedIn" style=" color: rgba(0, 68, 128, 0.826);">
            <span style="margin-right: 20px;"> {{ authStore.username }}고객님 환영합니다.</span>
            <span>{{ minutesRemaining }} : {{ secondsRemaining }}</span>

            <!--토큰 만료시간 체크-->
            <b></b>
          </div>
          <div v-else>
            <p></p>
          </div>
        </div>


        <v-btn class="me-4" type="submit"
          style=" color: rgba(0, 68, 128, 0.826); background-color:  rgba(0, 179, 255, 0.062); margin-left: 20px;">
          <div v-if="authStore.isLoggedIn">
            <p>Logout</p>
          </div>
          <div v-else>
            <a href="https://haedal.store/login" aria-label="해달 로그인"></a>
            <p>Login</p>
          </div>
        </v-btn>
      </v-container>
    </v-app-bar>
  </form>
</template>
<script setup>
import { onMounted, computed, watch } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/store/app';
import { ref } from 'vue';
import router from '@/router';

const minutesRemaining = ref(0);
const secondsRemaining = ref(0);

const username = ref(0);
const authStore = useAuthStore();
const links = [
  'Home',
  'Products',
  'RECOMMEND',
  'MYPAGE',
];
const submit = () => {
  if (authStore.isLoggedIn) {
    alert("로그아웃되었습니다.");
    console.log("로그아웃되었습니다.");
    authStore.logout();
    router.push('/home'); // 예시: 로그인 후 헤더 페이지로 이동
  }
  else {
    console.log(localStorage.getItem('accessToken'));
    console.log("로그인 페이지로 이동합니다.");
    router.push('/login');
  }
}

onMounted(async () => {
  console.log("새로고췸");
  console.log("헤더전역관리토큰입니다" + authStore.accessToken);
  // Local Storage에서 토큰을 가져와서 store에 저장
  const storedToken = localStorage.getItem('accessToken');
  if (storedToken) {
    authStore.loginSuccess(storedToken);
    console.log(localStorage.getItem('accessToken'));
    // 페이지 로딩 시 사용자 정보 요청 로직 추가
  }
  if (storedToken) {
    // 사용자 정보를 미리 가져오기
    try {
      authStore.setUserName(localStorage.getItem('userName'))
      // 전역으로 authStore에 저장해서 username 으로 접근하여 사용
      console.log("오이오이" + authStore.username);
      username.value = authStore.username;
    } catch (error) {
      console.error("사용자 정보 조회 중 오류 발생 : ", error);
    }
  }
});

const scrolling = ref(false);
// 스크롤 이벤트 리스너 추가
onMounted(() => {
  window.addEventListener('scroll', () => {
    scrolling.value = window.scrollY > 0;
  });
});

// 동적으로 v-app-bar의 배경색을 설정하는 계산된 속성 추가
const appBarColor = computed(() => {
  // 현재 라우트 경로를 가져옴
  const currentRoute = router.currentRoute.value.path;
  // /recommend 경로인 경우에만 다른 색상 반환
  if ((currentRoute === '/recommend' || currentRoute.startsWith('/subscribe/')) && !scrolling.value) {
    return 'rgba(0, 162, 255, 0.354)';
  } else {
    return 'rgba(255, 255, 255, 0.6)'; // 기본 색상
  }
});

setInterval(() => {
  const token = localStorage.getItem('accessToken'); // JWT 토큰 문자열
  if (token) {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expirationTimestamp = payload.exp;
    const currentTimestamp = Math.floor(Date.now() / 1000); // 현재 시간(초 단위)

    if (currentTimestamp > expirationTimestamp) {
      // 토큰이 이미 만료됨
      console.log("토큰이 이미 만료되었습니다.");
      alert("접속시간이 종료되어 강제 로그아웃되었습니다.")
      console.log("로그아웃되었습니다.");
      authStore.logout();

      router.push('/home');
    } else {
      const timeRemaining = expirationTimestamp - currentTimestamp;
      minutesRemaining.value = Math.floor(timeRemaining / 60); // 분 단위로 변환
      secondsRemaining.value = timeRemaining % 60; // 초 단위로 변환
      // console.log(`토큰 만료까지 ${minutesRemaining} 분 ${secondsRemaining} 초 남았습니다.`);
    }
  }
}, 1000); // 1초마다 업데이트


</script>
<style>
.logo {
  width: 120px;
  height: 41px;
  margin: 2rem;
}

.bar {
  backdrop-filter: blur(6px);
}

.nav-link {
  font-weight: bolder;
  color: rgba(0, 68, 128, 0.826);
}
</style>