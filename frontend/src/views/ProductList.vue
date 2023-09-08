<template>
    <div style=" background-color: rgba(0, 179, 255, 0.056); padding: 10px;">
        <form @submit.prevent="searchForm">
            <div class="search">
                <input type="text" v-model="searchTerm" class="searchProduct" placeholder="원하시는 상품명을 검색해 주세요">
                <v-btn class="searchButton" @click="searchForm">
                    검색
                </v-btn>
            </div>
        </form>

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
            <v-divider :thickness="3" color="info" style="width:60%;     border-style: double;
    margin: auto;"></v-divider>
        </div>
        <div v-if="showNoDataMessage"
            style="font-size:20px; width:500px; font-weight: bold; color:rgba(0, 179, 255, 0.826); text-align: center; margin:auto; margin-top:80px">
            :sob: 관련 상품이 존재하지 않습니다.:sob:
        </div>
        <div class="container">

            <v-card class="mx-auto" width="20rem" v-bind:class="item.productName" v-for="( item, index ) in  listData "
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
                        style="background: rgba(0, 179, 255, 0.826); color:white; font-weight: bold; border-radius: 0.6rem;"
                        @click=subscribeProduct(item)>
                        정보 보기
                    </v-btn>
                </v-card-actions>

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
    baseURL: 'http://localhost:8080', // 서버의 주소
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
        -4px 4px 10px 0 rgba(51, 96, 133, 0.252),
        12px -12px 16px rgba(255, 255, 255, 0.25);
    padding: 15px;
    border-radius: 15px;
}

.mx-auto button {
    margin: auto;
}

.container {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    grid-template-rows: repeat(3, minmax(100px, auto));
    grid-gap: 30px;
    margin: 10px 20rem 10rem 20rem;
    padding: 1rem;
    justify-content: center;
}


.searchProduct {
    width: 40%;
    height: 100%;
    box-shadow: 2px 4px 10px 0px rgba(0, 140, 200, 0.219);
    background-color: rgb(255, 255, 255);
    border-radius: 30px;
    margin: 2rem 0.6rem 1rem 0rem;
    padding: 10px 1rem;
    outline: none;
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
    height: 40px !important;
}


.search {
    padding: 10px;
    text-align: center;
    margin-bottom: 2rem;
    margin-top: 0rem;
}

.v-bottom-navigation {
    background: none;
    color: rgb(0, 75, 121);
    box-shadow: none;
}

.v-bottom-navigation .v-bottom-navigation__content>.v-btn {
    font-size: 16px;
    font-weight: bold;
    transition: inherit;
    background-color: rgba(0, 179, 255, 0.176);

    width: 100px;
    margin: -2px 4px;
    border-radius: 20px;
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