<template>
    <form @submit.prevent="submit">
    <div>
      <v-img
        class="mx-auto my-6"
        max-width="228"
        src="https://cdn.vuetifyjs.com/docs/images/logos/vuetify-logo-v3-slim-text-light.svg"
      ></v-img>
  
      <v-card
        class="mx-auto pa-12 pb-8"
        elevation="8"
        max-width="448"
        rounded="lg"
      >
        <div class="text-subtitle-1 text-medium-emphasis">Account</div>
  
        <v-text-field
          v-model="id.value.value"
          density="compact"
          placeholder="Email address"
          prepend-inner-icon="mdi-account-outline"
          variant="outlined"
          :error-messages="id.errorMessage.value"
        ></v-text-field>
  
        <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
          Password
        </div>
  
        <v-text-field
          v-model="password.value.value"
          :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          :type="visible ? 'text' : 'password'"
          density="compact"
          placeholder="Enter your password"
          prepend-inner-icon="mdi-lock-outline"
          variant="outlined"
          @click:append-inner="visible = !visible"
        ></v-text-field>
  
        <v-card
          class="mb-12"
          color="surface-variant"
          variant="tonal"
        >
          <v-card-text class="text-medium-emphasis text-caption">
            Warning: 안내문구다
          </v-card-text>
        </v-card>
  
        <v-btn
         :loading="loading"
          block
          class="mb-8"
          color="blue"
          size="large"
          variant="tonal"
          @click="onSubmit"
        >
          Log In
        </v-btn>
  
        <v-card-text class="text-center">
          <a
            class="text-blue text-decoration-none"
            href="http://localhost:3000/signup"
          >
            Sign up now <v-icon icon="mdi-chevron-right"></v-icon>
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

const {handleSubmit} = useForm({
      validationSchema: {
        id (value) {
          if (value?.length >= 1) return true
          return 'Id needs to be at least 1 characters.'
        },
        password (value) {
          if (value?.length >= 1) return true
          return 'password needs to be at least 1 characters.'
        },
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
      axios.post("http://localhost:8080/user/login",values)
      .then(response => {
        // POST 요청 성공 시 로직
        console.log(response.data);
        const receivedToken = response.data.token;

        localStorage.setItem('accessToken',receivedToken);
        
         // Pinia store를 이용하여 로그인 상태 업데이트
        authStore.loginSuccess(receivedToken);

        console.log("전역관리토큰입니다"+authStore.accessToken);



        // 로그인 후 페이지 이동

        router.push('/home'); 


      })
      // POST 요청 실패 시 로직
      .catch(error => {
        console.error(error);
      });
    }
  )


</script>
<style lang="scss" scoped>

</style>