<template>
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
    <p>로그인되지 않았습니다.</p>
  </div>
</div>
        
        
          <v-btn href="http://localhost:3000/logint"
           class="me-4"
            type="submit"
           >
             Logout
          </v-btn>

        

        <v-responsive max-width="160">
          <v-text-field
            density="compact"
            flat
            hide-details
            label="Search"
            rounded="lg"
            single-line
            variant="solo-filled"
          ></v-text-field>
        </v-responsive>
      </v-container>
    </v-app-bar>

    
  </v-app>
</template>

<script setup>
import { onMounted } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/store/app';
import { ref } from 'vue';

const username = ref(0);
const authStore = useAuthStore();

const links = [
'Home',
'Product',
'RECOMMEND',
'MYPAGE',
];

onMounted(() => {
console.log("새로고췸");
// Local Storage에서 토큰을 가져와서 store에 저장
const storedToken = localStorage.getItem('accessToken');
if (storedToken) {
  authStore.loginSuccess(storedToken);
  console.log(localStorage.getItem('accessToken'));
  // 페이지 로딩 시 사용자 정보 요청 로직 추가 (이전 답변 참고)
}
if(storedToken){
  axios.get("http://localhost:8080/user/alog",{
    headers: {
      Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
    },
  })
    .then (response => {
      console.log(response.data);
      username.value = response.data.username;
    })
}


});
</script>


<!-- <script setup>
  const links = [
    'Home',
    'Product',
    'RECOMMEND',
    'MYPAGE',
  ]
</script>

<script>
  export default {
    data: () => ({
      links: [
        'Home',
        'Product',
        'RECOMMEND',
        'MYPAGE',
      ],
    }),
  }
</script> -->