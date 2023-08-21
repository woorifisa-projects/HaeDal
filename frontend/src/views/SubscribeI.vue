<template>
    <div class=submitForm>
        <form @submit.prevent="submitForm">
            <p>ㅇㅇㅇ상품 가입</p>
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
            <v-btn variant="outlined" type="submit">
                신청하기
            </v-btn>
        </form>
    </div>
</template>
  
<script setup>
import axios from 'axios'
import { ref, watchEffect } from 'vue'
import { useRouter, useRoute } from 'vue-router';
import router from '../router'

//const currentPath = window.location.pathname
//const productIdMatch = currentPath.match(/\/subscribe\/(\d+)\/I/);
//const productId = productIdMatch ? productIdMatch[1] : '';
const formData = {
    accountNumber: '',
    authenticationNumber: '',
    startMoney: ''
};

const route = useRoute();
const productId = route.params.id;

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
  