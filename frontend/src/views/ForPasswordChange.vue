<template>
    <navigation-bar>  </navigation-bar>
    <form @submit.prevent="submit">
  <v-card-text>
            <span class="text-caption text-grey-darken-1">
                변경하실 비밀번호를 입력해주세요
            </span>
            <v-text-field
            v-model="password"
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            :type="visible ? 'text' : 'password'"
            density="compact"
            placeholder="Enter your password"
            prepend-inner-icon="mdi-lock-outline"
            variant="outlined"
            @click:append-inner="visible = !visible"
            label="Password"
            ></v-text-field>
            
          </v-card-text>
          <v-btn class = "btn" type ="submit" >
         비밀번호 변경
        </v-btn>
        <v-btn class = "btn" >
        취소
        </v-btn>

        <v-dialog
      v-model="dialog.isOpen.value"
      width="auto"
    >
      <v-card>
        <v-card-text>
          비밀번호변경이 완료되었습니다.
          재로그인이 필요합니다.
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" block @click="dialog.closeDialog">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</form>
</template>

<script setup>
import NavigationBar from '@/components/ProfileNavigationBar.vue';
import axios from 'axios'
import { ref } from 'vue'
import { useAuthStore } from '@/store/app';

const authStore = useAuthStore();
const password = ref();

const dialog = {
  isOpen: ref(false),
  openDialog() {
    dialog.isOpen.value = true; // 다이얼로그 열기
  },
  closeDialog() {
    dialog.isOpen.value = false; // 다이얼로그 닫기
    authStore.logout();
    location.href = "http://localhost:3000/home"
  }
};

const submit = ()=> {
  const requestData = {
      password: password.value // 비밀번호를 JSON 객체에 추가
    };

axios({
    method:"patch",
    url:"http://localhost:8080/changePW",
    data: requestData,
    headers: {
      Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 토큰 포함
    },
})
.then(response => {
dialog.openDialog();
console.log(response.data);

})
// POST 요청 실패 시 로직
.catch(error => {
console.error(error);
});
}

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