<template>
  <navigation-bar> </navigation-bar>
  <v-card>
    <div class="vertical-center">
      <v-btn class="btn" variant="outlined" href="/changePW">
        비밀번호 변경
      </v-btn>

      <v-btn class="leave-btn" variant="outlined" href="/leave">
        회원 탈퇴
      </v-btn>
    </div>
  </v-card>
</template>

<script setup>
import NavigationBar from '@/components/ProfileNavigationBar.vue';
</script>

<style lang="scss" scoped>
.v-card {
  text-align: center;
  justify-content: center;
  height: 200px;
  width: 30rem;
  margin: auto;
  margin-top: 10rem;
  margin-bottom: 10rem;
  box-shadow: -4px 4px 10px 0 rgba(51, 96, 133, 0.252),
    12px -12px 16px rgba(255, 255, 255, 0.25);
  padding: 15px;
  z-index: 10;
}

.btn {
  width: 10rem;
  height: 3rem;
  border-radius: 15px;
  box-shadow: none;
  background: rgba(0, 179, 255, 0.826);
  color: white;
  margin: 8px;
  font-weight: bolder;
  font-size: 16px;

  &:hover {
    transition: transform 0.5s ease;
    transform: scale(1.02);
    background-color: rgba(95, 207, 255, 0.826);
  }
}

.leave-btn {
  width: 10rem;
  height: 3rem;
  border-radius: 15px;
  box-shadow: none;
  background: rgba(0, 179, 255, 0.826);
  color: white;
  margin: 8px;
  font-weight: bolder;
  font-size: 16px;

  &:hover {
    transition: transform 0.5s ease;
    transform: scale(1.02);
    background-color: rgb(255, 95, 95);
  }
}

.vertical-center {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
</style>