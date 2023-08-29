
<template>
    <h2>고객님 맞춤형 상품 추천</h2>
    <p>고객님께서 제공해주신 정보를 바탕으로 추천해드리는 TOP 3 상품들입니다.</p>
    <div class="chooseButton">
        <v-layout class="overflow-visible" style="height: 56px;">
            <v-bottom-navigation v-model="value">
                <v-btn @click="ageGroup">
                    # 연령대
                    <v-tooltip activator="parent" location="bottom">고객님의 연령대 맞춤 상품을 추천해드립니다!</v-tooltip>
                </v-btn>

                <v-btn @click="purpose">
                    # 이용 목적
                    <v-tooltip activator="parent" location="bottom">고객님의 상품 이용 목적 맞춤 상품을 추천해드립니다!</v-tooltip>
                </v-btn>

                <v-btn @click="asset">
                    # 자산별
                    <v-tooltip activator="parent" location="bottom">고객님의 자산 맞춤 상품을 추천해드립니다!</v-tooltip>
                </v-btn>
            </v-bottom-navigation>
        </v-layout>
    </div>

    <!--v-for 활용하여 api에서 받아온 정보들 반복해서 보여주면 됩니다!-->
    <div id="products" v-bind:class="item.productName" v-for="(item, index) in listData" :key="index">
        <p>TOP {{ index + 1 }} </p>
        <v-card class="mx-auto" max-width="70%">
            <v-card-item>
                <div>
                    <div class="text-h6 mb-1">
                        {{ item.productName }}
                    </div>
                    <div class="text-overline mb-1">
                        <span>
                            상품 기간 : {{ item.period }}
                            금리 : {{ item.interestRate }}
                            최소 가입 금액 : {{ item.requiredStartMoney }}
                        </span>
                    </div>
                    <div class="text-caption">{{ item.longInfo }}</div>
                </div>
            </v-card-item>
            <div class="d-flex justify-end align-center">
                <v-card-actions>
                    <v-btn variant="outlined" @click=subscribeProduct(item)>
                        가입 하기
                    </v-btn>
                </v-card-actions>
            </div>
        </v-card>
    </div>
</template>

<script>

</script>

<script setup>
import axios from 'axios'
import { watchEffect, ref } from 'vue'
import router from '../router'
import { mdiConsoleNetwork } from '@mdi/js';

// 서버에서 받아오는 정보
const listData = ref([]);


// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: 'http://15.164.189.153:8080', // 서버의 주소
    // withCredentials: "true" // CORS 요청에 관련된 설정을 포함
})

watchEffect(() => {
    axiosInstance.get('/recommendedProduct').then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData);
    })
})

//연령대 해당 상품 버튼
const ageGroup = () => {
    listData.value = [];
    axiosInstance.get('/recommendedProduct/filter/userAgeGroup').then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData);
    })
}

// 이용목적 해당 상품 버튼
const purpose = () => {
    listData.value = [];
    axiosInstance.get('/recommendedProduct/filter/servicePurpose').then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData);
    })
}
//자산별 해당 상품 버튼
const asset = () => {
    listData.value = [];
    axiosInstance.get('/recommendedProduct').then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData)
    })
}


//신청하기 버튼
const subscribeProduct = (item) => {
    const productId = item.productId;
    const productName = item.productName
    console.log(productName);
    console.log(item.deposit);
    if (item.deposit == true) {
        router.push(
            {
                name: 'subscribeD',
                params: {
                    id: productId,
                }
            })
    } else if (item.deposit == false) {
        router.push({
            name: 'subscribeI',
            params: {
                id: productId,
            }
        })
    }
}
</script>

<style lang="scss" scoped>
h2,
p {
    text-align: center;
}

#products {
    margin: 2rem 0rem 1rem 0rem;
}

.mx-auto {
    padding: 1rem;
    margin: 2rem;
}
</style>
