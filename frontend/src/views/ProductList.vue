<template>
    <div class="search">
        <input type="text" class="searchProduct" placeholder="원하시는 상품명을 검색해 주세요">
        <v-btn type="submit" class="searchButton">
            검색
        </v-btn>
    </div>
    <div>
        <v-layout class="overflow-visible" style="height: 56px; box-shadow: none;">
            <v-bottom-navigation v-model="value" color="teal" grow>
                <v-btn>
                    전체 상품
                </v-btn>

                <v-btn>
                    금융 상품
                </v-btn>

                <v-btn>
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

const listData = ref([]);

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
        console.log(listData)
    })
})

const subscribeProduct = (item) => {
    const productId = item.productId;
    const productName = item.productName

    if (item.isDeposit === 0) {
        router.push(
            {
                path: `/subscribe/${productId}/I`,
                params: {
                    productId: productId,
                    productName: productName
                }
            })
    } else {
        router.push({
            path: `/subscribe/${productId}/D`,
            params: { productName: productName }
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
    outline: none;
}

.search {
    padding: 0px;
    text-align: center;
    margin-bottom: 2rem;
}
</style>