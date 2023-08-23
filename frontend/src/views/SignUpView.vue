<template>
  <form @submit.prevent="submit">
    <v-col cols="12" sm="8" md="6" lg="4">
    <v-text-field
      v-model="id.value.value"
      :counter="10"
      :error-messages="id.errorMessage.value"
      label="Id"
    >
    <template v-slot:prepend> 
      <div>아이디</div>
    </template>
    </v-text-field>
  

    <v-text-field
      v-model="password.value.value"
      :counter="10"
      :error-messages="password.errorMessage.value"
      label="Password"
    >
    <template v-slot:prepend> 
      <div>비밀번호</div>
    </template>
  </v-text-field>


     <v-text-field
      v-model="name.value.value"
      :counter="10"
      :error-messages="name.errorMessage.value"
      label="Name"
    >
    <template v-slot:prepend> 
      <div>이름</div>
         </template>
  </v-text-field>

    <v-text-field
      v-model="phone.value.value"
      :counter="7"
      :error-messages="phone.errorMessage.value"
      label="Phone Number"
    >
    <template v-slot:prepend> 
      <div>전화번호</div>
         </template>
    </v-text-field>

    <!-- <v-text-field
      v-model="email.value.value"
      :error-messages="email.errorMessage.value"
      label="E-mail"
    ></v-text-field> -->

    <v-select
      v-model="selectAge.value.value"
      :items="ageitems"
      :error-messages="selectAge.errorMessage.value"
      label="Age"
    >
    <template v-slot:prepend> 
      <div>연령대</div>
         </template>
    </v-select>

    <v-select
      v-model="selectPurpose.value.value"
      :items="purposeitems"
      :error-messages="selectPurpose.errorMessage.value"
      label="purpose"
    >
    <template v-slot:prepend> 
      <div>이용목적</div>
         </template>
    </v-select>

    <v-text-field
      v-model="accountNumber.value.value"
      :counter="7"
      :error-messages="accountNumber.errorMessage.value"
      label="Account Number"
    >
    <template v-slot:prepend> 
      <div>계좌번호</div>
         </template>
    </v-text-field>

    <v-text-field
      v-model="asset.value.value"
      :counter="13"
      :error-messages="asset.errorMessage.value"
      label="Asset"
    >
    <template v-slot:prepend> 
      <div>자산</div>
         </template>
    </v-text-field>

    <v-text-field
      v-model="accountPassword.value.value"
      :counter="4"
      :error-messages="accountPassword.errorMessage.value"
      label="Account Number"
    >
    <template v-slot:prepend> 
      <div>계좌 인증번호</div>
         </template>
    </v-text-field>

    <v-checkbox
      v-model="checkbox.value.value"
      :error-messages="checkbox.errorMessage.value"
      value="1"
      label="개인정보 처리에 동의합니다."
      type="checkbox"
    ></v-checkbox>

    <v-btn
      class="me-4"
      type="submit"
    >
      회원가입
    </v-btn>

    <v-btn @click="handleReset">
      취소
    </v-btn>
  </v-col>
  </form>
</template>

<script setup>
  import { ref } from 'vue'
  import axios from 'axios'
  import { useField, useForm } from 'vee-validate'
  const { handleSubmit, handleReset } = useForm({
    validationSchema: {
      id (value) {
        if (value?.length >= 2) return true
        return 'Id needs to be at least 2 characters.'
      },
      password (value) {
        if (value?.length >= 2) return true
        return 'password needs to be at least 2 characters.'
      },
      name (value) {
        if (value?.length >= 2) return true
        return 'Name needs to be at least 2 characters.'
      },
      phone (value) {
        if (value?.length > 9 && /[0-9-]+/.test(value)) return true
        return 'Phone number needs to be at least 9 digits.'
      },
      // email (value) {
      //   if (/^[a-z.-]+@[a-z.-]+\.[a-z]+$/i.test(value)) return true
      //   return 'Must be a valid e-mail.'
      // },
      selectAge (value) {
        if (value) return true
        return 'SelectAge an item.'
      },
      selectPurpose (value) {
        if (value) return true
        return 'SelectPurpose an item.'
      },
      checkbox (value) {
        if (value === '1') return true
        return 'Must be checked.'
      },
    },
  })
  const id = useField('id')
  const password = useField('password')
  const name = useField('name')
  const phone = useField('phone')
  // const email = useField('email')
  const selectAge = useField('selectAge')
  const selectPurpose = useField('selectPurpose')
  const checkbox = useField('checkbox')
  const ageitems = ref([
    'ONE',
    '20대',
    '30대',
    '40대',
    '50대',
  ])
  const purposeitems = ref([
    'FORCAR',
    'FORHOUSE',
    'MOKDON',
    'Guitar',
  ])
  const accountNumber = useField('accountNumber')
  const asset = useField('asset')
  const accountPassword = useField('accoutPassword')

  const changeItems = () =>{
    if(values.value.items === '10대'){
    }
    
  }
  const submit = handleSubmit(values => {
    
    console.log(values);
    axios.post("http://localhost:8080/user/register",values)
    .then(response => {
      // POST 요청 성공 시 로직
      console.log(response.data);
    })
    // POST 요청 실패 시 로직
    .catch(error => {
      console.error(error);
    });
  }
)
  
</script>