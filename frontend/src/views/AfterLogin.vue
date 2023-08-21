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
          
          
            <v-btn 
             class="me-4"
              type="submit"
             >
                고객님 환영합니다.
            </v-btn>


            <div>
    <!-- 로그인 상태에 따라 다른 내용을 표시 -->
    <div v-if="authStore.isLoggedIn">
      <p>로그인되었습니다.</p>
      <p>토큰: {{ authStore.accessToken }}</p>
    </div>
    <div v-else>
      <p>로그인되지 않았습니다.</p>
    </div>
  </div>
          

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
  import { useAuthStore } from '@/store/app';

    const authStore = useAuthStore();
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

    console.log();
  </script>