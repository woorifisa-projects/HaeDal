<template>
  <form @submit.prevent="submit">
    <v-container class="register">
      <v-card min-width="450" max-width="900" title="사용자 정보 입력">
        <v-container>
          <v-text-field class="center-text-field" v-model="id.value.value" :counter="10" variant="outlined"
            prepend-inner-icon="mdi-account-outline" :error-messages="id.errorMessage.value" label="아이디">
            <template v-slot:append-inner>
              <v-btn @click="checkId">
                <div v-if="checkIdResult==2">확인</div>
                <div v-else>
                중복체크
              </div>
              </v-btn>
            </template>
          </v-text-field>

          <v-text-field v-model="password.value.value" :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          :type="visible ? 'text' : 'password'" :counter="15" :error-messages="password.errorMessage.value"
            prepend-inner-icon="mdi-lock-outline" variant="outlined" @click:append-inner="visible = !visible" label="비밀번호">
          </v-text-field>

          <v-text-field v-model="confirmPassword.value.value" 
          :type="visible ? 'text' : 'password'" :counter="15"
            :error-messages="confirmPassword.errorMessage.value" prepend-inner-icon="mdi-lock-outline" variant="outlined"
             label="비밀번호확인">
          </v-text-field>

          <v-text-field v-model="name.value.value" :counter="10" :error-messages="name.errorMessage.value"
            prepend-inner-icon="mdi-account-outline" variant="outlined" label="이름">

          </v-text-field>

          <v-text-field v-model="phoneNumber.value.value" :counter="7" :error-messages="phoneNumber.errorMessage.value"
            prepend-inner-icon="mdi-phone-outline" variant="outlined" label="전화번호">

          </v-text-field>

        </v-container>
      </v-card>


      <v-card class="mx" min-width="450" max-width="900" title="추가 정보 입력">
        <v-container>


          <v-select v-model="userAgeGroup.value.value" :items="ageitems" :error-messages="userAgeGroup.errorMessage.value"
            label="연령대" variant="outlined">
          </v-select>

          <v-select v-model="servicePurpose.value.value" :items="purposeitems"
            :error-messages="servicePurpose.errorMessage.value" label="이용목적" variant="outlined">
          </v-select>

          <v-text-field v-model="accountNumber.value.value" :counter="7"
            :error-messages="accountNumber.errorMessage.value" prepend-inner-icon="mdi-credit-card-outline" label="계좌번호" variant="outlined">
          </v-text-field>

          <v-text-field v-model="asset.value.value" :counter="13" :error-messages="asset.errorMessage.value" label="자산"
          prepend-inner-icon="mdi-currency-usd" variant="outlined">
          </v-text-field>

          <v-text-field v-model="authNumber.value.value" :counter="4" :error-messages="authNumber.errorMessage.value"
            label="계좌 인증번호" prepend-inner-icon="mdi-credit-card-lock-outline" variant="outlined">
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

      </v-card>
    </v-container>

    <div class="text-center">
      
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

    <v-dialog v-model="dialogalreadyId.isOpen.value" width="auto">
      <v-card>
        <v-card-text>
          이미 사용중인 아이디입니다.
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" block @click="dialogalreadyId.closeDialog">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </form>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useField, useForm } from 'vee-validate'

let checkIdResult = 0;
const visible = ref(false);

const dialog = {
  isOpen: ref(false),
  openDialog() {
    dialog.isOpen.value = true; // 다이얼로그 열기
  },
  closeDialog() {
    dialog.isOpen.value = false; // 다이얼로그 닫기
    location.href = "https://haedal.store/login"
  }
};


const dialogId = { // 중복되지않은 아이디 - 성공일때
  isOpen: ref(false),
  openDialog() {
    dialogId.isOpen.value = true; // 다이얼로그 열기
  },
  closeDialog() {
    checkIdResult = 2;
    dialogId.isOpen.value = false; // 다이얼로그 닫기
  }
};

const dialogalreadyId = { // 중복된 아이디일때 - 실패일때
  isOpen: ref(false),
  openDialog() {
    dialogalreadyId.isOpen.value = true; // 다이얼로그 열기
  },
  closeDialog() {
    checkIdResult = 1;
    dialogalreadyId.isOpen.value = false; // 다이얼로그 닫기
  }
};

const { handleSubmit, handleReset } = useForm({
  validationSchema: {
    id(value) {
      if (value?.length >= 2) return true
      return '아이디는 최소 2글자 이상이어야합니다.'
    },
    password(value) {
      if (value?.length >= 2) return true
      return '비밀번호는 최소 2글자 이상이어야합니다.'
    },
    confirmPassword(value) {
      if (value === password.value.value) return true;
      return '비밀번호가 일치하지않습니다.';
    },
    name(value) {
      if (value?.length >= 1) return true
      return '필수입력항목입니다.'
    },
    phoneNumber(value) {
      if (value?.length > 9 && /[0-9-]+/.test(value)) return true
      return '전화번호는 최소 9자리 이상이어야합니다.'
    },
    userAgeGroup(value) {
      if (value) return true
      return '필수입력항목입니다.'
    },
    servicePurpose(value) {
      if (value) return true
      return '필수입력항목입니다.'
    },
    accountNumber(value) {
      if (value) return true
      return '필수입력항목입니다.'
    },
    asset(value) {
      if (value) return true
      return '필수입력항목입니다.'
    },
    authNumber(value) {
      if (value) return true
      return '필수입력항목입니다.'
    },

    checkbox(value) {
      if (value === '1') return true
      return '항목에 동의해주세요.'
    },
  },
})
const id = useField('id')
const password = useField('password')
const confirmPassword = useField('confirmPassword')
const name = useField('name')
const phoneNumber = useField('phoneNumber')
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
  axios.get(`https://backend.haedal.store/user/idcheck?id=${idcheck}`)
    .then(response => {
      console.log(response.data);
      dialogId.openDialog();
    })

    .catch(error => {
      console.log("아이디이미있단");
      //  console.error(error);
      const serverErrorMessage = '아이디가 이미 존재합니다.'; // 서버 오류 메시지 설정
      dialogalreadyId.openDialog();

    });
}

const submit = handleSubmit(values => {
  if(checkIdResult == 0){
    alert("아이디중복체크를 진행해주세요");
  }else if(checkIdResult == 1){
    alert("이미 사용중인 아이디입니다")
  }else{

  const ageEnum = mapToAgeEnum(values.userAgeGroup);
  const purposeEnum = mapToPurposeEnum(values.servicePurpose);

  const dataToSend = { ...values, userAgeGroup: ageEnum, servicePurpose: purposeEnum };

  console.log(dataToSend);
  axios.post("https://backend.haedal.store/user/register", dataToSend)
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
}
)


</script>

<style lang="scss" scoped>
.register {
  display: flex;
  flex-direction: row;
  margin-bottom: 100px;
  margin-top: 20px;
  text-align: center;
  justify-content: center;

}

.v-card-title {
  font-family: 'Noto Sans KR', sans-serif !important;
}


.register .v-card {
  outline: solid 2px rgba(0, 139, 219, 0.469);
  padding: 20px;
  margin: 1px;
  box-shadow: none;
}

.register .v-btn {
  width: 6rem;
  border-radius: 10px;
  box-shadow: none;
  background: rgba(0, 179, 255, 0.826);
  color: white;
  font-weight: bolder;
  font-size: 16px;

  &:hover {
    box-shadow: none;
  }
}
</style>
