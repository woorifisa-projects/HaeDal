<template class = "mypage">
  <navigation-bar> </navigation-bar>
  <v-card class="mx-auto" max-width="400">
    <v-img class="align-end text-white" height="200" src="https://cdn.vuetifyjs.com/images/cards/docks.jpg" cover>
      <v-card-title>{{ username }}님 환영합니다</v-card-title>
    </v-img>

    <v-card-text>
      <div> 전화번호 : {{ phonenumber }} </div>
    </v-card-text>

    <v-card-text>
      <div>연령대 : {{ userAgeGroup }}</div>
    </v-card-text>

    <v-card-text>
      <div>이용 목적 : {{ servicePurpose }}</div>
    </v-card-text>

    <v-card-actions>
      <v-btn color="orange">
        Share
      </v-btn>

      <v-btn color="orange" href="http://localhost:3000/profile/edit">
        프로필 수정하기
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
import { onMounted } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/store/app';
import { ref } from 'vue';
import router from '@/router';
import NavigationBar from '@/components/ProfileNavigationBar.vue'

const username = ref(0);
const phonenumber = ref(0);
const userAgeGroup = ref(0);
const servicePurpose = ref(0);
const authStore = useAuthStore();



onMounted(() => {
  console.log("새로고췸");
  // Local Storage에서 토큰을 가져와서 store에 저장
  const storedToken = localStorage.getItem('accessToken');
  console.log("저장된 토큰값 " + authStore.accessToken);

  if (storedToken) {
    console.log("요청전송");
    axios.get("http://localhost:8080/profile", {
      headers: {
        //   Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
        Authorization: `Bearer ${storedToken}`
      },
    })
      .then(response => {
        console.log(response.data);
        username.value = response.data.name;
        phonenumber.value = response.data.phoneNumber;
        userAgeGroup.value = response.data.userAgeGroup;
        servicePurpose.value = response.data.servicePurpose;
      })
  }

});


</script>

<style lang="scss" scoped></style>


