<template>
    <div class=submitForm>
        <form @submit.prevent="submitForm">
            <h3>{{ listData.productName }} 상품 신청</h3>
            <div class=datas>
                <div>
                    <label for="accountNumber">계좌번호</label>
                    <input type="text" id="accountNumber" v-model="formData.accountNumber" required>
                </div>
                <div>
                    <label for="authenticationNumber">인증번호</label>
                    <input type="text" id="authenticationNumber" v-model="formData.authenticationNumber" required>
                </div>
                <div>
                    <label for="startMoney">시작금액</label>
                    <input type="number" id="startMoney" v-model="formData.startMoney" required>
                </div>
                <div>
                    월 간 구독료 : {{ listData.subscription }} 원
                </div>
                <v-btn variant="outlined" type="submit">
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


// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // 서버의 주소
    withCredentials: "true" // CORS 요청에 관련된 설정을 포함
})

const listData = ref({});

const route = useRoute();
const productId = route.params.id;
const currentPath = `/subscribe/${productId}`;

watchEffect(() => {
    axiosInstance.get(`${currentPath}`).then((res) => {
        console.log(res.data)
        listData.value = res.data
    })
})

console.log(listData);

const formData = {
    accountNumber: '',
    authenticationNumber: '',
    startMoney: ''
};


const submitForm = () => {
    const url = `http://localhost:8080/subscribe/${productId}/I`;

    // productId가 유효한 경우에만 요청을 보냅니다.
    axios.post(url, formData)
        .then(response => {
            console.log('신청 성공', response);
            router.push(/*TODO : 성공 화면 라우터 연결 */); // 성공한 경우, 리다이렉트 또는 다른 처리를 수행합니다.
        })
        .catch(error => {
            console.error('에러 발생', error);
            alert("정보가 올바르지 않습니다.");
        });
};

</script>
  
<style lang="scss" scoped>
input {
    width: 500px;
    height: 32px;
    font-size: 15px;
    border: 0;
    margin: 0.5rem;
    border-radius: 15px;
    outline: none;
    padding-left: 10px;
    background-color: rgb(233, 233, 233);
}

div {
    text-align: center;
}
</style>
  