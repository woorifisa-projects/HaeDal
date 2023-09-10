<template>
  <form @submit.prevent="submit">
    <div class="edit_style">
      <h1 style="margin-bottom: 40px; text-align: center;">
        개인정보 수정
      </h1>
      <v-text-field class="name" v-model="name.value.value" :counter="10" :error-messages="name.errorMessage.value"
        label="Name">
        <template v-slot:prepend>
          <div>이름</div>
        </template>
      </v-text-field>

      <v-text-field v-model="phoneNumber.value.value" :counter="7" :error-messages="phoneNumber.errorMessage.value"
        label="Phone Number">
        <template v-slot:prepend>
          <div>전화번호</div>
        </template>
      </v-text-field>

      <v-select v-model="userAgeGroup.value.value" :items="ageitems" :error-messages="userAgeGroup.errorMessage.value"
        label="Age">
        <template v-slot:prepend>
          <div>연령대</div>
        </template>
      </v-select>

      <v-select v-model="servicePurpose.value.value" :items="purposeitems"
        :error-messages="servicePurpose.errorMessage.value" label="purpose">
        <template v-slot:prepend>
          <div>이용목적</div>
        </template>
      </v-select>

      <v-text-field v-model="accountNumber.value.value" :counter="7" :error-messages="accountNumber.errorMessage.value"
        label="Account Number">
        <template v-slot:prepend>
          <div>계좌번호</div>
        </template>
      </v-text-field>

      <v-text-field v-model="asset.value.value" :counter="13" :error-messages="asset.errorMessage.value" label="Asset">
        <template v-slot:prepend>
          <div>자산</div>
        </template>
      </v-text-field>

      <v-text-field v-model="authNumber.value.value" :counter="4" :error-messages="authNumber.errorMessage.value"
        label="Account Number">
        <template v-slot:prepend>
          <div>계좌 인증번호</div>
        </template>
      </v-text-field>

      <v-btn class="button-style" type="submit">
        수정
      </v-btn>

      <v-btn class="button-style" href="/mypage">
        취소
      </v-btn>
    </div>

    <div class="text-center">

      <v-dialog v-model="dialog.isOpen.value" width="auto">
        <v-card>
          <v-card-text>
            {{ username }}회원님의 정보가 수정되었습니다!
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" block @click="dialog.closeDialog">확인</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>


  </form>
</template>

<script setup>
import { onMounted } from 'vue';
import { ref } from 'vue'
import axios from 'axios'
import { useField, useForm } from 'vee-validate'
import { useAuthStore } from '@/store/app';

const authStore = useAuthStore();
const username = ref(0);
let useragegroupchange = '';
let servicepurposechange = '';


const dialog = {
  isOpen: ref(false),
  openDialog() {
    dialog.isOpen.value = true; // 다이얼로그 열기
  },
  closeDialog() {
    dialog.isOpen.value = false; // 다이얼로그 닫기
    location.href = "https://haedal.store/mypage"
  }
};

const { handleSubmit } = useForm({
  validationSchema: {
    name(value) {
      if (value?.length >= 2) return true
      return 'Name needs to be at least 2 characters.'
    },
    phoneNumber(value) {
      if (value?.length > 9 && /[0-9-]+/.test(value)) return true
      return 'Phone number needs to be at least 9 digits.'
    },
    userAgeGroup(value) {
      if (value) return true
      return 'userAgeGroup an item.'
    },
    servicePurpose(value) {
      if (value) return true
      return 'servicePurpose an item.'
    },
  },
})

const name = useField('name')
const phoneNumber = useField('phoneNumber')
// const email = useField('email')
const userAgeGroup = useField('userAgeGroup')
const servicePurpose = useField('servicePurpose')
const checkbox = useField('checkbox')

const ageitems = ref([
  '10대',
  '20대',
  '30대',
  '40대',
  '50대',
])
const purposeitems = ref([
  '자동차 구매',
  '주택 구매',
  '목돈 마련',
  '기타',
])

const ageEnumMapping = {
  '10대': 'ONE',
  '20대': 'TWO',
  '30대': 'THREE',
  '40대': 'FOUR',
  '50대': 'FIVE',
};

const purposeEnumMapping = {
  '자동차 구매': 'FORCAR',
  '주택 구매': 'FORHOUSE',
  '목돈 마련': 'MOKDON',
  '기타': 'OTHERS',
}

const mapToAgeEnum = (value) => ageEnumMapping[value];
const mapToPurposeEnum = (value) => purposeEnumMapping[value];

const accountNumber = useField('accountNumber')
const asset = useField('asset')
const authNumber = useField('authNumber')
// console.log("보낼헤더: " + authStore.accessToken);

const submit = handleSubmit(values => {

  const ageEnum = mapToAgeEnum(values.userAgeGroup);
  const purposeEnum = mapToPurposeEnum(values.servicePurpose);

  const dataToSend = { ...values, userAgeGroup: ageEnum, servicePurpose: purposeEnum };

  console.log(dataToSend);
  console.log(authStore.accessToken + "여기 토큰값이다");
  axios.patch("https://backend.haedal.store/profile/edit/save", dataToSend, {
    headers: {
      Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
    }
  })
    .then(response => {
      // POST 요청 성공 시 로직
      console.log(response.data);
      username.value = values.username;
      dialog.openDialog();
      console.log("모달창띄웟다");

    })
    // POST 요청 실패 시 로직
    .catch(error => {
      console.error(error);
    });
}
)


onMounted(() => {
  console.log("새로고췸");
  console.log("헤더전역관리토큰입니다" + authStore.accessToken);
  // Local Storage에서 토큰을 가져와서 store에 저장
  const storedToken = localStorage.getItem('accessToken');
  if (storedToken) {
    authStore.loginSuccess(storedToken);
    console.log(localStorage.getItem('accessToken'));
    // 페이지 로딩 시 사용자 정보 요청 로직 추가
  }
  if (storedToken) {
    axios.get("https://backend.haedal.store/profile/edit", {
      headers: {
        Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
      },
    })
      .then(response => {
        switch (response.data.userAgeGroup) {
          case 'ONE':
            useragegroupchange = '10대'
            break;
          case 'TWO':
            useragegroupchange = '20대'
            break;
          case 'THREE':
            useragegroupchange = '30대'
            break;
          case 'FOUR':
            useragegroupchange = '40대'
            break;
          case 'FIVE':
            useragegroupchange = '50대'
            break;

        }
        switch (response.data.servicePurpose) {
          case 'MOKDON':
            servicepurposechange = '목돈 마련'
            break;
          case 'FORCAR':
            servicepurposechange = '자동차 구매'
            break;
          case 'FORHOUSE':
            servicepurposechange = '주택 구매'
            break;
          case 'OTHERS':
            servicepurposechange = '기타'
            break;
        }

        console.log(response.data);
        name.value.value = response.data.name
        phoneNumber.value.value = response.data.phoneNumber
        userAgeGroup.value.value = useragegroupchange
        servicePurpose.value.value = servicepurposechange
        accountNumber.value.value = response.data.accountNumber
        asset.value.value = response.data.asset
        authNumber.value.value = response.data.authNumber

      })
  }

});


</script>

<style lang="scss" scoped>
.title {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.center-text-field {
  display: flex;
  flex-direction: column;
}

.edit_style {
  width: 40%;
  margin: auto;
  margin-top: 5rem;
  margin-bottom: 10rem;
  text-align: center;
}

.button-style {
  width: 8rem;
  border-radius: 10px;
  box-shadow: none;
  background: rgba(0, 179, 255, 0.826);
  color: white;
  margin: 30px 14px;
  font-weight: bolder;
  font-size: 18px;
}
</style>
