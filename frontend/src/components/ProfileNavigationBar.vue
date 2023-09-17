<template>
  <v-navigation-drawer
    theme="ligth"
    permanent
    minimize
    class="custom-navigation-bar"
  >
    <v-list color="transparent">
      <v-list-item
        prepend-icon="mdi-account-box"
        title="내 프로필"
        href="/mypage"
      ></v-list-item>
      <v-list-item
        prepend-icon="mdi mdi-account-cog"
        title="보안 설정"
        href="/securitysetting"
      ></v-list-item>
      <v-list-item
        prepend-icon="mdi mdi-chart-areaspline"
        title="포트폴리오"
        href="/chartportfolio"
      ></v-list-item>
      <v-list-item
        prepend-icon="mdi mdi-view-list"
        title="가입 상품 상세정보"
        href="/portfolio"
      ></v-list-item>
      <v-list-item
        prepend-icon="mdi mdi-heart-outline"
        title="찜한 상품"
        href="/portfoliodibs"
      ></v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
.custom-navigation-bar {
  background: linear-gradient(
    to bottom,
    rgb(255, 255, 255) 5%,
    rgb(172, 216, 255)
  );
  z-index: inherit;
  border: none;
  height: 100% !important;
  border-bottom: 1px solid transparent;
  padding-left: 2rem;
  padding-top: 1rem;
}

.v-list-item-title {
  font-family: "Noto Sans KR", sans-serif !important;
}
</style>
