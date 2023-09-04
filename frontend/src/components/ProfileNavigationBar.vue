<template>
  <v-navigation-drawer
    style="background:linear-gradient(to bottom, rgba(0, 162, 255, 0.354), rgba(255, 255, 255, 0));z-index: inherit;"
    theme="ligth" permanent minimize>
    <v-list color="transparent">
      <v-list-item prepend-icon="mdi-account-box" title="내 프로필" href="/mypage"></v-list-item>
      <v-list-item prepend-icon="mdi-view-dashboard" title="보안 설정" href="/securitysetting"></v-list-item>
      <v-list-item prepend-icon="mdi-gavel" title="계좌 관리" href="/portfolio"></v-list-item>
    </v-list>


  </v-navigation-drawer>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped></style>
