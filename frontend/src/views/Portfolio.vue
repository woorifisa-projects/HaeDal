<template>
    <navigation-bar>  </navigation-bar>
    <v-card class="mx-auto" max-width="400">
    <v-img class="align-end text-white" height="200" src="https://cdn.vuetifyjs.com/images/cards/docks.jpg" cover>
      <v-card-title>{{ username }}님 의 계좌</v-card-title>
    </v-img>

    <v-card-text>
      <div> 계좌번호 : {{ accountNumber }} </div>
    </v-card-text>

    <v-card-text>
      <div>현재 잔액 : {{ asset }}</div>
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
                <v-btn @click="viewAll">
                    전체 가입 상품
                </v-btn>

                <v-btn @click="financial">
                    찜해둔 상품
                </v-btn>

                <v-btn @click="tema">
                    테마 상품
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
                <v-card-item style="padding: 20px;">
                    <div>
                        <div class="text-h5 mb-3" style="font-weight: bolder;">
                            {{ item.productName }}
                        </div>
                        <div class="text-overline mb-3">
                            <span>
                                <b>상품 기간 :</b> {{ item.period }}개월,
                                <b>금리 :</b> {{ item.interestRate }}%,
                                <b>초기 투자 금액 :</b> {{ item.presentMoney }}원
                            </span>
                        </div>
                        <div class="text-caption">{{ item.longInfo }}</div>
                    </div>
                </v-card-item>
                <div class="d-flex justify-end align-center">
                    <v-card-actions>
                        <v-btn class="button-style" @click=subscribeProduct(item)>
                            상세 정보
                        </v-btn>
                    </v-card-actions>
                </div>
            </v-card>
        </div>
    </div>
    
</template>

<script setup>
import axios from 'axios'
import { watchEffect, ref } from 'vue'
import router from '../router'
import NavigationBar from '@/components/ProfileNavigationBar.vue';
import { mdiConsoleNetwork } from '@mdi/js';
import { useAuthStore } from '@/store/app';
import { onMounted } from 'vue';

const authStore = useAuthStore();
const username = ref(0);
const servicePurpose = ref(0);
const asset = ref(0);
const accountNumber = ref(0);
let servicepurposechange='';

let rightNow = new Date(); //2021-09-22T13:05:36.508Z *코드가 실행되는 순간의 현재 시간
let year = rightNow.getFullYear(); //2021
let month = rightNow.getMonth()+1; //9 *현재 달보다 1 작은 값 반환하므로 +1을 해준다.
let date = rightNow.getDate(); //22

// let today = year+"-"+month+"-"+date;
let today = `${year}-${month}-${date}`;

// 서버에서 받아오는 정보
const listData = ref([]);

// Axios 인스턴스 생성
axios({
        method:"get",
        url:"http://localhost:8080/subscribe/portfolio",
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 토큰 포함
        },
    }).then(res => {
        console.log(today);
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData);
      })
      // POST 요청 실패 시 로직
      .catch(error => {
        console.error(error);
      });

    
    const subscribeProduct = (item) => {
    const productId = item.productId;
    const productName = item.productName
    console.log(productName);
    console.log(item.deposit);
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

onMounted(() => {
  
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
        asset.value = response.data.asset;
        servicePurpose.value = servicepurposechange;
      })
  }

});

</script>


<style lang="scss" scoped>
.overflow-visible {
    margin-bottom: 4rem;
}

.mx-auto {
    text-align: center;
    justify-content: center;
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

.searchProduct {
    width: 30%;
    height: 100%;
    background-color: rgb(238, 238, 238);
    border-radius: 3px;
    margin-right: 1rem;
    padding: 7px 2rem;
    outline: none;
}

.search {
    padding: 10px;
    text-align: center;
    margin-bottom: 2rem;
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

.mx-auto {
    padding: 1rem;
    margin: 1rem 0rem 4rem 0rem;
    box-shadow:
        -4px 4px 10px 0 rgba(51, 96, 133, 0.252),
        12px -12px 16px rgba(255, 255, 255, 0.25);
}

</style>