<template>
    <div style="background: linear-gradient(to bottom, rgba(0, 162, 255, 0.354), rgba(255, 255, 255, 0)); height: 160px;">
    </div>

    <div class=submitForm>
        <form @submit.prevent="submitForm">
            <h2>{{ listData.productName }} 상품 신청</h2><br class=".mb-8">
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
                    <b>출금 계좌번호</b>
                    {{ listData.accountNumber }}
                </div>
                <div>
                    <b><label for="authenticationNumber">인증 번호</label></b>
                    <input type="text" id="authenticationNumber" v-model="formData.authenticationNumber" required>
                </div>
                <div>
                    <b><label for="startMoney">시작 구독료</label></b>
                    <input type="number" id="startMoney" v-model="formData.startMoney" required>
                </div>
                <div style="margin: 2rem;">
                    <p style="font-size: 12px;font-weight: 300; margin-bottom: 0;">입력한 시작금액을 바탕으로</p>
                    <v-btn
                        style=" font-size: 18px; font-weight: 600; color: rgb(0, 162, 255); padding: 0.4rem 2rem; margin-bottom: 1rem; box-shadow: -2px 4px 10px 0px rgba(0, 0, 0, 0.066) ;"
                        @click="calculate(formData.startMoney)" type="button">만기시 금액 예상하기
                    </v-btn>
                    <p v-show="calculatedAmount !== null && calculatedAmount !== 0"> 만기시 예상 금액은 {{ calculatedAmount }}원
                        입니다!
                    </p>
                </div>

                <v-btn class=" button-style" type=" submit">
                    신청하기
                </v-btn>
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
    // baseURL: 'http://localhost:8080', // 서버의 주소
    baseURL: 'http://15.164.189.153:8080',
    // withCredentials: true // CORS 요청에 관련된 설정을 포함
})

const listData = ref({});
const calculatedAmount = ref(null);

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
console.log(listData)

const formData = {
    authenticationNumber: '',
    startMoney: ''
};

const calculate = (money) => {
    if (money) {
        calculatedAmount.value = parseFloat(money + (money * (listData.value.interestRate) / 100)).toFixed(2);
    } else {
        calculatedAmount.value = null;
    }
};

const submitForm = () => {
    const url = `http://15.164.189.153:8080/subscribe/${productId}/D`;


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
            router.push(/*TODO : 성공 화면 라우터 연결 */'/success'); // 성공한 경우, 리다이렉트 또는 다른 처리를 수행합니다.
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
    width: 25rem;
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
}

h2 {
    font-size: 45px;
    margin: 2rem 0rem 2rem 0rem;
}

form {
    margin-bottom: 10rem;
}
</style>
  