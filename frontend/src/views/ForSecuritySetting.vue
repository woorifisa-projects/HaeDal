<template>
    <navigation-bar>  </navigation-bar>
    <form @submit.prevent="submit">
    <v-card
      class="mx-auto"
      max-width="500"
    >
      <v-card-title class="text-h6 font-weight-regular justify-space-between">
        <span>{{ currentTitle }}</span>
      </v-card-title>
  
      <v-window v-model="step">
        <v-window-item :value="1">
          <v-card-text>
            <v-text-field
            v-model="password"
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            :type="visible ? 'text' : 'password'"
            density="compact"
            placeholder="Enter your password"
            prepend-inner-icon="mdi-lock-outline"
            variant="outlined"
            @click:append-inner="visible = !visible"
              label="Password"
            ></v-text-field>
            <span class="text-caption text-grey-darken-1">
                Please enter a password to verify your account
            </span>
          </v-card-text>
        </v-window-item>
  
        <v-window-item :value="2">
          <div class="pa-4 text-center">
            <v-icon
            class="mb-6"
            color="success"
            icon="mdi-check-circle-outline"
            size="128"
          ></v-icon>
            <h3 class="text-h6 font-weight-light mb-2">
              본인인증이 완료되었습니다.
            </h3>
            <span class="text-caption text-grey">Thanks for signing up!</span>
          </div>
        </v-window-item>
      </v-window>
  
      <v-divider></v-divider>
  
      <v-card-actions>
    
        <v-spacer></v-spacer>
        <v-btn
          v-if="step <= 2"
          color="primary"
          variant="flat"
          @click="onSubmit"
        >
          확인
        </v-btn>
      </v-card-actions>
    </v-card>
  </form>
  </template>

<script setup>
import { ref, computed } from 'vue';
import NavigationBar from '@/components/ProfileNavigationBar.vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter(); // 라우터 객체 가져오기
const password = ref();
const step = ref(1);
const checkResult = false;

const onSubmit = ()=>{
  if(step.value<2)
  {
    console.log(password.value);
    const requestData = {
      password: password.value // 비밀번호를 JSON 객체에 추가
    };
    console.log(requestData);

      axios({
        method:"post",
        url:"https://backend.haedal.store/security",
        data : requestData,
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 토큰 포함
          // "Content-Type":"application/json" -1개 데이터 직접 보낼때 사용
        },
    })
      .then(response => {
        console.log(response.data);
        step.value++;
      })
      // POST 요청 실패 시 로직
      .catch(error => {
        console.error(error);
      });
    }
    else
    {
      router.push('/security'); 
    }
    }
  


const check = () =>{
  if(checkResult==true){
    step.value++;
  }
  else{
    console.log("비밀번호가 일치하지않습니다.");
  }
  
}

const currentTitle = computed(() => {
  switch (step.value) {
    case 1: return '사용자 확인을 위해 비밀번호를 입력해주세요';
    default: return '계정확인 완료';
  }
});
</script>