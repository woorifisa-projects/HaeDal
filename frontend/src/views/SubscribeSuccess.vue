<template>
    <div style="margin-top:130px">
        <v-card class="success-card">
            <div class=" success">
                <img src='@/assets/img/success.jpg'>
            </div>
            <h2>신청이 완료되었습니다!</h2>
            <p style="margin-bottom: 18px;font-size: 13px;">해달과 함께 해와 달이 뜨고 지 듯 꾸준히 저축해 보아요!</p>
            <v-btn href="/home" class="btn">홈 페이지</v-btn>
            <v-btn href="/mypage" class="btn">마이페이지</v-btn>
        </v-card>
        <div class=" overlay">
        </div>
    </div>
</template>

<script setup>

</script>

<style lang="scss" scoped>
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
    width: 30%;
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

.overlay-container {
    position: relative;
    /* 부모 컨테이너를 상대적으로 위치시킴 */
}

.overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(104, 177, 219, 0.098);
    /* 불투명한 배경색 */
    z-index: 1;
}
</style>