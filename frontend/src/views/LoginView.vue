<template>
    <div class = "page-container">
      <p class ="text-center">
          해와달에 오신것을 환영합니다.
          <br>
      </p>
  
      <div>
      
      </div>
  
      <v-row justify="center">
        <v-text-field
          label="Username"
          :rules="rules"
          hide-details="auto"
        >
        <template v-slot:prepend>
          <svg-icon type="mdi" :path="path"></svg-icon>
        </template>
        </v-text-field>
  
        <div></div>
  
        <v-text-field label="Password">
            <template v-slot:prepend> 
          <svg-icon type="mdi" :path ="path2"></svg-icon> 
            </template> 
        </v-text-field>
  
        <div></div> 
  
        <v-container class="text-center">
          <v-col cols="12" sm="20" md="12">
            <v-btn 
            href = "http://localhost:5173/signupt"
            block rounded="lg" size="x-large">로그인</v-btn>
          </v-col>
          
      </v-container>
  
      <div></div>
  
    <v-container class="text-center">
        
          <v-col cols="12" >
            <v-btn 
            href = "http://localhost:5173/signup"
            block rounded="lg" size="x-large">회원가입</v-btn>
          </v-col>
          
      </v-container>
      </v-row>
    </div>
  </template>
  
  
  
  <script>
  import SvgIcon from '@jamescoyle/vue-icon';
  import { mdiAccount } from '@mdi/js';
  import { mdiLock } from '@mdi/js';
  
  export default {
      name: "my-cool-component",
  
      components: {
          SvgIcon
      },
  
      data: () => ({
        rules: [
          value => !!value || '필수입력',
          value => (value && value.length >= 3) || '아이디는 최소 3글자 이상이어야합니다',
          value => (value && value.length <= 15) || '아이디는 최대 15글자 이하여야합니다',
        ],path: mdiAccount
       , path2:mdiLock,
      }),
    }
  
    
  </script>
  
  <style>
  .page-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh; /* 화면 높이의 100%를 차지하도록 설정 */
  }
  </style>