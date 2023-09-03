<template>
    <v-card class="error">
        <div class="error-icon">
            <img src='@/assets/img/exception.png'>
        </div>
        <h1>로그인이 필요한 서비스 입니다.</h1>
        <p style="margin-bottom: 30px;">로그인 후 다시 이용해 주세요!</p>
        <v-btn href="/login" class="btn">로그인</v-btn>
        <v-btn @click="goBack" class="btn">돌아가기</v-btn>
    </v-card>
    <div class=" overlay"></div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
const goBack = () => {
    router.go(-1);
}
</script>

<style lang="scss" scoped>
.error-icon {
    width: 30%;
    margin: auto;
    margin-top: 30px;
}

.error-icon img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

h1 {
    color: rgba(0, 179, 255, 0.826);
}

p {
    background-color: rgba(0, 162, 255, 0.225);
    width: 45%;
    border-radius: 5px;
    margin: auto;
    margin-top: 10px;
    font-weight: bold;
    color: rgb(57, 113, 174);
}

.error {
    box-shadow:
        -4px 4px 10px 0 rgba(51, 96, 133, 0.252),
        12px -12px 16px rgba(255, 255, 255, 0.25) !important;
    padding: 40px !important;
    justify-content: center;
    width: 40%;
    height: 500px;
    margin: auto;
    margin-top: 5%;
    margin-bottom: 5%;
    color: rgb(0, 63, 90);
    border-radius: 30px;
    vertical-align: middle !important;
    text-align: center;
    z-index: 2;


    align-items: center;
    justify-content: center;
}

.btn {
    width: 6rem;
    border-radius: 9px;
    box-shadow: none;
    background: rgba(0, 179, 255, 0.826);
    color: white;
    margin: 8px;
    font-weight: bolder;
    font-size: 16px;
}


.overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(178, 224, 255, 0.326);
    /* 불투명한 배경색 */
    z-index: 1;
}
</style>