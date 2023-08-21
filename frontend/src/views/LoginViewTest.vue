<template>
  <form @submit.prevent="submit">
    <v-col cols="12" sm="8" md="6" lg="4">
    <v-text-field
      v-model="id.value.value"
      :counter="10"
      :error-messages="id.errorMessage.value"
      label="Id"
    >
    <template v-slot:prepend> 
      <!-- <svg-icon type="mdi" :path="path"></svg-icon> -->
    </template>
    </v-text-field>
  </v-col>

  <v-col cols="12" sm="8" md="6" lg="4">
    <v-text-field
      v-model="password.value.value"
      :counter="10"
      :error-messages="password.errorMessage.value"
      label="Password"
    >
    <template v-slot:prepend> 
      <!-- <svg-icon type="mdi" :path="path2"></svg-icon> -->
    </template>
  </v-text-field>
  </v-col>

    <v-btn
      class="me-4"
      type="submit"
    >
      로그인
    </v-btn>

    <v-btn>
      회원가입
    </v-btn>
  </form>
</template>

  
  <script setup>
    import SvgIcon from '@jamescoyle/vue-icon';
    import { mdiAccount } from '@mdi/js';
    import { mdiLock } from '@mdi/js';
    import { useRouter } from 'vue-router';
    import axios from 'axios'
    import { useField, useForm } from 'vee-validate'
    import { useAuthStore } from '@/store/app';


    const { handleSubmit} = useForm({
      validationSchema: {
        id (value) {
          if (value?.length >= 2) return true
          return 'Id needs to be at least 2 characters.'
        },
        password (value) {
          if (value?.length >= 2) return true
          return 'password needs to be at least 2 characters.'
        },
      },
    })

    const id = useField('id')
    const password = useField('password')
    const authStore = useAuthStore();
    const router = useRouter(); // 라우터 객체 가져오기
    
//     const submit = handleSubmit(async values => {
//   try {
//     const response = await axios.post('http://localhost:8080/user/login', values);
//     const receivedToken = response.data.token;

//     // Pinia store를 이용하여 로그인 상태 업데이트 및 토큰 저장
//     authStore.loginSuccess(receivedToken);

//     // 로그인 후 페이지 이동 로직 추가
//   } catch (error) {
//     console.error(error);
//   }
// });

    const submit = handleSubmit(values => {
      
      console.log(values);
      axios.post("http://localhost:8080/user/login",values)
      .then(response => {
        // POST 요청 성공 시 로직
        console.log(response.data);
        const receivedToken = response.data.token;

        localStorage.setItem('accessToken',receivedToken);
        
         // Pinia store를 이용하여 로그인 상태 업데이트
        authStore.loginSuccess();

        console.log(receivedToken);

        // 로그인 후 페이지 이동
        router.push('/header'); // 예시: 로그인 후 헤더 페이지로 이동

      })
      // POST 요청 실패 시 로직
      .catch(error => {
        console.error(error);
      });
    }
  )
    
  </script>