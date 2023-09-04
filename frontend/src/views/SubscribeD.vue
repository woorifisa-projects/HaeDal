<template>
    <div style="background: linear-gradient(to bottom, rgba(0, 162, 255, 0.354), rgba(255, 255, 255, 0)); height: 160px;">
    </div>

    <div class=submitForm>
        <form @submit.prevent="submitForm">
            <h2>{{ listData.productName }} 상품 정보</h2><br class=".mb-8">
            <div class=datas>
                <div style="margin-bottom:4rem">
                    <b>상품 설명</b>
                    <p
                        style="font-size: 20px; color:rgb(0, 162, 255); margin-top: 1.6rem; margin-bottom :0.3rem; font-weight: 700; font-family: 'Noto Serif KR', serif;">
                        "{{ listData.shortInfo }}"</p>
                    <br>
                    {{ listData.longInfo }}
                </div>
                <div style="margin-bottom:1rem">
                    <b>상품 가입 최소 금액</b>
                    {{ listData.requiredStartMoney }} 원
                    <br>
                    <b>상품 가입 최대 금액</b>
                    {{ listData.maxProductMoney }} 원
                    <br>
                    <b>금리</b>
                    {{ listData.interestRate }} %
                    <br>
                    <b>가입기간</b>
                    {{ listData.period }} 개월
                </div>
                <br>
                <div>
                    <b><label for="startMoney">시작 구독료</label></b>
                    <input type="number" id="startMoney" v-model="startMoney">
                </div>
                <div style="margin: 2rem;">
                    <p style="font-size: 12px;font-weight: 300; margin-bottom: 0;">입력한 시작금액을 바탕으로</p>
                    <v-btn
                        style=" font-size: 18px; font-weight: 600; color: rgb(0, 162, 255); padding: 0.4rem 2rem; margin-bottom: 1rem; box-shadow: -2px 4px 10px 0px rgba(0, 0, 0, 0.066) ;"
                        @click="calculate" type="button">만기시 금액 예상하기
                    </v-btn>
                    <p
                        v-show="calculatedAmount !== null && calculatedAmount !== 0 && calculatedAmount <= listData.maxProductMoney">
                        만기시 예상 금액은 {{ calculatedAmount }}원
                        입니다!
                    </p>
                    <p v-show="calculatedAmount > listData.maxProductMoney">입력하신 금액이 최대 금액을 초과하였습니다.</p>
                </div>
                <div>
                    <div class="favorite" @click="dibs(listData.productId)" style="cursor:pointer;">
                        <img v-if="isDibs === true" src='@/assets/img/favorite.png'>
                        <img v-else src='@/assets/img/favorite_border.png'>
                    </div>
                </div>
                <v-btn class=" button-style" @click="openModal" type="button">
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
                            <input type="text" id="authenticationNumber" v-model="formData.authenticationNumber" required>
                        </div>

                        <div>
                            <b><label for="startMoney">시작 구독료</label></b>
                            <input type="number" id="startMoney" v-model="formData.startMoney" required>
                        </div>
                        <div style="margin-bottom:13px">
                            <b>상품 가입 가능 금액</b><br>
                            최소 {{ listData.requiredStartMoney }} 원 ~최대{{ listData.maxProductMoney }} 원
                        </div>

                        <v-btn class="button-style" variant="outlined" type="submit">
                            신청하기
                        </v-btn>
                        <v-btn class="button-style" @click="closeModal">취소</v-btn>
                    </div>
                </div>
            </div>
        </form>
    </div>
</template>
  
<script setup>
import axios from 'axios'
import { ref, watchEffect } from 'vue'
import { useRouter, useRoute } from 'vue-router';
import router from '../router'
import { defineProps } from 'vue';
import { useAuthStore } from '@/store/app';

const authStore = useAuthStore();

// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // 서버의 주소
    // baseURL: 'http://15.164.189.153:8080',

})

const listData = ref({});
const calculatedAmount = ref(null);
const showModal = ref(false);
const isDibs = ref(false);

const route = useRoute();
const productId = route.params.id;
const currentPath = `/subscribe/${productId}`;

watchEffect(() => {
    axiosInstance.get(`${currentPath}`
    ).then((res) => {
        console.log(res.data)
        listData.value = res.data
    })
    // 찜되어있는지 확인
    axiosInstance.get(`/dibs/${productId}/check`, {
        headers: {
            Authorization: `Bearer ${authStore.accessToken}`
        }
    }).then((res) => {
        isDibs.value = res.data; // 서버에서 전달된 찜 여부
        console.log(isDibs.value);
    }).catch((error) => {
        // 로그인 되어 있지 않을 시 무조건 false
        isDibs.value = false;
    });

})
console.log(listData)

//찜하기 버튼 누를 시
const dibs = (productId) => {
    if (isDibs.value === false) {
        console.log("찜!")
        axios({
            method: "post",
            url: `http://localhost:8080/dibs/${productId}/add`,
            headers: {
                Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 토큰 포함
            },
        }).catch((error) => alert("로그인 후 이용 가능한 서비스 입니다"))
        isDibs.value = true;
    } else if (isDibs.value === true) {
        console.log("찜 취소")
        axios({
            method: "delete",
            url: `http://localhost:8080/dibs/${productId}/delete`,
            headers: {
                Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 토큰 포함
            },
        })
        isDibs.value = false;
    }
}

const formData = {
    authenticationNumber: '',
    startMoney: ''
};

const calculate = () => {
    const money = parseFloat(startMoney.value);
    if (money) {
        calculatedAmount.value = parseFloat(money + (money * (listData.value.interestRate) / 100)).toFixed(2);
    } else {
        calculatedAmount.value = null;
    }
};

const openModal = () => {
    showModal.value = true;
    axiosInstance.get(`${currentPath}/semi`, {
        headers: {
            Authorization: `Bearer ${authStore.accessToken}`
        }
    }).then((res) => {
        console.log(res);
        listData.value = res.data;
    }).catch((error) => {
        router.push('/error');
    })
};

const closeModal = () => {
    showModal.value = false;
};


const submitForm = () => {
    const url = `http://localhost:8080/subscribe/${productId}/final`;
    // `http://15.164.189.153:8080/subscribe/${productId}/D`;


    // productId가 유효한 경우에만 요청을 보냅니다.
    axios.post(url, formData,
        {
            headers: {
                Authorization: `Bearer ${authStore.accessToken}`
            }
        },)
        .then(response => {
            console.log('신청 성공', response);
            alert("신청이 완료 되었습니다.");
            router.push('/success'); // 성공한 경우, 리다이렉트 또는 다른 처리를 수행합니다.
        })
        .catch(error => {
            console.error('에러 발생', error);
            alert("정보가 올바르지 않습니다.");
        });
};

</script>
  
<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Serif+KR:wght@500&display=swap');

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
</style>
  