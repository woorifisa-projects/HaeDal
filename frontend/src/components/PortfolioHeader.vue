<template>
  <v-card class="mx-auto" max-width="400">
    <v-img class="align-end text-white" height="200" src="https://cdn.vuetifyjs.com/images/cards/docks.jpg" cover>
      <v-card-title>{{ username }}님 의 계좌</v-card-title>
    </v-img>

    <v-card-text>
      <div> 계좌번호 : {{ accountNumber }} </div>
    </v-card-text>

    <v-card-text>
      <div>계좌 잔고 : {{ asset }}원</div>
    </v-card-text>

    <v-card-text>
      <div>계좌 총합금액 : {{ totalPresentAsset }}원</div>
    </v-card-text>

    <v-card-text>
      <div>총 수익율 : <b class="text-red-lighten-1"> +{{(parseFloat(((totalPresentAsset-asset)/asset)*100)).toFixed(2)}}%</b></div>
    </v-card-text>

    <v-card-text>
      <div>이용 목적 : {{ servicePurpose }}</div>
    </v-card-text>

    <v-card-actions>
      <v-btn color="orange">
        Share
      </v-btn>

      <v-btn color="orange" href="http://localhost:3000/profile/edit">
        계좌정보 수정하기
      </v-btn>
    </v-card-actions>
  </v-card>
    <div>
        <v-layout class="overflow-visible" style="height: 56px; box-shadow: none;">
            <v-bottom-navigation v-model="value" color="teal" grow>
                <v-btn href="/viewAll">
                    전체 가입 상품 (돈)
                </v-btn>

                <v-btn @click="redirectToPortfoliodibs">
                    찜해둔 상품
                </v-btn>

                <v-btn @click="redirectToPortfolioDays">
                    가입 일자 순
                </v-btn>
            </v-bottom-navigation>
        </v-layout>
    </div>
</template>

<script setup>
import axios from 'axios'
import { ref } from 'vue'
import router from '../router'
import { useAuthStore } from '@/store/app';

const authStore = useAuthStore();
const username = ref(0);
const servicePurpose = ref(0);
const asset = ref(0);
const totalPresentAsset = ref(0); //계좌 현재 총 자산액(적금액 모두포함)
const accountNumber = ref(0);
let servicepurposechange='';


const redirectToPortfolioDays = () => {
  // Vue Router를 사용하여 경로를 변경
  router.push('/portfoliodays');
};

const redirectToPortfoliodibs = () => {
  // Vue Router를 사용하여 경로를 변경
  router.push('/portfoliodibs');
};


console.log("새로고췸");
  // Local Storage에서 토큰을 가져와서 store에 저장
  const storedToken = localStorage.getItem('accessToken');
  console.log("저장된 토큰값 " + authStore.accessToken);

  if (storedToken) {
    console.log("요청전송");
    axios.get("http://localhost:8080/profile/edit", {
      headers: {
        //   Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
        Authorization: `Bearer ${storedToken}`
      },
    })
      .then(response => {
        switch(response.data.servicePurpose){
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

        console.log(response.data);
        username.value = response.data.name;
        accountNumber.value = response.data.accountNumber;
        asset.value = response.data.asset; // 잔고 남은 금액
        servicePurpose.value = servicepurposechange;
        totalPresentAsset.value = response.data.asset; // 현재남은 잔고를 초기값으로 설정
      })
  }


</script>

<style>

</style>