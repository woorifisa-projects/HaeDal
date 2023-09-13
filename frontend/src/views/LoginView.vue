<template>
  <form @submit.prevent="onSubmit">
    <div>
      <v-img class="mx-auto my-6" max-width="200" src='@/assets/img/HaeDalLogo.png'></v-img>

      <v-card class="mx-auto pa-12 pb-8" max-width="448" rounded="lg" style="margin-bottom:100px;">
        <div class="text-subtitle-1 text-medium-emphasis"><b>아이디</b></div>

        <v-text-field v-model="id.value.value" density="compact" placeholder="Enter your Id"
          prepend-inner-icon="mdi-account-outline" variant="outlined"
          :error-messages="id.errorMessage.value"></v-text-field>

        <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
          <b>비밀번호</b>
        </div>

        <v-text-field v-model="password.value.value" :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          :type="visible ? 'text' : 'password'" density="compact" placeholder="Enter your password"
          prepend-inner-icon="mdi-lock-outline" variant="outlined" @click:append-inner="visible = !visible"
          class="mb-8"></v-text-field>

        <v-btn :loading="loading" block class="mb-8" color="blue" size="large" variant="tonal" @click="onSubmit">
          로그인
        </v-btn>

        <v-card-text class="text-center">
          <a class="text-blue text-decoration-none" href="https://haedal.store/signup">
            회원가입 하기 <v-icon icon="mdi-chevron-right"></v-icon>
          </a>
        </v-card-text>
      </v-card>
    </div>
  </form>
</template>

<script setup>

import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios'
import { useField, useForm } from 'vee-validate'
import { useAuthStore } from '@/store/app';

const visible = ref(false);

const { handleSubmit } = useForm({
  validationSchema: {
    id(value) {
      if (value?.length >= 1) return true
      return 'Id는 최소 2글자 이상입니다'
    },
    // password(value) {
    //   if (value?.length >= 1) return true
    //   return '비밀번호는 최소 2글자 이상입니다'
    // },
  },
})

const id = useField('id')
const password = useField('password')
const authStore = useAuthStore();
const router = useRouter(); // 라우터 객체 가져오기
const loading = ref(false);


const onSubmit = handleSubmit(values => {
  loading.value = true;
  setTimeout(() => (loading.value = false), 2000);
  console.log(values);
  // axios.post("http://localhost:8080/user/login", values)
  axios.post("https://backend.haedal.store/user/login", values)
    .then(response => {
      // POST 요청 성공 시 로직

      if (response.status === 400) {
        // 400 Bad Request
        console.log("어이어이")
        alert("로그인 정보가 일치하지 않습니다")
      };

      if (response.status === 409) {
        // 400 Bad Request
        alert("휴면처리된 계정입니다.")
      };

      console.log(response.data);
      const receivedToken = response.data.token;
      const username = response.data.name;

      localStorage.setItem('accessToken', receivedToken);
      localStorage.setItem('userName', username);
      authStore.setUserName(username);

      // Pinia store를 이용하여 로그인 상태 업데이트
      authStore.loginSuccess(receivedToken);

      console.log("전역관리토큰입니다" + authStore.accessToken);


      // 로그인 후 페이지 이동

      router.push('/home');


    })
    // POST 요청 실패 시 로직
    .catch(error => {
      console.error(error);
      console.log(error.response.status);
      if (error.response.status === 400) {
        // 400 Bad Request
        console.log("어이어이")
        alert("로그인 정보가 일치하지 않습니다")
      }
      else if (error.response.status === 409) {
        alert("휴면처리된 계정입니다.")
      }
    }
    )
});


</script>
<style lang="scss" scoped>
.v-card {
  background-color: white;
  outline: solid 2px rgba(0, 139, 219, 0.469);
  box-shadow: none;
}
</style>