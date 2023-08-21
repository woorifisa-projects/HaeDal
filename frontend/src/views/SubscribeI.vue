<template>
    <div>
        <form @submit.prevent="submitForm">
            <label for="accountNumber">계좌번호</label>
            <input type="text" id="accountNumber" v-model="formData.accountNumber" required>

            <label for="authenticationNumber">인증번호</label>
            <input type="text" id="authenticationNumber" v-model="formData.authenticationNumber" required>

            <label for="startMoney">시작 금액</label>
            <input type="number" id="startMoney" v-model="formData.startMoney" required>

            <button type="submit">신청하기</button>
        </form>
    </div>
</template>
  
<script>
import axios from 'axios'
import { ref, watchEffect } from 'vue'
import router from '../router'

const currentPath = window.location.pathname
const productIdMatch = currentPath.match(/\/subscribe\/(\d+)\/I/);
const productId = productIdMatch

export default {
    setup() {
        const formData = {
            accountNumber: '',
            authenticationNumber: '',
            startMoney: ''
        };

        const submitForm = () => {
            const url = `/subscribe/${productId}/I`;

            // POST 요청을 서버로 보낸다.
            axios.post(url, formData)
                .then(response => {
                    console.log('신청 성공', response);
                    // 성공한 경우, 리다이렉트 또는 다른 처리를 수행합니다.
                })
                .catch(error => {
                    console.error('에러 발생', error);
                    // 에러 처리 로직을 추가할 수 있습니다.
                });
        };

        return {
            formData,
            submitForm
        };
    }
};
</script>
  
<style lang="scss" scoped>
/* 스타일을 추가하세요 */
</style>
  