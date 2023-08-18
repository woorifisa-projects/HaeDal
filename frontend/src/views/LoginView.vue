<template>
  <v-container fluid fill-height> <!-- fluid 속성과 fill-height 속성을 사용하여 화면 전체를 채움 -->
    <v-row justify="center" align-content="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-text-field
          label="Username"
          :rules="rules"
          hide-details="auto"
        >
          <template v-slot:prepend>
            <svg-icon type="mdi" :path="path"></svg-icon>
          </template>
        </v-text-field>
      </v-col>
    </v-row>

    <v-row justify="center" align-content="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-text-field label="Password">
          <template v-slot:prepend>
            <svg-icon type="mdi" :path="path2"></svg-icon>
          </template>
        </v-text-field>
      </v-col>
    </v-row>

    <v-row justify="center" align-content="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-btn
          href="http://localhost:3000/signup"
          block
          rounded="lg"
          size="x-large"
        >
          로그인
        </v-btn>
      </v-col>
    </v-row>

    <v-row justify="center" align-content="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-btn
          href="http://localhost:3000/signup"
          block
          rounded="lg"
          size="x-large"
        >
          회원가입
        </v-btn>
      </v-col>
    </v-row>
    
  </v-container>
</template>

<script>
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiAccount } from '@mdi/js';
import { mdiLock } from '@mdi/js';

export default {
  name: "my-cool-component",

  components: {
    SvgIcon
  },

  data: () => ({
    rules: [
      value => !!value || '필수입력',
      value => (value && value.length >= 3) || '아이디는 최소 3글자 이상이어야합니다',
      value => (value && value.length <= 15) || '아이디는 최대 15글자 이하여야합니다',
    ],
    path: mdiAccount,
    path2: mdiLock,
  }),
}
</script>

<style>
/* 필요한 경우 추가적인 스타일 적용 */
</style>