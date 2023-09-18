<template>
  <v-carousel
    class="banner"
    delimiter-icon="mdi-minus"
    show-arrows="hover"
    cycle
    interval="3000"
    height="60vh"
  >
    <v-carousel-item>
      <img src="@/assets/img/banner-1.png" alt="해달 홍보 배너"
    /></v-carousel-item>
    <v-carousel-item
      ><img src="@/assets/img/banner-2.png" alt="해달 상품 홍보 배너"
    /></v-carousel-item>
    <v-carousel-item
      ><img src="@/assets/img/banner-3.png" alt="해달 포트폴리오 홍보 배너"
    /></v-carousel-item>
  </v-carousel>
  <div style="text-align: center">
    <!-- <v-divider :thickness="3" color="info" style="width:30%;     border-style: double;
    margin: auto; margin-top:100px;"></v-divider> -->
    <p
      style="
        margin-top: 170px;
        margin-bottom: 40px;
        color: rgba(0, 179, 255, 0.826);
        font-size: 30px;
        font-weight: 600;
      "
    >
      ✨ 해달의 오늘의 추천 상품 ✨
    </p>
    <v-card
      class="recommend"
      style="
        width: 50%;
        height: 10%;
        margin: auto;
        margin-bottom: 180px;
        color: rgb(0, 63, 90);
        border-radius: 30px;
        vertical-align: middle !important;
      "
    >
      <div>
        <div class="text-h5 mb-3" style="font-weight: bolder">
          {{ recommend.productName }}
        </div>
        <div class="text-overline mb-3">
          <span>
            <b>상품 기간</b> {{ recommend.period }}개월, <b>금리</b>
            {{ recommend.interestRate }}%, <b>최소 가입 금액</b>
            {{ recommend.requiredStartMoney }}원
          </span>
          <div style="font-size: 14px; font-weight: bold">
            "{{ recommend.shortInfo }}상품"
          </div>
        </div>
        <v-btn
          @click="detail(recommend)"
          style="
            background: rgba(0, 179, 255, 0.826);
            color: white;
            font-weight: bold;
            border-radius: 15px;
            width: 200px;
            height: 40px;
          "
          >추천 상품 정보 보기</v-btn
        >
      </div>
    </v-card>
  </div>
  <div
    style="
      text-align: center;
      margin: 60px 0rem -10px 0rem;
      font-weight: 600;
      height: 300vh;
    "
  >
    <div
      style="
        display: flex;
        justify-content: center;
        align-items: center;
        margin-bottom: -50px;
        background-color: rgba(0, 179, 255, 0.158);
        height: 100vh;
        margin-top: 90px;
      "
    >
      <div style="display: flex; flex-direction: column; align-items: center">
        <div
          style="
            margin-bottom: 40px;
            margin-top: -80px;
            background-color: white;
            border-radius: 100%;
            text-align: center;
            font-size: 50px;
            width: 100px;
            height: 100px;
            padding: 10px;
          "
        >
          🐚
        </div>
        <p
          style="
            color: rgba(0, 179, 255, 0.826);
            line-height: 150%;
            font-size: 50px;
          "
        >
          해달과 함께<br />해와 달이 뜨고 지듯 꾸준히 저축해보세요!
        </p>
      </div>
    </div>

    <div style="height: 100vh">
      <p style="font-size: 24px; font-weight: 500; margin-top: 160px">
        꾸준히 조개를 모으는 해달이 여러분의 저축을 도와드릴게요!
      </p>
      <div
        style="margin: auto; display: flex; justify-content: center; width: 70%"
      >
        <img
          src="@/assets/img/main-image1.png"
          class="first-image"
          alt="해달 추천상품 기능 홍보 이미지"
        />
      </div>
      <p
        style="
          font-size: 18px;
          color: balck;
          font-weight: 500;
          line-height: 200%;
          margin-bottom: 100px;
        "
      >
        해달이 추천해드리는<br />
        <span
          style="
            font-size: 20px;
            color: rgb(0, 179, 255);
            background-color: rgba(0, 179, 255, 0.158);
            font-weight: bolder;
            padding: 3px;
          "
          >"여러분에게 딱 맞는 상품"</span
        >을 만나보세요.
      </p>
      <div style="background: rgba(238, 244, 248, 0.457)">
        <img
          src="@/assets/img/haedal-mockup.png"
          class="mockup"
          alt="해달 목업 이미지"
        />
      </div>
      <div>
        <p
          style="
            margin: 150px 0px 60px 0px;
            color: rgba(0, 179, 255, 0.826);
            font-size: 40px;
          "
        >
          지금 시작해 보세요!
        </p>
        <v-btn
          style="
            background: rgba(0, 179, 255, 0.826);
            color: white;
            font-weight: bold;
            border-radius: 30px;
            margin-bottom: 1rem;
            width: 400px;
            height: 60px;
            font-size: 20px;
          "
          href="/products"
          >상품 보러가기</v-btn
        >
      </div>
    </div>
  </div>
  <div
    style="
      background: linear-gradient(
        to top,
        rgba(0, 162, 255, 0.354),
        rgba(255, 255, 255, 0)
      );
      height: 180px;
      margin-top: 500px;
      display: block;
    "
  ></div>
</template>

<script setup>
import axios from "axios";
import { watchEffect, ref, onMounted } from "vue";
import router from "../router";
import { mdiConsoleNetwork } from "@mdi/js";

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: "https://backend.haedal.store", // 서버의 주소
  // baseURL: 'http://15.164.189.153:8080',
});

// 서버에서 받아오는 정보
const listData = ref([]);
const recommend = ref({});
// 데이터를 가져오는 함수
function fetchData() {
  axiosInstance.get("/products").then((res) => {
    let tempArr = [...res.data];
    tempArr.forEach((item) => {
      listData.value.push(item);
    });
    // console.log(listData)
    recommend.value =
      listData.value[Math.floor(Math.random() * listData.value.length)];
    // console.log(recommend.value)

    // 데이터를 Local Storage에 저장
    localStorage.setItem("data", JSON.stringify(recommend.value));
  });
}

// 페이지가 로드될 때 데이터를 가져오거나 Local Storage에서 데이터를 로드
onMounted(() => {
  const storedData = localStorage.getItem("data");
  if (storedData) {
    // Local Storage에 데이터가 있는 경우, 저장된 데이터를 사용
    recommend.value = JSON.parse(storedData);
    // console.log(recommend.value)
  } else {
    // Local Storage에 데이터가 없는 경우, 데이터를 가져와서 저장
    fetchData();
  }

  // 오전 12시에 주기적으로 데이터를 업데이트
  setInterval(() => {
    const now = new Date();
    if (now.getHours() === 0 && now.getMinutes() === 0) {
      fetchData();
    }
  }, 60000); // 1분마다 체크 (조절 가능)
});

//추천 상품 상세 정보 보기 버튼
const detail = (item) => {
  const productId = item.productId;
  const productName = item.productName;
  // console.log(productName);
  // console.log(item.deposit);
  if (item.deposit == true) {
    router.push({
      name: "subscribeD",
      params: {
        id: productId,
      },
    });
  } else if (item.deposit == false) {
    router.push({
      name: "subscribeI",
      params: {
        id: productId,
      },
    });
  }
};
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
}

.recommend {
  box-shadow: -4px 5px 20px 0 rgba(51, 96, 133, 0.122),
    12px -12px 16px rgba(255, 255, 255, 0.25) !important;
  padding: 40px !important;
  justify-content: center;

  &:hover {
    transition: transform 0.5s ease;
    transform: scale(1.05);
    box-shadow: -5px 5px 20px 0 rgba(0, 179, 255, 0.264),
      12px -12px 16px rgba(0, 179, 255, 0.046) !important;
  }
}

.banner {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.banner img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.mockup {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  /* 이미지를 블록 레벨 요소로 설정 */
  margin: 0 auto;
  /* 가운데 정렬 (선택 사항) */
}

.recommend b {
  background-color: rgba(0, 179, 255, 0.264);
  padding: 3px 6px;
  border-radius: 20px;
  color: rgba(0, 68, 128, 0.826);
}

.v-btn--size-x-small {
  color: rgba(0, 179, 255, 0.696) !important;
  margin-top: 20px;
  font-size: 20px !important;
}
</style>
