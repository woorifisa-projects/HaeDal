<template class = "mypage">
  <navigation-bar> </navigation-bar>
  <v-card class="mx-auto" max-width="400">
    <v-img class="align-end text-black" height="200" src='@/assets/img/profile.png' cover>
      <v-card-title style="font-weight: bolder; color:rgba(0, 179, 255, 0.826);">{{ username }}님 환영합니다</v-card-title>
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

    <v-card-actions class="d-flex justify-center align-center">

      <v-btn style=" background-color: rgba(0, 179, 255, 0.062); border-radius: 10px;  margin: 0rem 0rem 0.5rem 0.5rem; "
        color="blue" href="https://haedal.store/profile/edit">
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
let useragegroupchange = '';
let servicepurposechange = '';

onMounted(() => {

  // console.log("새로고췸");
  // Local Storage에서 토큰을 가져와서 store에 저장
  const storedToken = localStorage.getItem('accessToken');
  // console.log("저장된 토큰값 " + authStore.accessToken);

  if (storedToken) {
    // console.log("요청전송");
    axios.get("https://backend.haedal.store/profile", {
      headers: {
        //   Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
        Authorization: `Bearer ${storedToken}`
      },
    })
      .then(response => {
        switch (response.data.userAgeGroup) {
          case 'ONE':
            useragegroupchange = '10대'
            break;
          case 'TWO':
            useragegroupchange = '20대'
            break;
          case 'THREE':
            useragegroupchange = '30대'
            break;
          case 'FOUR':
            useragegroupchange = '40대'
            break;
          case 'FIVE':
            useragegroupchange = '50대'
            break;

        }
        switch (response.data.servicePurpose) {
          case 'MOKDON':
            servicepurposechange = '목돈 마련'
            break;
          case 'FORCAR':
            servicepurposechange = '자동차 구매'
            break;
          case 'FORHOUSE':
            servicepurposechange = '주택 구매'
            break;
          case 'OTHERS':
            servicepurposechange = '기타'
            break;
        }

        // console.log(response.data);
        username.value = response.data.name;
        phonenumber.value = response.data.phoneNumber;
        userAgeGroup.value = useragegroupchange;
        servicePurpose.value = servicepurposechange;
      })
  } else { router.push('/error') };

});


</script>

<style lang="scss" scoped>
.mx-auto {
  box-shadow:
    -4px 4px 10px 0 rgba(51, 96, 133, 0.252),
    12px -12px 16px rgba(255, 255, 255, 0.25);
  ;
  margin: 5rem 0rem 8rem 0rem;
  padding: 10px;
}
</style>


