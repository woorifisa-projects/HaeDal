<template>
    <navigation-bar>  </navigation-bar>
    <form @submit.prevent="submit">
    <div style="margin-top:130px">
        <v-card class="success-card">
            <div class=" success">
                <img src='@/assets/img/success.jpg'>
            </div>
            <h2>회원탈퇴 하시려구요?</h2>
            <p style="margin-bottom: 18px;font-size: 13px;">해달과 함께 해와 달이 뜨고 지 듯 꾸준히 저축해 보아요!</p>
            <v-btn class="btn" type = "submit">탈퇴하기</v-btn>
            <v-btn href="/mypage" class="btn">취소하기</v-btn>
        </v-card>
        <div class=" overlay">
        </div>
    </div>


    <v-dialog
      v-model="dialog.isOpen.value"
      width="auto"
    >
      <v-card>
        <v-card-text>
          회원탈퇴완료되었습니다.
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" block @click="dialog.closeDialog">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</form>

</template>

<script setup>
import axios from 'axios'
import { ref } from 'vue'
import NavigationBar from '@/components/ProfileNavigationBar.vue';
import { useAuthStore } from '@/store/app';

const authStore = useAuthStore();

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

    axios({
        method:"delete",
        url:"http://localhost:8080/user/leave",
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
    width: 40%;
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