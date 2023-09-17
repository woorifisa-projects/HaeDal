<template>

  <div
    style="
      background: linear-gradient(
        to bottom,
        rgba(0, 162, 255, 0.354),
        rgba(255, 255, 255, 0)
      );
      height: 160px;
    "
  ></div>
  <div class="submitForm">
    <form @submit.prevent="submitForm">
      <h2>{{ listData.productName }} 상품 정보</h2>
      <br class=".mb-8" />
      <div class="datas">
        <div style="margin-bottom: 4rem">
          <b>상품 설명</b><br />
          <p
            style="
              font-size: 20px;
              color: rgb(0, 162, 255);
              margin-top: 1.6rem;
              margin-bottom: 0.3rem;
              font-weight: 700;
              font-family: 'Noto Serif KR', serif;
            "
          >
            "{{ listData.shortInfo }}"
          </p>
          <br />
          <p style="max-width: 40%; margin: auto; word-break: keep-all">
            {{ listData.longInfo }}
          </p>
        </div>
        <div class="desc" style="display: flex; justify-content: center">
          <span
            class="box"
            style="display: flex; justify-content: center; align-items: center"
          >
            <div
              style="margin-bottom: 1rem; text-align: left; min-width: 240px"
            >
              <h3 style="text-align: center">상품 정보</h3>
              <div style="display: flex; justify-content: space-between">
                <span style="text-align: left; margin-right: 20px">
                  <b>상품 가입 최소 구독료</b>
                  <br />
                  <b>상품 가입 최대 구독료</b>
                  <br />
                  <b>금리</b>
                  <br />
                  <b>가입기간</b>
                </span>
                <span style="text-align: left">
                  {{ listData.requiredStartMoney }} 원
                  <br />
                  {{ listData.maxProductMoney }} 원
                  <br />
                  {{ listData.interestRate }} %
                  <br />
                  {{ listData.period }} 개월
                </span>
              </div>

            </div>
            <br />
          </span>
          <span
            class="box"
            style="display: flex; justify-content: center; align-items: center"
          >
            <div>
              <h3>가입 후 얼마가 될지 예상해보세요!</h3>
              <div>
                <b><label for="startMoney">시작 구독료</label></b
                ><br />
                <input type="number" id="startMoney" v-model="startMoney" />
              </div>
              <p style="font-size: 13px; font-weight: 400; margin-bottom: -7px">
                입력한 시작금액을 바탕으로
              </p>
              <v-btn
                style="
                  font-size: 15px;
                  font-weight: 600;
                  color: rgb(0, 162, 255);
                  margin-bottom: 0rem;
                  box-shadow: none;
                  background-color: rgba(0, 179, 255, 0.093);
                "
                @click="calculate"
                type="button"
                >만기시 금액 예상하기
              </v-btn>
              <div style="height: 10px; color: rgb(0, 162, 255)">
                <p
                  v-show="
                    calculatedAmount !== null &&
                    calculatedAmount !== 0 &&
                    startMoney !== null &&
                    startMoney !== 0 &&
                    startMoney <= listData.maxProductMoney &&
                    startMoney >= listData.requiredStartMoney
                  "
                >
                  만기시 예상 금액은 {{ calculatedAmount }}원 입니다!
                </p>
                <p v-show="startMoney > listData.maxProductMoney">
                  입력하신 금액이 최대 금액을 초과하였습니다.
                </p>
                <p v-show="startMoney < listData.requiredStartMoney">
                  입력하신 금액이 최소 금액 미만입니다.
                </p>
              </div>
            </div>
          </span>
        </div>
        <v-btn
          @click="dibs(listData.productId)"
          style="
            margin-top: 14px;
            margin-right: 10px;
            border-radius: 10px;
            box-shadow: none !important;
            padding: 4px;
            box-shadow: 0px 0px 3px rgba(0, 162, 255, 0.578);
            justify-content: center;
          "
        >
          <div
            style="
              text-align: center;
              margin: auto;
              margin-top: 1px;
              display: flex;
              align-items: center;
              justify-content: center;
            "
          >
            <div
              class="favorite"
              style="
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
              "
            >
              <img v-if="isDibs === true" src="@/assets/img/favorite.png" />
              <img v-else src="@/assets/img/favorite_border.png" />
            </div>
          </div>
        </v-btn>
        <v-btn
          class="button-style"
          variant="outlined"
          @click="openModal"
          type="button"
        >
          신청하기
        </v-btn>

        <!-- 모달 창 -->
        <div v-if="showModal" class="blur-background" @click="closeModal"></div>
        <div v-if="showModal" class="modal">
          <!-- 모달 내용: 계좌번호와 인증 번호 입력 -->
          <div class="modal-content">
            <h2>{{ listData.productName }} 상품 가입</h2>

            <div>
              <b>출금 계좌번호</b>
              {{ listData.accountNumber }}
            </div>
            <div>
              <b>계좌 잔액</b>
              {{ listData.asset }}
            </div>
            <div>
              <b>인증 번호</b>
              <input
                type="text"
                id="authenticationNumber"
                v-model="formData.authenticationNumber"
                required
              />
            </div>
            <div>
              <b><label for="startMoney">시작 구독료</label></b>
              <input
                type="number"
                id="startMoney"
                v-model="formData.startMoney"
                required
              />
            </div>
            <div style="margin-bottom: 13px">
              <b>상품 가입 가능 금액</b><br />
              최소 {{ listData.requiredStartMoney }} 원 ~최대{{
                listData.maxProductMoney
              }}
              원
            </div>
            <div style="margin-bottom: 30px">
              <v-btn class="button-style" type="submit"> 신청하기 </v-btn>
              <v-btn class="button-style" @click="closeModal">취소</v-btn>
            </div>
          </div>
        </div>
      </div>

      <!--에러 모달창 설정-->
      <div
        v-if="showErrorModal === true"
        class="error-background"
        @click="showErrorModal = false"
      ></div>
      <div
        v-show="showErrorModal === true"
        class="modal"
        id="errorModal"
        role="dialog"
        style="width: 30%"
      >
        <div class="modal-content">
          <div class="modal-header">
            <div class="error-icon">
              <img src="@/assets/img/exception.png" />
            </div>
          </div>
          <div class="modal-body">
            <p id="errorText">{{ errorText }}</p>
          </div>
          <div class="modal-footer">
            <v-btn
              type="button"
              class="button-style"
              data-dismiss="modal"
              @click="showErrorModal = false"
              >닫기</v-btn
            >
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref, watchEffect } from "vue";
import { useRouter, useRoute } from "vue-router";
import router from "../router";
import { useAuthStore } from "@/store/app";

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: "https://backend.haedal.store", // 서버의 주소
  // baseURL: 'http://15.164.189.153:8080',
});

const route = useRoute();
const productId = route.params.id;
const currentPath = `/subscribe/${productId}`;
const authStore = useAuthStore();
const listData = ref({});

const calculatedAmount = ref(null);
const showModal = ref(false);
const showErrorModal = ref(false);
const isDibs = ref(false);

watchEffect(() => {
  axiosInstance.get(`${currentPath}`).then((res) => {
    // console.log(res.data);
    listData.value = res.data;
  });
  // 찜되어있는지 확인
  if (localStorage.getItem("accessToken")) {
    axiosInstance
      .get(`/dibs/${productId}/check`, {
        headers: {
          Authorization: `Bearer ${authStore.accessToken}`,
        },
      })
      .then((res) => {
        isDibs.value = res.data; // 서버에서 전달된 찜 여부
        // console.log(isDibs.value);
      })
      .catch((error) => {
        // 로그인 되어 있지 않을 시 무조건 false
        isDibs.value = false;
      });
  }
});

//찜하기 버튼 누를 시
const dibs = (productId) => {
  if (isDibs.value === false) {
    // console.log("찜!");
    console.log(productId);
    axios({
      method: "post",
      url: `https://backend.haedal.store/dibs/${productId}/add`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`, // 토큰 포함
      },
    })
      .then(() => {
        isDibs.value = true; // Vue 3에서는 ref를 사용하므로 .value 없이 값 변경
      })
      .catch((error) => alert("로그인 후 이용 가능한 서비스 입니다"));
  } else if (isDibs.value === true) {
    // console.log("찜 취소");
    axios({
      method: "delete",
      url: `https://backend.haedal.store/dibs/${productId}/delete`,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("accessToken")}`, // 토큰 포함
      },
    })
      .then(() => {
        isDibs.value = false; // Vue 3에서는 ref를 사용하므로 .value 없이 값 변경
      })
      .catch((error) => alert("로그인 후 이용 가능한 서비스 입니다"));
  }
};

const openModal = () => {
  showModal.value = true;
  axiosInstance
    .get(`${currentPath}/semi`, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    })
    .then((res) => {
      // console.log(res);
      listData.value = res.data;
    })
    .catch((error) => {
      router.push("/error");
    });
};

const closeModal = () => {
  showModal.value = false;
};

// console.log(listData);

const formData = {
  authenticationNumber: "",
  startMoney: "",
};

const calculate = () => {
  const money = parseFloat(startMoney.value);
  if (money) {
    let totalAmount = 0;
    const n = listData.value.period;

    for (let i = 0; i < n; i++) {
      totalAmount +=
        money + money * (listData.value.interestRate / 100) * (i / n);
    }

    calculatedAmount.value = parseFloat(totalAmount).toFixed(2);
  } else {
    // console.log("오류");
  }
};

const submitForm = () => {
  const url = `https://backend.haedal.store/subscribe/${productId}/final`;

  // productId가 유효한 경우에만 요청을 보냅니다.
  axios
    .post(url, formData, {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`,
      },
    })
    .then((response) => {
      // console.log(authStore.accessToken);
      // console.log("신청 성공", response);
      router.push("/success");
    })
    .catch((error) => {
      console.error("에러 발생", error);
      showModal.value = true;
      if (error.response && error.response.status === 400) {
        // 400 Bad Request 응답인 경우 모달 창을 열고 서버에서 전달한 메시지를 모달에 표시
        const errorText = document.getElementById("errorText");
        errorText.textContent = error.response.data;
        showErrorModal.value = true; // 모달 창 열기
      } else {
        // 다른 에러인 경우 일반 에러 메시지 출력
        alert("정보가 올바르지 않습니다.");
      }
    });
  closeModal(); // 모달 창 닫기
};
</script>

<style lang="scss" scoped>
@import url("https://fonts.googleapis.com/css2?family=Noto+Serif+KR:wght@500&display=swap");

.error-icon {
  width: 30%;
  margin: auto;
  margin-top: 5px;
}

.error-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.error-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.087);
  /* 블러 색상 및 투명도 설정 */
  backdrop-filter: blur(5px);
  /* 블러 효과 설정 */
  z-index: 9999;
  /* 모달 아래에 위치하도록 설정 */
}

input {
  width: 300px;
  height: 32px;
  font-size: 15px;
  border: 1px solid rgba(128, 128, 128, 0.095);
  margin: 0.5rem;
  border-radius: 10px;
  outline: none;
  padding-left: 10px;
  background-color: rgba(236, 236, 236, 0.337);
  box-shadow: -2px 4px 10px 0px rgba(0, 0, 0, 0.033) inset;
}

div {
  text-align: center;
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

.modal-content .button-style {
  width: 10rem;
  border-radius: 10px;
  box-shadow: none;
  background: rgba(0, 179, 255, 0.826);
  color: white;
  margin: 14px 5px 0px 5px;
  font-weight: bolder;
  font-size: 18px;
}

.datas b {
  color: rgba(0, 0, 0, 0.712);
  background-color: rgba(0, 162, 255, 0.225);
  padding: 0px 4px 2px 4px;
  border-radius: 5px;
  font-weight: 550;
  font-size: 15px;
  margin-bottom: 4rem;
  line-height: 40px;
}

.desc {
  margin: auto;
  padding-top: 10px;
  margin-bottom: 70px;
  height: 400px;
}

.desc .box {
  box-shadow: -2px 2px 10px 0 rgba(51, 96, 133, 0.118);
  margin-bottom: 10px;
  width: 400px;
  margin: 10px 20px 5px 20px;
  padding: 40px;
  border-radius: 30px;
  line-height: 300%;
}

.desc span h3 {
  font-size: 20px;
  margin-bottom: 15px;
}

.desc input {
  width: 200px;
}

input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.datas {
  font-size: 14px;
  margin-bottom: 200px;
}

h2 {
  font-size: 45px;
  margin: 2rem 0rem 2rem 0rem;
}

form {
  margin-bottom: 10rem;
}

/* 모달 스타일 */
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 9999;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  padding: 3rem;
  max-width: 80%;
  width: 50rem;
  overflow: hidden;
}

/* 블러 효과 스타일 */
.blur-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 145, 255, 0.2);
  /* 블러 색상 및 투명도 설정 */
  backdrop-filter: blur(5px);
  /* 블러 효과 설정 */
  z-index: 9998;
  /* 모달 아래에 위치하도록 설정 */
}

//찜 기능 관련
.favorite {
  width: 10px;
  margin: auto;
  margin-bottom: 10px;
}

.favorite img {
  width: 25px;
  height: 25px;
  object-fit: cover;
}
</style>
