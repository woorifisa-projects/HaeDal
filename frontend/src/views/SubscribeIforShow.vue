<template>
    <div style="background: linear-gradient(to bottom, rgba(0, 162, 255, 0.354), rgba(255, 255, 255, 0)); height: 160px;">
    </div>
    <div class=submitForm>
        <form @submit.prevent="submitForm">
            <h2>{{ listData.productName }} 상품 정보</h2><br class=".mb-8">
            <div class=datas>
                <div>
                    <b>상품 설명</b><br>
                    {{ listData.shortInfo }} <br>
                    {{ listData.longInfo }}
                </div><br class=".mb-6">
                <div>
                    <b>상품 가입 최소 구독료</b>
                    {{ listData.requiredStartMoney }} 원
                    <br>
                    <b>상품 가입 최대 구독료</b>
                    {{ listData.maxProductMoney }} 원
                </div>
                <div>
                    <b>금리</b>
                    {{ listData.interestRate }} %
                </div>
                <div>
                    <b>가입기간</b>
                    {{ listData.period }} 개월
                </div>
                <div style="margin-bottom: 60px;"></div>

                <v-btn class="button-style" variant="outlined" @click="openModal">
                    해지하기
                </v-btn>
                <v-spacer></v-spacer>
                <v-btn class="button-style" variant="outlined" >
                    돌아가기
                </v-btn>
            </div>

             <!-- 모달 창 -->
             <div v-if="showModal" class="blur-background" @click="closeModal"></div>
                <div v-if="showModal" class="modal">
                    <!-- 모달 내용: 계좌번호와 인증 번호 입력 -->
                    <div class="modal-content">
                        <h2>{{ listData.productName }} 상품 해지</h2>
                        <v-icon color="error">mdi-alert-circle</v-icon>
                        <div class="warning-message">
                        <p>
                            ⚠ 중도 해지 시 투자 원금만 반환됩니다 ⚠
                        </p>
                        <p>
                            고객님,

        저희 금융 상품에 투자해 주셔서 감사합니다. 중요한 안내를 드립니다.

        금융 상품 중도 해지 시, 투자 원금만을 반환받으실 수 있습니다. 이것은 해당 상품의 특성에 따른 것으로, 중도 해지 시 수익을 포함한 추가 금액을 받지 못하게 됩니다. 따라서 투자 기간이 끝나지 않았을 경우, 원금의 일부 손실이 발생할 수 있습니다.

        중요한 결정을 하기 전에 모든 옵션을 신중히 고려하시기 바랍니다. 투자 상품에 대한 이해를 높이고, 본인의 금융 목표를 달성하기 위해 최선의 선택을 하실 수 있도록 노력하겠습니다.

        감사합니다.
                        </p>
                        </div>                   


                        <div>

                    
                        <div>
                            <b>인증 번호</b>
                            <input type="text" id="authenticationNumber" v-model="formData.authenticationNumber" required>
                        </div>
                        
                        <div style="margin-bottom: 30px;">
                            <v-btn class="button-style" variant="outlined" type="submit">
                                해지하기
                            </v-btn>
                            <v-spacer></v-spacer>
                            <v-btn class="button-style" @click="closeModal">취소</v-btn>
                        </div>
                    </div>
                </div>
            </div>
                
            <div v-if="showfinalModal" class="blur-background" @click="closeModal"></div>
                <div v-if="showfinalModal" class="finalmodal">
                    <!-- 모달 내용: 해지완료 -->
                    <div style="margin:100px">
                    <v-card class="success-card">
                        <h1>상품 해지가 완료되었습니다</h1>
                        <div class=" success">
                        <img src='@/assets/img/leave.jpg'>
                        </div>
                        <h1>{{ result }}</h1>
                        <p style="margin-bottom: 18px;font-size: 13px;">해달과 계속해서 저축해요...</p>
                        <v-btn class="btn" @click="goportfolio">확인</v-btn>
                    </v-card>
                    <div class=" overlay">
                    </div>
                </div>
            </div>

        </form>
    </div>
</template>
  
<script setup>
import axios from 'axios'
import { ref, watchEffect } from 'vue'
import { useRoute } from 'vue-router';
import router from '../router'
import { useAuthStore } from '@/store/app';

// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: 'https://backend.haedal.store', // 서버의 주소
    // baseURL: 'http://15.164.189.153:8080',
})

const authStore = useAuthStore();
const listData = ref({});
const showModal = ref(false);
const showfinalModal = ref(false);
const result = ref(0); //환급액
const route = useRoute();
const productId = route.params.id;
const currentPath = `/subscribe/${productId}`;

watchEffect(() => {
    axiosInstance.get(`${currentPath}`, {
        headers: {
            Authorization: `Bearer ${authStore.accessToken}`
        }
    }).then((res) => {
        console.log(res.data)
        listData.value = res.data
    })
})

console.log(listData);

const goportfolio = () => {
    router.push('/portfolio');
}

const formData = {
    authenticationNumber: '',
    startMoney: ''
};

const submitForm = () => {
    const url = `https://backend.haedal.store/subscribe/${productId}/cancle`;

// productId가 유효한 경우에만 요청을 보냅니다.
axios.delete(url,
    {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    },)
    .then((res) => {
    showModal.value = false;
    showfinalModal.value = true;
    result.value = res.data;
    console.log(res);
    
}).catch((error) => {
    router.push('/error');
})
};

const openModal = () => {
    showModal.value = true;
   
};

const closeModal = () => {
    showModal.value = false;
};

const openfinalModal = () => {
    showfinalModal.value = true;
   
};

const closefinalModal = () => {
    showfinalModal.value = false;
};


</script>
  
 
<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Serif+KR:wght@500&display=swap');


.warning-message {
  background-color: #ffcccc;
  border: 1px solid #ff3333;
  padding: 10px;
  text-align: center;
  font-weight: bold;
  color: #ff3333;
}


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
    width: 20rem;
    border-radius: 10px;
    box-shadow: none;
    background: rgba(0, 179, 255, 0.826);
    color: white;
    margin-top: 14px;
    font-weight: bolder;
    font-size: 18px;
}


.datas b {
    color: rgba(0, 0, 0, 0.712);
    background-color: rgba(0, 162, 255, 0.225);
    padding: 1px;
    border-radius: 2px;
    font-weight: 550;
    font-size: 15px;
    margin-bottom: 4rem;
    line-height: 40px;
}

.datas {
    font-size: 14px;
    margin-bottom: 200px
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

.finalmodal {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 9999;
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
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

.success {
  width: 200px;
  height: 200px;
  border-radius: 70%;
  overflow: hidden;
  margin: auto;
  margin-bottom: 30px;
}

.success img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.success-card {
  box-shadow: -4px 4px 10px 0 rgba(51, 96, 133, 0.252);
  width: 100%;
  text-align: center;
  padding: 40px;
  margin: auto;
  z-index: 2;
}

.btn {
  width: 8rem;
  border-radius: 8px;
  box-shadow: none;
  background: rgba(0, 179, 255, 0.826);
  color: white;
  margin: 8px;
  font-weight: bolder;
  font-size: 16px;
}
</style>
  