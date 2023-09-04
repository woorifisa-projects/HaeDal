<template>
  <form @submit.prevent="submit">
    <v-container class="register">
      <v-card class="mx" min-width="450" max-width="900" title="User Registration">
        <v-container>
          <v-text-field class="center-text-field" v-model="id.value.value" :counter="10" variant="outlined"
            prepend-inner-icon="mdi-account-outline" :error-messages="id.errorMessage.value" label="아이디">
            <template v-slot:append>
              <v-btn @click="checkId">
                중복체크
              </v-btn>
            </template>
          </v-text-field>

          <v-text-field v-model="password.value.value" :counter="10" :error-messages="password.errorMessage.value"
            prepend-inner-icon="mdi-lock-outline" variant="outlined" label="비밀번호">
          </v-text-field>


          <v-text-field v-model="name.value.value" :counter="10" :error-messages="name.errorMessage.value"
            prepend-inner-icon="mdi-account-outline" variant="outlined" label="이름">

          </v-text-field>

          <v-text-field v-model="phoneNumber.value.value" :counter="7" :error-messages="phoneNumber.errorMessage.value"
            prepend-inner-icon="mdi-phone-outline" variant="outlined" label="전화번호">

          </v-text-field>

        </v-container>
      </v-card>


      <v-card class="mx" min-width="450" max-width="900" title="account Registration">
        <v-container>


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

          <v-text-field v-model="accountNumber.value.value" :counter="7"
            :error-messages="accountNumber.errorMessage.value" label="Account Number">
            <template v-slot:prepend>
              <div>계좌번호</div>
            </template>
          </v-text-field>

          <v-text-field v-model="asset.value.value" :counter="13" :error-messages="asset.errorMessage.value"
            label="Asset">
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

          <v-checkbox v-model="checkbox.value.value" :error-messages="checkbox.errorMessage.value" value="1"
            label="개인정보 처리에 동의합니다." type="checkbox"></v-checkbox>

          <v-btn class="me-4" type="submit">
            회원가입
          </v-btn>

          <v-btn @click="handleReset">
            취소
          </v-btn>
        </v-container>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn color="success">
            Complete Registration

            <v-icon icon="mdi-chevron-right" end></v-icon>
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-container>

    <div class="text-center">
      <!-- <v-btn
      color="primary"
      @click="dialog.openDialog"
    >
      Open Dialog
    </v-btn> -->

      <v-dialog v-model="dialog.isOpen.value" width="auto">
        <v-card>
          <v-card-text>
            해달의 회원이 되신것을 환영합니다!
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" block @click="dialog.closeDialog">확인</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>

    <v-dialog v-model="dialogId.isOpen.value" width="auto">
      <v-card>
        <v-card-text>
          사용가능한 아이디입니다.
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" block @click="dialogId.closeDialog">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </form>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { defineRule } from 'vee-validate';
import { useField, useForm } from 'vee-validate'

const dialog = {
  isOpen: ref(false),
  openDialog() {
    dialog.isOpen.value = true; // 다이얼로그 열기
  },
  closeDialog() {
    dialog.isOpen.value = false; // 다이얼로그 닫기
    location.href = "http://localhost:3000/login"
  }
};


const dialogId = {
  isOpen: ref(false),
  openDialog() {
    dialogId.isOpen.value = true; // 다이얼로그 열기
  },
  closeDialog() {
    dialogId.isOpen.value = false; // 다이얼로그 닫기
  }
};


defineRule('customIdRule', async () => {
  if (id(value)?.length >= 2) {
    console.log(id.value.value);
    // 서버에 아이디 중복 체크 요청 보내기
    const response = await axios.get(`http://localhost:8080/user/idcheck?id=${value}`);

    if (response.data.exists) {
      return 'Id already exists'; // 아이디가 이미 존재하는 경우 오류 메시지 반환
    } else {
      return true; // 아이디가 존재하지 않는 경우 유효성 통과
    }
  }

  return 'Id needs to be at least 2 characters.';
});


const { handleSubmit, handleReset } = useForm({
  validationSchema: {
    id: [{
      customIdRule: true
    }],
    password(value) {
      if (value?.length >= 2) return true
      return 'password needs to be at least 2 characters.'
    },
    name(value) {
      if (value?.length >= 2) return true
      return 'Name needs to be at least 2 characters.'
    },
    phoneNumber(value) {
      if (value?.length > 9 && /[0-9-]+/.test(value)) return true
      return 'Phone number needs to be at least 9 digits.'
    },
    // email (value) {
    //   if (/^[a-z.-]+@[a-z.-]+\.[a-z]+$/i.test(value)) return true
    //   return 'Must be a valid e-mail.'
    // },
    userAgeGroup(value) {
      if (value) return true
      return 'userAgeGroup an item.'
    },
    servicePurpose(value) {
      if (value) return true
      return 'servicePurpose an item.'
    },
    checkbox(value) {
      if (value === '1') return true
      return 'Must be checked.'
    },
  },
})
const id = useField('id')
const password = useField('password')
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

const checkId = () => {
  const idcheck = id.value.value;
  console.log(idcheck);
  axios.get(`http://localhost:8080/user/idcheck?id=${idcheck}`)
    .then(response => {
      console.log(response.data);
      dialogId.openDialog();
    })

    .catch(error => {
      console.log("아이디이미있단");
      //  console.error(error);
      const serverErrorMessage = '아이디가 이미 존재합니다.'; // 서버 오류 메시지 설정
      id.setErrors({ id: serverErrorMessage });

    });
}

const submit = handleSubmit(values => {

  const ageEnum = mapToAgeEnum(values.userAgeGroup);
  const purposeEnum = mapToPurposeEnum(values.servicePurpose);

  const dataToSend = { ...values, userAgeGroup: ageEnum, servicePurpose: purposeEnum };

  console.log(dataToSend);
  axios.post("http://localhost:8080/user/register", dataToSend)
    .then(response => {
      // POST 요청 성공 시 로직
      console.log(response.data);
      dialog.openDialog();
      console.log("모달창띄웟다");

    })
    // POST 요청 실패 시 로직
    .catch(error => {
      console.error(error);
    });
}
)


</script>

<style lang="scss" scoped>
.register {
  display: flex;
  flex-direction: row;
}
</style>
