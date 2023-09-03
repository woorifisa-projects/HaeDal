<template>
    <navigation-bar>  </navigation-bar>
    <v-btn class = "btn" variant="outlined" href="/changePW">
  홈페이지 비밀번호 변경
</v-btn>

<v-btn class = "btn" variant="outlined">
  계좌 비밀번호 변경
</v-btn>

<v-btn class = "btn" variant="outlined" href="/leave">
  회원 탈퇴
</v-btn>
</template>

<script setup>
import NavigationBar from '@/components/ProfileNavigationBar.vue';
</script>

<style lang="scss" scoped>
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