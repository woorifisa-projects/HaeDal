<template>
  <form @submit.prevent="submit">
    <v-app-bar flat>
      <v-container class="mx-auto d-flex align-center justify-center ">
        <!-- <v-btn icon
          class="me-4"
          color="grey-darken-1"
          width="30000px"
          object-fit="cover"
        ></v-btn> -->
        <a href="http://13.209.167.190/home"><img src='@/assets/img/HaeDalLogo.png' class="logo"></a>
        <v-btn v-for="link in links" :key="link" :text="link" variant="text" :to="`/${link.toLowerCase()}`"></v-btn>
        <!-- 해당링크 소문자로 바꿔서 라우터로 이동시켜준다 -->
        <v-spacer></v-spacer>

        <div style="white-space: nowrap;">
          <!-- 로그인 상태에 따라 다른 내용을 표시 -->
          <div v-if="authStore.isLoggedIn">
            <p> {{ username }}고객님 환영합니다.</p>
          </div>
          <div v-else>
            <p></p>
          </div>
        </div>

        <v-btn class="me-4" type="submit">
          <div v-if="authStore.isLoggedIn">
            <p>Logout</p>
          </div>
          <div v-else>
            <a href="http://localhost:3000/login"></a>
            <p>Login</p>
          </div>
        </v-btn>

      </v-container>
    </v-app-bar>

  </form>
</template>

<script setup>
import { onMounted } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/store/app';
import { ref } from 'vue';
import router from '@/router';

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



onMounted(() => {
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
    // "http://localhost:8080/user/alog"
    axios.get("http://15.164.189.153:8080/user/alog", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
      },
    })
      .then(response => {
        console.log(response.data);
        console.log(response.data.name);
        authStore.setUserName(response.data.name)
        // 전역으로 authStore에 저장해서 username 으로 접근하여 사용
        console.log("오이오이" + authStore.username);
        username.value = authStore.username;
      })
  }

});


</script>


<style>
.logo {
  width: 120px;
  margin: 2rem;
}
</style>
