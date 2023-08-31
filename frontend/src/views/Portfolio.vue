<template>
    <navigation-bar>  </navigation-bar>
    <v-card class="mx-auto" max-width="400">
    <v-img class="align-end text-white" height="200" src="https://cdn.vuetifyjs.com/images/cards/docks.jpg" cover>
      <v-card-title>{{ username }}님 의 계좌</v-card-title>
    </v-img>

    <v-card-text>
      <div> 계좌번호 : {{ phonenumber }} </div>
    </v-card-text>

    <v-card-text>
      <div>현재 잔액 : {{ userAgeGroup }}</div>
    </v-card-text>

    <v-card-text>
      <div>이용 목적 : {{ servicePurpose }}</div>
    </v-card-text>

    <v-card-actions>
      <v-btn color="orange">
        Share
      </v-btn>

      <v-btn color="orange" href="http://localhost:3000/profile/edit">
        계좌정보 수정하기
      </v-btn>
    </v-card-actions>
  </v-card>
    <div>
        <v-layout class="overflow-visible" style="height: 56px; box-shadow: none;">
            <v-bottom-navigation v-model="value" color="teal" grow>
                <v-btn @click="viewAll">
                    전체 가입 상품
                </v-btn>

                <v-btn @click="financial">
                    찜해둔 상품
                </v-btn>

                <v-btn @click="tema">
                    테마 상품
                </v-btn>
            </v-bottom-navigation>
        </v-layout>
    </div>

    <div class="container">
        <v-card class="mx-auto" width="30rem" v-bind:class="item.productName" v-for="(item, index) in listData"
            :key="index">
            <v-card-item class="products">
                <div>
                    <div class="text-h6 mb-1">
                        {{ item.productName }}
                    </div>
                    <div class="text-overline mb-1">
                        {{ item.shortInfo }}
                    </div>
                    <div class="text-caption">{{ item.tag }}</div>
                </div>
            </v-card-item>
            <v-card-actions>
                <v-btn variant="outlined" @click=subscribeProduct(item)>
                    정보 보기
                </v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>

<script setup>
import axios from 'axios'
import { watchEffect, ref } from 'vue'
import router from '../router'
import NavigationBar from '@/components/ProfileNavigationBar.vue';
import { mdiConsoleNetwork } from '@mdi/js';
import { useAuthStore } from '@/store/app';

const authStore = useAuthStore();
const username = ref(0);

// 서버에서 받아오는 정보
const listData = ref([]);

// Axios 인스턴스 생성
axios({
        method:"get",
        url:"http://localhost:8080/subscribe/portfolio",
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 토큰 포함
        },
    }).then(res => {
         username.value = authStore.username;
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData);
      })
      // POST 요청 실패 시 로직
      .catch(error => {
        console.error(error);
      });

    
    const subscribeProduct = (item) => {
    const productId = item.productId;
    const productName = item.productName
    console.log(productName);
    console.log(item.deposit);
    if (item.deposit == true) {
        router.push(
            {
                name: 'subscribeDforShow',
                params: {
                    id: productId,
                }
            })
    } else if (item.deposit == false) {
        router.push({
            name: 'subscribeIforShow',
            params: {
                id: productId,
            }
        })
    }
}

</script>


<style lang="scss" scoped>
.overflow-visible {
    margin-bottom: 4rem;
}

.mx-auto {
    text-align: center;
    justify-content: center;
}

.mx-auto button {
    margin: auto;
}

.container {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    grid-template-rows: repeat(3, minmax(100px, auto));
    grid-gap: 20px;
    margin: 10px 20rem 10rem 20rem;
}

.searchProduct {
    width: 30%;
    height: 100%;
    background-color: rgb(238, 238, 238);
    border-radius: 3px;
    margin-right: 1rem;
    padding: 7px 2rem;
    outline: none;
}

.search {
    padding: 10px;
    text-align: center;
    margin-bottom: 2rem;
}
</style>