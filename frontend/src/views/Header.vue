<template>
  <form @submit.prevent="submit">
  <v-app id="inspire">
    <v-app-bar flat>
      <v-container class="mx-auto d-flex align-center justify-center">
        <v-avatar
          class="me-4 "
          color="grey-darken-1"
          size="32"
        ></v-avatar>

        <v-btn
          v-for="link in links"
          :key="link"
          :text="link"
          variant="text"
        ></v-btn>

        <v-spacer></v-spacer>

        <div>
  <!-- 로그인 상태에 따라 다른 내용을 표시 -->
  <div v-if="authStore.isLoggedIn">
    <p> {{username}}고객님 환영합니다.</p>
  </div>
  <div v-else>
    <p></p>
  </div>
</div>
        
          <v-btn 
           class="me-4"
            type="submit"
           >
           <div v-if="authStore.isLoggedIn">
            <p>Logout</p>
          </div>
            <div v-else>
              <a href="http://localhost:3000/logint"></a>
          <p>Login</p>
          </div>
          </v-btn>

      </v-container>
    </v-app-bar>

    
  </v-app>
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
'Product',
'RECOMMEND',
'MYPAGE',
];

const submit = () => {
  if(authStore.isLoggedIn){
    console.log("로그아웃되었습니다.");
        authStore.logout();
        router.push('/header'); // 예시: 로그인 후 헤더 페이지로 이동
  }
  else{
    console.log(localStorage.getItem('accessToken'));
    console.log("로그인 페이지로 이동합니다.");
    router.push('/logint');
  }
}

     

onMounted(() => {
console.log("새로고췸");
console.log("헤더전역관리토큰입니다"+authStore.accessToken);
// Local Storage에서 토큰을 가져와서 store에 저장
const storedToken = localStorage.getItem('accessToken');
if (storedToken) {
  authStore.loginSuccess(storedToken);
  console.log(localStorage.getItem('accessToken'));
  // 페이지 로딩 시 사용자 정보 요청 로직 추가 
}
if(storedToken){
  axios.get("http://localhost:8080/user/alog",{
    headers: {
      Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
    },
  })
    .then (response => {
      console.log(response.data);
      console.log(response.data.name);
      authStore.setUserName(response.data.name)
      // 전역으로 authStore에 저장해서 username 으로 접근하여 사용
      console.log(authStore.username);
      username.value = authStore.username;
    })
}

});


</script>


