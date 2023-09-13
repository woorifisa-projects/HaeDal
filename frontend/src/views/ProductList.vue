<template>
    <div style="background-color: rgba(84, 180, 224, 0.06); padding: 10px;">
        <h1 style="text-align: center; margin-top: 50px">해달의 다양한 상품들을 검색해보세요</h1>
        <div style="padding: 10px; display: flex; flex-direction: column; align-items: center; justify-content: center;">
            <form @submit.prevent="searchForm" class="search">
                <input type="text" v-model="searchTerm" class="searchProduct" placeholder="원하시는 상품명을 검색해 주세요">
                <v-btn class="searchButton" @click="searchForm">
                    검색
                </v-btn>
            </form>
        </div>
        <div>

            <v-layout class="overflow-visible" style="height: 56px; box-shadow: none; margin-bottom:30px;">
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
        <div v-if="showNoDataMessage"
            style="font-size:20px; width:500px; font-weight: bold; color:rgba(0, 179, 255, 0.826); text-align: center; margin:auto; margin-top:80px">
            😭 관련 상품이 존재하지 않습니다.😭
        </div>
        <div class="container">

            <v-card class="mx-auto" width="20rem" v-bind:class="item.productName" v-for="( item, index ) in   listData  "
                :key="index" @click=subscribeProduct(item)>

                <v-card-item class="products">
                    <div style="display: flex;flex-direction: column;align-items: center; ">
                        <div style="font-size: 20px; font-weight: bold; margin: 1rem 0rem 1.3rem 0rem;">
                            {{ item.productName }}
                        </div>
                        <div style="font-size: 13px; margin-bottom: 2rem; text-align: left;">
                            {{ item.shortInfo }}
                        </div>
                        <span>
                            <v-chip v-show="item.deposit === false" class="mr-1" color="green"
                                text-color=" white">예금</v-chip>
                            <v-chip v-show="item.deposit === true" class="mr-1" color="red" text-color=" white">적금</v-chip>
                            <v-chip class="mr-1" color="blue" text-color="white"> {{ item.interestRate }}%</v-chip>
                            <v-chip class="mr-1" color="orange" text-color="white"> {{ item.period }}개월</v-chip>
                        </span>
                    </div>
                </v-card-item>

                <span class="favorite" @click="dibs(item)" style="cursor:pointer;">
                    <img v-if="item.isDibs === true" src='@/assets/img/favorite.png'>
                    <img v-else src='@/assets/img/favorite_border.png'>
                </span>

            </v-card>
        </div>
    </div>
</template>

<script setup>
import axios from 'axios'
import { watchEffect, ref } from 'vue'
import router from '../router'
import { mdiConsoleNetwork } from '@mdi/js';
import { useAuthStore } from '@/store/app';

// 서버에서 받아오는 정보
const listData = ref([]);

//사용자가 검색한 단어
const searchTerm = ref('');

// 상품이 없을 때 띄워주는 메세지
const showNoDataMessage = ref(false);

//찜 해두었는지 여부
const isDibs = ref(false);

const authStore = useAuthStore();



// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: 'https://backend.haedal.store', // 서버의 주소
    // baseURL: 'http://15.164.189.153:8080',
    // withCredentials: true // CORS 요청에 관련된 설정을 포함
})

watchEffect(() => {
    axiosInstance.get('/products').then((res) => {
        showNoDataMessage.value = false;
        console.log(res.data);

        let tempArr = [...res.data];
        tempArr.forEach((item) => {
            console.log(item);
            switch (item.tag) {
                case "THEMA":
                    item.tag = '테마'
                    break;
                case 'FINANCE':
                    item.tag = '금융'
                    break;
            }


            // 데이터를 받아온 후에 listData에 추가
            listData.value.push(item);
            console.log(item.tag);
        });

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
    });
});


// 찜하기 버튼 누를 시
const dibs = (item) => {
    if (!item.isDibs) {
        console.log("찜!");
        axios({
            method: "post",
            url: `https://backend.haedal.store/dibs/${item.productId}/add`,
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
            url: `https://backend.haedal.store/dibs/${item.productId}/delete`,
            headers: {
                Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
            },
        })
            .then(() => {
                item.isDibs = false;
            })
            .catch((error) => alert("로그인 후 이용 가능한 서비스 입니다"));
    }
}

//검색 기능
const searchForm = () => {
    //기존 데이터 제거
    listData.value.splice(0, listData.value.length);

    console.log("로그인한 검색기록 로그 저장 요청 보냄");
    console.log(authStore.accessToken);
    showNoDataMessage.value = false;


    listData.value.splice(0, listData.value.length);

    // const searchKeyword = searchTerm.value;
    if (localStorage.getItem("accessToken")) {
        const searchKeyword = searchTerm.value;
        axiosInstance.post(`/products/${searchKeyword}/login`, null, {
            headers: {
                Authorization: `Bearer ${authStore.accessToken}`
            }
        }).then((res) => {
            let tempArr = [...res.data]
            tempArr.forEach((item) => {
                console.log(item)
                switch (item.tag) {
                    case "THEMA":
                        item.tag = '테마'
                        break;
                    case 'FINANCE':
                        item.tag = '금융'
                        break;
                }


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
            if (listData.value.length === 0) {
                setTimeout(() => {
                    showNoDataMessage.value = true;
                }, 200)
            }
        })
    } else {
        const searchKeyword = searchTerm.value;
        axiosInstance.post(`/products/${searchKeyword}`).then((res) => {
            let tempArr = [...res.data]
            tempArr.forEach((item) => {
                console.log(item)
                switch (item.tag) {
                    case "THEMA":
                        item.tag = '테마'
                        break;
                    case 'FINANCE':
                        item.tag = '금융'
                        break;
                }

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
            if (listData.value.length === 0) {
                setTimeout(() => {
                    showNoDataMessage.value = true;
                }, 200)
            }

        })
    }

}


//상품 정보 버튼
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
    showNoDataMessage.value = false;
    axiosInstance.get('/products').then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            switch (item.tag) {
                case "THEMA":
                    item.tag = '테마'
                    break;
                case 'FINANCE':
                    item.tag = '금융'
                    break;
            }

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
        console.log(listData)
    })
}
// 금융 상품 조회 기능
const financial = () => {
    listData.value = [];
    showNoDataMessage.value = false;
    axiosInstance.get(`products/filter/FINANCE`).then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            switch (item.tag) {
                case "THEMA":
                    item.tag = '테마'
                    break;
                case 'FINANCE':
                    item.tag = '금융'
                    break;
            }
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
        console.log(listData)
    })
}
// 테마 상품 조회 기능
const tema = () => {
    listData.value = [];
    showNoDataMessage.value = false;
    axiosInstance.get(`products/filter/THEMA`).then((res) => {
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            console.log(item)
            switch (item.tag) {
                case "THEMA":
                    item.tag = '테마'
                    break;
                case 'FINANCE':
                    item.tag = '금융'
                    break;
            }
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
    box-shadow:
        -4px 4px 10px 0 rgba(51, 96, 133, 0.044),
        12px -12px 16px rgba(255, 255, 255, 0.25);
    padding: 15px;
    border-radius: 15px;
    transition: box-shadow 0.3s ease, transform 0.3s ease;
    outline: 1px solid rgba(0, 153, 248, 0.091);
    height: 250px;

    &:hover {
        box-shadow:
            -1px 5px 10px 0 rgba(51, 96, 133, 0.138);
        transform: translateY(-5px);
    }
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
    padding: 1rem;
    justify-content: center;
}

.products {
    margin-bottom: 20px;
}

.searchProduct {
    display: flex;
    width: 100%;
    height: 100%;
    box-shadow: 2px 4px 10px 0px rgba(0, 140, 200, 0.115);
    background-color: rgb(255, 255, 255);
    border-radius: 30px;
    padding: 10px 1rem;
    outline: none;
    margin: auto;
    transition: box-shadow 0.3s ease;
}

.searchProduct:focus {
    box-shadow: 2px 4px 10px 0px rgba(56, 149, 188, 0.301);
}

.search {
    padding: 10px;
    text-align: center;
    margin-top: 0rem;
    position: relative;
    flex-direction: column;
    margin: 2rem 0.6rem 1rem 0rem;
    width: 600px;
    justify-content: center;
    display: flex;
    text-align: center;
    align-items: center;
    flex-direction: column;
}

.searchButton {
    width: 6rem;
    border-radius: 20px;
    box-shadow: none;
    background: rgba(0, 179, 255, 0.826);
    box-shadow: -2px 4px 10px 0px rgba(0, 39, 62, 0.089);
    color: white;
    font-weight: bolder;
    font-size: 16px;
    position: absolute;
    top: 12.9px;
    left: 30.6em;
    height: 37px !important;
    box-shadow: none;

    &:hover {
        box-shadow: none;
    }

}


.v-bottom-navigation {
    background: none;
    color: rgb(0, 75, 121);
    box-shadow: none;
}

.v-bottom-navigation .v-bottom-navigation__content>.v-btn {
    font-size: 14px;
    transition: inherit;
    background-color: rgba(0, 179, 255, 0.158);
    outline: 1px solid rgba(0, 153, 248, 0.14);
    width: 1px !important;
    margin: 6px;
    height: 40px;
    border-radius: 15px;
    max-width: 100px;
    font-weight: 400;
}


.favorite {
    width: 10px;
    margin: auto;
    margin-top: 30px;
}

.favorite img {
    width: 25px;
    height: 25px;
    object-fit: cover;
}
</style>
