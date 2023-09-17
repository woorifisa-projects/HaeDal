<template>
  <navigation-bar> </navigation-bar>
  <v-card class="mx-auto" id="account" max-width="600">
    <v-card-title style="font-weight: bolder; color:rgba(0, 179, 255, 0.826);">{{ username }}님 의 계좌</v-card-title>
    <div style="display: flex; flex-direction: row; margin-bottom: 20px; align-items: center;">
      <v-img class="text-black" height="150" src='@/assets/img/profile.png' alt="프로필 이미지" contain
        style="display: block; margin:0px;">
      </v-img>
      <div class="account-info" style="text-align: left; margin-left: -20px;">
        <v-card-text>
          <div> 계좌번호 : {{ accountNumber }} </div>
          <div>계좌 잔고 : {{ asset }}원</div>
          <div>계좌 총합금액 : {{ totalPresentAsset }}원</div>
          <div>총 수익율 : <b class="text-red-lighten-1"> +{{ (parseFloat(((totalPresentAsset - asset) / asset) *
            100)).toFixed(2) }}%</b></div>
          <div>이용 목적 : {{ servicePurpose }}</div>
        </v-card-text>
      </div>
    </div>
    <v-card-actions class="d-flex justify-center align-center">
      <!-- 버튼은 아래쪽 가운데에 위치 -->
      <v-btn style=" background-color: rgba(0, 179, 255, 0.062); border-radius: 10px;" color="blue"
        href="https://haedal.store/profile/edit">
        계좌정보 수정하기
      </v-btn>
    </v-card-actions>
  </v-card>
  <div>
    <v-layout class="overflow-visible" style="height: 56px; box-shadow: none;">
      <v-bottom-navigation v-model="value" color="teal" grow>
        <v-btn @click="redirectToPortfolio">
          자산별 가입 상품
        </v-btn>

        <v-btn @click="redirectToPortfolioDays">
          가입 일자 순
        </v-btn>
      </v-bottom-navigation>
    </v-layout>
  </div>


  <div style="margin-bottom: 200px;">
    <div id="products" v-bind:class="item.productName" v-for="(item, index) in listData" :key="index">

      <!-- <p
                style="background-color: rgba(0, 179, 255, 0.362); width: 80px; font-weight: bolder; border-radius: 10px; color:rgb(0, 75, 121);; text-align: center;">
                TOP {{ index + 1 }} </p> -->

      <v-card class="mx-auto" max-width="70%" min-width="300px">
        <v-card-item @click=subscribeProduct(item) style="padding: 30px;">
          <div>
            <div class="text-h5 mb-3" style="font-weight: bolder;text-align: left;">
              {{ item.productName }}
            </div>
            <div class="text-overline mb-3" style="text-align: left;">
              <span>
                <v-chip class="mr-1" color="green" text-color="white">
                  {{ item.period }}개월
                </v-chip>
                <v-chip class="ma-1" color="secondary" text-color="white">
                  {{ item.servicePurpose }}
                </v-chip>
                <v-chip class="ma-1" color="primary" text-color="white">
                  {{ item.userAgeGroup }}
                </v-chip>

                <div class="info">
                  <div class="small-info">
                    <b>상품 기간 :</b> {{ item.period }}개월,
                    <b>금리 :</b> {{ item.interestRate }}%,
                    <b>초기 금액 :</b> {{ item.startMoney }}원
                    <br>
                    <b>종료 일자 :</b> {{ item.endSubscribeDate }}
                  </div>

                  <div class="big-info">
                    <div class="text-h5 mb-3" style="font-weight: bolder;text-align: right;;">
                      만난지 {{ item.progressdate }}일째!
                    </div>
                    <div class="text-h5 mb-3" style="font-weight: bolder;text-align: right;;">
                      {{ item.presentMoney + item.cleanplusMoney }}원
                      <div class="text-overline mb-3 text-grey-darken-1" style="font-weight: bolder;text-align: right;;">
                        +{{ item.cleanplusMoney }}원
                        <b class="text-red-lighten-1">+{{ item.plusPercentage }}%</b>
                      </div>
                    </div>
                  </div>
                </div>
              </span>
            </div>
            <div style="font-size: 14px;">{{ item.longInfo }}</div>
          </div>
        </v-card-item>
      </v-card>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios'
import { watchEffect, ref, onBeforeMount } from 'vue'
import router from '../router'
import NavigationBar from '@/components/ProfileNavigationBar.vue';
import { useAuthStore } from '@/store/app';
import { onMounted } from 'vue';
import { getApi } from '@/api/modules';

const authStore = useAuthStore();
const username = ref(0);
const servicePurpose = ref(0);
const asset = ref(0);
const totalPresentAsset = ref(0); //계좌 현재 총 자산액(적금액 모두포함)
const accountNumber = ref(0);
const cleanplusMoney = ref(0);
let servicepurposechange = '';


const redirectToPortfolio = () => {
  // Vue Router를 사용하여 경로를 변경
  router.push('/portfolio');
};

const redirectToPortfolioDays = () => {
  // Vue Router를 사용하여 경로를 변경
  router.push('/portfoliodays');
};

const redirectToPortfoliodibs = () => {
  // Vue Router를 사용하여 경로를 변경
  router.push('/portfoliodibs');
};

// 서버에서 받아오는 정보
const listData = ref([]);


// console.log("새로고췸");
// Local Storage에서 토큰을 가져와서 store에 저장
const storedToken = localStorage.getItem('accessToken');
// console.log("저장된 토큰값 " + authStore.accessToken);

if (storedToken) {
  // console.log("요청전송");
  axios.get("https://backend.haedal.store/profile/edit", {
    headers: {
      //   Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
      Authorization: `Bearer ${storedToken}`
    },
  })
    .then(response => {
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
      accountNumber.value = response.data.accountNumber;
      asset.value = response.data.asset; // 잔고 남은 금액
      servicePurpose.value = servicepurposechange;
      totalPresentAsset.value = response.data.asset; // 현재남은 잔고를 초기값으로 설정
    })
}



// Axios 인스턴스 생성
axios({
  method: "get",
  url: "https://backend.haedal.store/subscribe/portfolio/days",
  headers: {
    Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 토큰 포함
  },
}).then(res => {

  let tempArr = [...res.data]
  tempArr.forEach((item) => {
    // console.log(item)
    listData.value.push(item)
    // chips 표시 내용 변환
    switch (item.servicePurpose) {
      case 'MOKDON':
        item.servicePurpose = '목돈 마련'
        break;
      case 'FORCAR':
        item.servicePurpose = '자동차 구매'
        break;
      case 'FORHOUSE':
        item.servicePurpose = '주택 구매'
        break;
      case 'OTHERS':
        item.servicePurpose = '기타'
        break;
    }

    switch (item.userAgeGroup) {
      case 'ONE':
        item.userAgeGroup = '10대'
        break;
      case 'TWO':
        item.userAgeGroup = '20대'
        break;
      case 'THREE':
        item.userAgeGroup = '30대'
        break;
      case 'FOUR':
        item.userAgeGroup = '40대'
        break;
      case 'FIVE':
        item.userAgeGroup = '50대'
        break;
      case 'ONETWOTHREEFOURFIVE':
        item.userAgeGroup = '전연령'
        break;
    }


    const subscribeDate = new Date(item.subscribeDate);
    const endSubscribeDate = new Date(item.endSubscribeDate);
    const timeDifferenceInMilliseconds = endSubscribeDate - subscribeDate;

    // 밀리초를 일로 변환
    const daysDifference = timeDifferenceInMilliseconds / (1000 * 60 * 60 * 24);

    const minusfirtsdatefinishdate = daysDifference;
    // console.log(minusfirtsdatefinishdate); // 예금 만기일 - 시작일
    const money = item.startMoney; //시작금액
    let n = item.period;

    let totalAmount = 0;
    //예금일때
    if (item.deposit == true) {
      totalAmount = parseFloat(money + (money * (item.interestRate) / 100));
      // console.log(totalAmount + "예금" + money);

      const plusMoney = ((totalAmount - money) / minusfirtsdatefinishdate) * (item.progressdate); //현재 수익
      item.cleanplusMoney = parseInt(plusMoney); //현재 수익 변수 값 초기화
      const plusPercentage = ((item.interestRate) / minusfirtsdatefinishdate) * (item.progressdate);// 현재 수익율
      item.plusPercentage = parseFloat(plusPercentage).toFixed(3);

      const totalMoney = item.presentMoney + item.cleanplusMoney; //현재 총 잔고 더하기
      item.totalMoney = totalMoney;
      totalPresentAsset.value += item.cleanplusMoney;
      // console.log(item.cleanplusMoney);
    }
    else { // 적금일때
      totalAmount = money; // 초기 예금액
      for (let i = 0; i < item.progressdate; i++) {
        totalAmount += (totalAmount * ((item.interestRate / 100) / minusfirtsdatefinishdate)); // 각 날짜에 대한 이자 계산
      }

      const plusMoney = (totalAmount - money); // 현재 수익
      item.cleanplusMoney = parseInt(plusMoney); // 현재 수익 변수 값 초기화
      const plusPercentage = ((totalAmount - money) / money) * 100; // 수익률 계산
      item.plusPercentage = parseFloat(plusPercentage).toFixed(3);


      const totalMoney = item.presentMoney + item.cleanplusMoney; //현재 총 잔고 더하기
      item.totalMoney = totalMoney;
      totalPresentAsset.value += item.cleanplusMoney;
      // console.log(item.cleanplusMoney);
      // console.log(totalAmount + " 적금 " + money);
    }
  })
})
  // POST 요청 실패 시 로직
  .catch(error => {
    console.error(error);
  });

const subscribeProduct = (item) => {
  const productId = item.productId;
  const productName = item.productName
  // console.log(productName);
  // console.log(item.deposit);
  if (item.deposit == true) {
    router.push(
      {
        name: 'subscribeDforShow',
        params: {
          id: productId,
        }
      })
  } else if (item.deposit == false) {
    router.push({
      name: 'subscribeIforShow',
      params: {
        id: productId,
      }
    })
  }
}



</script>


<style lang="scss" scoped>
#account {
  width: 40rem;
  border-radius: 30px;
  box-shadow: none;
  border: solid 1px rgba(0, 149, 255, 0.133);
  background-color: rgba(238, 249, 255, 0.061);
  box-shadow:
    -2px 2px 10px 0 rgba(51, 96, 133, 0.065) inset;
  margin-top: 40px;
}

.overflow-visible {
  margin-bottom: 4rem;
}

.info {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}

.big-info {
  width: 30%;
  text-align: right;
}

.small-info {
  margin-top: 3rem;
  width: 70%;
  text-align: left;
}

.mx-auto {
  padding: 1rem;
  margin: 1rem 0rem 4rem 0rem;
  margin-right: 0px;
  box-shadow: 0px 4px 10px 0 rgba(51, 96, 133, 0.252),
    12px -12px 16px rgba(255, 255, 255, 0.25);
  text-align: center;
  justify-content: center;
}

.account-info .v-card-text div {
  margin: 13px;
  margin-right: 30px;
}

.mx-auto button {
  margin: auto;
}

.container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: repeat(3, minmax(100px, auto));
  grid-gap: 20px;
  margin: 10px 20rem 10rem 20rem;
  display: flex;
  flex-direction: column;
}


.button-style {
  width: 10rem;
  border-radius: 10px;
  box-shadow: none;
  background: rgba(0, 179, 255, 0.826);
  color: white;
  margin-top: 14px;
  font-weight: bolder;
  font-size: 18px;
}


#products:hover {
  transition: transform 0.5s ease;
  transform: scale(1.03);
  cursor: pointer;
}

.v-bottom-navigation {
  background: none;
  color: rgb(0, 149, 255);
  box-shadow: none;
  padding: 1px;
}

.v-bottom-navigation button {
  background: rgba(255, 255, 255, 0.264);
  box-shadow: -3px 2px 9px 0 rgba(51, 96, 133, 0.252),
    12px -12px 16px rgba(255, 255, 255, 0.25);
  margin-left: 16px;
  border-radius: 10px;
}

.v-bottom-navigation .v-bottom-navigation__content>.v-btn {
  font-size: 14px;
  font-weight: 500;
  height: 3rem;
  max-width: 168px;
  min-width: 80px;
  text-transform: none;
  transition: inherit;
  width: 118px;
  border-radius: 24px;
}

* {
  font-family: 'Noto Sans KR', sans-serif !important;
}
</style>