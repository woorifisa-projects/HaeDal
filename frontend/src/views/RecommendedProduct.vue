
<template>
    <div class="top">
        <h2 style="  font-size: 35px;">고객님 맞춤형 상품 추천</h2>
        <p style="font-size: 13px; margin-bottom: 30px;">고객님께서 제공해주신 정보를 바탕으로 추천해드리는 TOP 3 상품들입니다.</p>
        <div class="chooseButton">
            <v-layout class="overflow-visible" style="height: 56px; margin-bottom: 40px;">
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
    </div>
    <div style="margin-bottom: 200px;">
        <div id="products" v-bind:class="item.productName" v-for="(item, index) in listData" :key="index">

            <p
                style="background-color: rgba(0, 179, 255, 0.362); width: 80px; font-weight: bolder; border-radius: 10px; color:rgb(0, 75, 121);; text-align: center;">
                TOP {{ index + 1 }} </p>

            <v-card class="mx-auto" max-width="70%" min-width="300px">
                <v-card-item style="padding: 20px;">
                    <div>
                        <div class="text-h5 mb-3" style="font-weight: bolder;">
                            {{ item.productName }}
                        </div>
                        <div class="text-overline mb-3">
                            <span>
                                <b>상품 기간 :</b> {{ item.period }}개월,
                                <b>금리 :</b> {{ item.interestRate }}%,
                                <b>최소 가입 금액 :</b> {{ item.requiredStartMoney }}원
                            </span>
                        </div>
                        <div class="text-caption">{{ item.longInfo }}</div>
                    </div>
                </v-card-item>
                <div class="d-flex justify-end align-center">
                    <span class="favorite" @click="dibs(item)" style="cursor:pointer;margin-right: 10px;">
                        <img v-if="item.isDibs === true" src='@/assets/img/favorite.png'>
                        <img v-else src='@/assets/img/favorite_border.png'>
                    </span>
                    <v-card-actions style="margin-left: 1rem;">
                        <v-btn class=" button-style" @click=subscribeProduct(item)>
                            정보 보기
                        </v-btn>
                    </v-card-actions>
                </div>
            </v-card>
        </div>
    </div>
</template>

<script>

</script>

<script setup>
import axios from 'axios'
import { watchEffect, ref } from 'vue'
import router from '../router'
import { mdiConsoleNetwork } from '@mdi/js';
import { useAuthStore } from '@/store/app';

// 서버에서 받아오는 정보
const listData = ref([]);
const authStore = useAuthStore();

// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
    // baseURL: 'http://15.164.189.153:8080', // 서버의 주소

})

watchEffect(() => {
    if (localStorage.getItem("accessToken")) {
        axiosInstance.get('/recommendedProduct', {
            headers: {
                Authorization: `Bearer ${authStore.accessToken}`
            }
        }).then((res) => {
            let tempArr = [...res.data]
            tempArr.forEach((item) => {
                console.log(item)
                listData.value.push(item)
            })

            // 모든 데이터를 받아온 후에 찜 여부를 확인
            listData.value.forEach((item) => {
                axiosInstance.get(`/dibs/${item.productId}/check`, {
                    headers: {
                        Authorization: `Bearer ${authStore.accessToken}`
                    }
                }).then((res) => {
                    item.isDibs = res.data; // 상품 객체에 찜 여부 추가
                    console.log(item.isDibs);
                }).catch((error) => {
                    // 로그인 되어 있지 않을 시 무조건 false
                    item.isDibs = false;
                });
            });
            console.log(listData);
        })
    }
    else {
        router.push('/error');
    }
})


// 찜하기 버튼 누를 시
const dibs = (item) => {
    if (!item.isDibs) {
        console.log("찜!");
        axios({
            method: "post",
            url: `http://localhost:8080/dibs/${item.productId}/add`,
            headers: {
                Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            },
        })
            .then(() => {
                item.isDibs = true; // Vue 3에서는 ref를 사용하므로 .value 없이 값 변경
            })
            .catch((error) => alert("로그인 후 이용 가능한 서비스 입니다"));
    } else {
        console.log("찜 취소");
        axios({
            method: "delete",
            url: `http://localhost:8080/dibs/${item.productId}/delete`,
            headers: {
                Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            },
        })
            .then(() => {
                item.isDibs = false; // Vue 3에서는 ref를 사용하므로 .value 없이 값 변경
            })
            .catch((error) => alert("로그인 후 이용 가능한 서비스 입니다"));
    }
}


//연령대 해당 상품 버튼
const ageGroup = () => {
    listData.value = [];
    axiosInstance.get('/recommendedProduct/filter/userAgeGroup', {
        headers: {
            Authorization: `Bearer ${authStore.accessToken}`
        }
    }).then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })

        listData.value.forEach((item) => {
            axiosInstance.get(`/dibs/${item.productId}/check`, {
                headers: {
                    Authorization: `Bearer ${authStore.accessToken}`
                }
            }).then((res) => {
                item.isDibs = res.data; // 상품 객체에 찜 여부 추가
                console.log(item.isDibs);
            }).catch((error) => {
                // 로그인 되어 있지 않을 시 무조건 false
                item.isDibs = false;
            });
        });
        console.log(listData);
    })
}

// 이용목적 해당 상품 버튼
const purpose = () => {
    listData.value = [];
    axiosInstance.get('/recommendedProduct/filter/servicePurpose', {
        headers: {
            Authorization: `Bearer ${authStore.accessToken}`
        }
    }).then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })

        listData.value.forEach((item) => {
            axiosInstance.get(`/dibs/${item.productId}/check`, {
                headers: {
                    Authorization: `Bearer ${authStore.accessToken}`
                }
            }).then((res) => {
                item.isDibs = res.data; // 상품 객체에 찜 여부 추가
                console.log(item.isDibs);
            }).catch((error) => {
                // 로그인 되어 있지 않을 시 무조건 false
                item.isDibs = false;
            });
        });
        console.log(listData);
    })
}

//자산별 해당 상품 버튼
const asset = () => {
    listData.value = [];
    axiosInstance.get('/recommendedProduct', {
        headers: {
            Authorization: `Bearer ${authStore.accessToken}`
        }
    }).then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            listData.value.push(item)
        })

        listData.value.forEach((item) => {
            axiosInstance.get(`/dibs/${item.productId}/check`, {
                headers: {
                    Authorization: `Bearer ${authStore.accessToken}`
                }
            }).then((res) => {
                item.isDibs = res.data; // 상품 객체에 찜 여부 추가
                console.log(item.isDibs);
            }).catch((error) => {
                // 로그인 되어 있지 않을 시 무조건 false
                item.isDibs = false;
            });
        });
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
                },
                headers: {
                    Authorization: `Bearer ${authStore.accessToken}`
                }
            })
    } else if (item.deposit == false) {
        router.push({
            name: 'subscribeI',
            params: {
                id: productId,
            },
            headers: {
                Authorization: `Bearer ${authStore.accessToken}`
            }
        })
    }
}
</script>

<style lang="scss" scoped>
h2 {
    text-align: center;
    padding: 100px 0px 4px 0px;
}

p {
    margin: auto;
    text-align: center;
}

#products {
    margin: 4rem 10rem;

}

.top {
    background: linear-gradient(to bottom, rgba(0, 162, 255, 0.354), rgba(255, 255, 255, 0));
}

.mx-auto {
    padding: 1rem;
    margin: 1rem 0rem 4rem 0rem;
    box-shadow:
        -4px 4px 10px 0 rgba(51, 96, 133, 0.252),
        12px -12px 16px rgba(255, 255, 255, 0.25);
}

.v-bottom-navigation {
    background: none;
    color: rgb(0, 149, 255);
    box-shadow: none;
}

.v-bottom-navigation button {
    background: rgba(255, 255, 255, 0.264);
    box-shadow:
        -4px 4px 10px 0 rgba(51, 96, 133, 0.252),
        12px -12px 16px rgba(255, 255, 255, 0.25);
    margin-left: 16px;
    border-radius: 10px;
    height: 2px;
}

.v-bottom-navigation .v-bottom-navigation__content>.v-btn {
    font-size: inherit;
    font-weight: bolder;
    height: 3rem;
    max-width: 168px;
    min-width: 80px;
    text-transform: none;
    transition: inherit;
    width: 118px;
    border-radius: 24px;
}

.button-style {
    width: 10rem;
    border-radius: 10px;
    box-shadow: none;
    background: rgba(0, 179, 255, 0.826);
    color: white;
    font-weight: bolder;
    font-size: 18px;
}

.favorite {
    width: 10px;
    margin: auto;
}

.favorite img {
    width: 25px;
    height: 25px;
    object-fit: cover;
}
</style>
