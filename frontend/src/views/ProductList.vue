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
        <v-card class="mx-auto" width="20rem" v-bind:class="item.productName" v-for="(item, index) in listData"
            :key="index">
            <v-card-item class="products">
                <div style="display: flex;flex-direction: column;align-items: center; ">
                    <div style="font-size: 20px; font-weight: bold; margin: 1rem 0rem 1.3rem 0rem;">
                        {{ item.productName }}
                    </div>
                    <div style="font-size: 12px; margin-bottom: 1rem; text-align: left;">
                        {{ item.shortInfo }}
                    </div>
                    <div class="text-caption"
                        style="background-color: rgba(0, 162, 255, 0.225); width:4rem; padding:2px; border-radius: 15px; color:rgb(0, 111, 186); font-size:6px; margin-top: 0.3rem;">
                        {{ item.tag }}
                    </div>
                </div>
            </v-card-item>
            <v-card-actions>
                <v-btn
                    style="background: rgba(0, 179, 255, 0.826); color:white; font-weight: bold; border-radius: 0.6rem; margin-bottom: 1rem;"
                    @click=subscribeProduct(item)>
                    가입 하기
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
    // baseURL: 'http://15.164.189.153:8080',
    // withCredentials: true // CORS 요청에 관련된 설정을 포함
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

//가입하기 버튼
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
    grid-gap: 40px;
    margin: -30px 20rem 10rem 20rem;
    padding: 1rem;
    justify-content: center;
}

.searchProduct {
    width: 40%;
    height: 100%;
    border: 1px solid rgba(128, 128, 128, 0.095);
    box-shadow: -2px 4px 10px 0px rgba(0, 0, 0, 0.033) inset;
    background-color: rgba(236, 236, 236, 0.337);
    border-radius: 10px;
    margin-top: 2rem;
    margin-right: 0.6rem;
    padding: 6px 1rem;
    outline: none;
}


.searchButton {
    width: 5rem;
    border-radius: 16px;
    box-shadow: none;
    background: rgba(0, 179, 255, 0.826);
    color: white;
    font-weight: bolder;
    font-size: 16px;
}


.search {
    padding: 10px;
    text-align: center;
    margin-bottom: 2rem;
}

.v-bottom-navigation {
    background: rgba(0, 179, 255, 0.362);
    color: black;
    box-shadow: none;
}
</style>