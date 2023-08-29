<template>

      <v-navigation-drawer
        class="bg-deep-purple"
        theme="dark"
        permanent
        minimize
      >
        <v-list color="transparent">
          <v-list-item prepend-icon="mdi-account-box" title="내 프로필" href="#"></v-list-item>
          <v-list-item prepend-icon="mdi-view-dashboard" title="보안 설정" href="/securitysetting"></v-list-item>
          <v-list-item prepend-icon="mdi-gavel" title="계좌 관리" href="#"></v-list-item>
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



</style>