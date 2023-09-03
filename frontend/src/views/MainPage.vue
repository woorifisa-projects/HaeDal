<template>
  <v-carousel show-arrows="hover">
    <v-carousel-item> <img src="@/assets/img/HaeDalLogo.png">
    </v-carousel-item>

    <v-carousel-item src="https://cdn.vuetifyjs.com/images/cards/hotel.jpg" cover></v-carousel-item>

    <v-carousel-item src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg" cover></v-carousel-item>
  </v-carousel>
  <div style="text-align: center;">
    <p style="margin:30px; color:rgba(0, 179, 255, 0.826);font-size:20px; font-weight: 600;">✨ 해달의 오늘의 추천 상품 ✨</p>
    <v-card class="recommend"
      style="width:50%; height:220px; margin: auto; color:rgb(0, 63, 90); border-radius: 30px;vertical-align:middle !important;">
      <div>
        <div class="text-h5 mb-3" style="font-weight: bolder;">
          {{ recommend.productName }}
        </div>
        <div class="text-overline mb-3">
          <span>
            <b>상품 기간 :</b> {{ recommend.period }}개월,
            <b>금리 :</b> {{ recommend.interestRate }}%,
            <b>최소 가입 금액 :</b> {{ recommend.requiredStartMoney }}원
          </span>
          <div class="text-caption">{{ recommend.shortInfo }}</div>
        </div>
        <v-btn @click="detail(recommend)"
          style="background: rgba(0, 179, 255, 0.826); color:white; font-weight: bold; border-radius: 15px; width:200px;height: 40px;">추천
          상품 정보 보기</v-btn>
      </div>
    </v-card>

  </div>
  <div style=" height: 60rem; text-align: center; margin:60px 0rem -10px 0rem; font-size:40px; font-weight: 600; ">
    <v-divider :thickness="3" color="info" style="width:30%;     border-style: double;
    margin: auto;"></v-divider>
    <p style="margin: 60px; ">🐚</p>
    <p style="color:rgba(0, 179, 255, 0.826); margin-bottom: -20px;">해달과 함께, 해와 달이 뜨고 지듯 꾸준히 저축해보세요!</p>
    <div style=" margin:auto; display: flex; justify-content: center;;">
      <img src='@/assets/img/main-image1.png' class="first-image">
    </div>
    <p style="margin-top:100px; font-size: 18px; color:balck; font-weight: 500;">해달이 추천해드리는 여러분에게 딱 맞는 상품을 만나보세요.
    </p>
    <p style="margin-top:150px; color:rgba(0, 179, 255, 0.826);">지금 시작해 보세요!</p>
    <v-btn
      style="background: rgba(0, 179, 255, 0.826); color:white; font-weight: bold; border-radius: 30px; margin-bottom: 1rem; margin-top:70px; width:400px;height: 60px;"
      href="/products">상품 보러가기</v-btn>
  </div>

  <div
    style="background: linear-gradient(to top, rgba(0, 162, 255, 0.354), rgba(255, 255, 255, 0)); height: 180px;margin-top:500px;display: block;">
  </div>
</template>

<script setup>
import axios from 'axios'
import { watchEffect, ref, onMounted } from 'vue'
import router from '../router'
import { mdiConsoleNetwork } from '@mdi/js';

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080', // 서버의 주소
  // baseURL: 'http://15.164.189.153:8080',
})

// 서버에서 받아오는 정보
const listData = ref([]);
const recommend = ref({});
// 데이터를 가져오는 함수
function fetchData() {
  axiosInstance.get('/products').then((res) => {
    let tempArr = [...res.data]
    tempArr.forEach((item) => {
      listData.value.push(item)
    })
    console.log(listData)
    recommend.value = listData.value[Math.floor(Math.random() * listData.value.length)]
    console.log(recommend.value)

    // 데이터를 Local Storage에 저장
    localStorage.setItem('data', JSON.stringify(recommend.value))
  })
}

// 페이지가 로드될 때 데이터를 가져오거나 Local Storage에서 데이터를 로드
onMounted(() => {
  const storedData = localStorage.getItem('data')
  if (storedData) {

    // Local Storage에 데이터가 있는 경우, 저장된 데이터를 사용
    recommend.value = JSON.parse(storedData)
    console.log(recommend.value)
  } else {

    // Local Storage에 데이터가 없는 경우, 데이터를 가져와서 저장
    fetchData()
  }

  // 오전 12시에 주기적으로 데이터를 업데이트
  setInterval(() => {
    const now = new Date()
    if (now.getHours() === 0 && now.getMinutes() === 0) {
      fetchData()
    }
  }, 60000) // 1분마다 체크 (조절 가능)
})

//추천 상품 상세 정보 보기 버튼
const detail = (item) => {
  const productId = item.productId;
  const productName = item.productName
  console.log(productName);
  console.log(item.deposit);
  if (item.deposit == true) {
    router.push(
      {
        name: 'subscribeD',
        params: {
          id: productId,
        }
      })
  } else if (item.deposit == false) {
    router.push({
      name: 'subscribeI',
      params: {
        id: productId,
      }
    })
  }
}
</script>

<style>
.v-carousel__controls {
  background: none !important;
}

.first-image {
  width: 80%;
  height: 100%;
  object-fit: cover;
  box-shadow: none !important;
  margin-top: 10rem
}

.recommend {
  box-shadow:
    -4px 4px 10px 0 rgba(51, 96, 133, 0.252),
    12px -12px 16px rgba(255, 255, 255, 0.25) !important;
  padding: 40px !important;
  justify-content: center;

  &:hover {
    transition: transform 0.5s ease;
    transform: scale(1.05);
    box-shadow:
      -5px 5px 9px 0 rgba(0, 179, 255, 0.264),
      12px -12px 16px rgba(0, 179, 255, 0.046) !important;
  }
}
</style>
