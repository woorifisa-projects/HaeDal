<template>
  <v-card>
    <v-layout class = "navigation" >
      <v-navigation-drawer
        class="bg-deep-purple"
        theme="dark"
        permanent
      >
        <v-list color="transparent">
          <v-list-item prepend-icon="mdi-account-box" title="내 프로필"></v-list-item>
          <v-list-item prepend-icon="mdi-view-dashboard" title="보안 설정" href="#"></v-list-item>
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
      <v-main style="height: 45rem"></v-main>
    </v-layout>
  </v-card>
</template>

<script>
export default {

}
</script>

<style lang="scss" scoped>


</style>