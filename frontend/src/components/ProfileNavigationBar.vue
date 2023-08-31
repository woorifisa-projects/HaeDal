<template>
  <v-navigation-drawer style="background: rgba(0, 162, 255, 0.088);" theme="ligth" permanent minimize>
    <v-list color="transparent">
      <v-list-item prepend-icon="mdi-account-box" title="내 프로필" href="/mypage"></v-list-item>
      <v-list-item prepend-icon="mdi-view-dashboard" title="보안 설정" href="/securitysetting"></v-list-item>
      <v-list-item prepend-icon="mdi-gavel" title="계좌 관리" href="/portfolio"></v-list-item>
    </v-list>

    <template v-slot:append>
      <div class="pa-2">
        <v-btn block>
          Logout
        </v-btn>
      </div>
    </template>

  </v-navigation-drawer>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>
button {
  width: 10rem;
  border-radius: 10px;
  box-shadow: none;
  background: rgb(0, 149, 255);
  color: rgb(255, 255, 255);
  margin-top: 14px;
  font-weight: bolder;
  font-size: 13px;
}
</style>
