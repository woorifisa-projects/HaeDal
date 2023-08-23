<template>
    <form @submit.prevent="searchForm">
        <div class="search">
            <input type="text" v-model="searchTerm" class="searchProduct" placeholder="원하시는 상품명을 검색해 주세요">
            <v-btn class="searchButton">
                검색
            </v-btn>
        </div>
    </form>
    <div>
        <v-layout class="overflow-visible" style="height: 56px; box-shadow: none;">
            <v-bottom-navigation v-model="value" color="teal" grow>
                <v-btn @click="viewAll">
                    전체 상품
                </v-btn>

                <v-btn @click="financial">
                    금융 상품
                </v-btn>

                <v-btn @click="tema">
                    테마 상품
                </v-btn>
            </v-bottom-navigation>
        </v-layout>
    </div>

    <div class="container">
        <v-card class="mx-auto" width="15rem" variant="outlined" v-bind:class="item.productName"
            v-for="(item, index) in listData" :key="index">
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
                    신청하기
                </v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>

<script setup>
import axios from 'axios'
import { watchEffect, ref } from 'vue'
import router from '../router'
import { mdiConsoleNetwork } from '@mdi/js';

// 서버에서 받아오는 정보
const listData = ref([]);

//사용자가 검색한 단어
const searchTerm = ref('');

// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080', // 서버의 주소
    withCredentials: "true" // CORS 요청에 관련된 설정을 포함
})

watchEffect(() => {
    axiosInstance.get('/products').then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData);
    })
})

//검색 기능
const searchForm = () => {
    //listData 초기화
    listData.value = [];

    const searchKeyword = searchTerm.value;
    axiosInstance.post(`/products/${searchKeyword}`).then((res) => {
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

//전체 조회 기능
const viewAll = () => {
    listData.value = [];
    axiosInstance.get('/products').then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData)
    })
}
// 금융 상품 조회 기능
const financial = () => {
    listData.value = [];
    axiosInstance.get(`products/filter/FINANCE`).then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData)
    })
}
// 테마 상품 조회 기능
const tema = () => {
    listData.value = [];
    axiosInstance.get(`products/filter/THEMA`).then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })
        console.log(listData)
    })
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