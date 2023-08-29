<template>
    <div style="align-items: center; text-align: center; justify-content: center;">
        <div class="success">
            <img src='@/assets/img/success.jpg'>
        </div>
        <h2>신청이 완료되었습니다!</h2>
        <p>해달과 함께 해와 달이 뜨고 지듯 저축해 보아요!</p>
        <v-btn>홈 페이지</v-btn>
        <v-btn>마이페이지</v-btn>
    </div>
</template>

<script setup>

</script>

<style lang="scss" scoped>
.success {
    width: 230px;
    height: 230px;
    border-radius: 70%;
    overflow: hidden;
}

.success img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

button {
    width: 8rem;
    border-radius: 8px;
    box-shadow: none;
    background: rgba(0, 179, 255, 0.826);
    color: white;
    margin: 14px;
    font-weight: bolder;
    font-size: 16px;
}
</style>