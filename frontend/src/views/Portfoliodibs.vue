
<template>
    <navigation-bar> </navigation-bar>
    <div style="text-align: center; margin-top:40px">
        <h3 style="margin-bottom:10px">{{ username }}님이 찜한 상품들입니다.</h3>
        <p style="margin-bottom:40px">총 <span style="font-weight: bold; color:rgba(0, 179, 255, 0.826);">{{ listData.length
        }}개</span>의 상품을 찜 하셨습니다.
        </p>
    </div>

    <div style="margin-bottom: 200px;">
        <div id="products" v-bind:class="item.productName" v-for="(item, index) in listData" :key="index">

            <v-card class="mx-auto" max-width="700px" min-width="300px">
                <v-card-item style="padding: 20px;">
                    <div>
                        <div class="mb-3" style="font-weight: bolder;font-size:20px;">
                            {{ item.productName }}
                        </div>
                        <div class=" mb-3" style="font-size:14px">
                            <span>
                                <b>상품 기간 :</b> {{ item.period }}개월,
                                <b>금리 :</b> {{ item.interestRate }}%,
                                <b>최소 가입 금액 :</b> {{ item.requiredStartMoney }}원
                            </span>
                        </div>
                        <div style="font-size:13px">{{ item.longInfo }}</div>
                    </div>
                </v-card-item>
                <div class="d-flex justify-end align-center ">
                    <span class="favorite" @click="dibs(item)" style="cursor:pointer;margin-right: 10px;">
                        <img v-if="item.isDibs === true" src='@/assets/img/favorite.png' alt="찜 완료">
                        <img v-else src='@/assets/img/favorite_border.png' alt="찜 미완료">
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
import NavigationBar from '@/components/ProfileNavigationBar.vue';
import { useAuthStore } from '@/store/app';


// 서버에서 받아오는 정보
const listData = ref([]);
const authStore = useAuthStore();
const username = ref(0);
const servicePurpose = ref(0);
const asset = ref(0);
const totalPresentAsset = ref(0); //계좌 현재 총 자산액(적금액 모두포함)
const accountNumber = ref(0);
let servicepurposechange = '';

const redirectToPortfolio = () => {
    // Vue Router를 사용하여 경로를 변경
    router.push('/portfolio');
};

const redirectToPortfolioDays = () => {
    // Vue Router를 사용하여 경로를 변경
    router.push('/portfoliodays');
};

const redirectToPortfoliodibs = () => {
    // Vue Router를 사용하여 경로를 변경
    router.push('/portfoliodibs');
};

// console.log("새로고췸");
// Local Storage에서 토큰을 가져와서 store에 저장
const storedToken = localStorage.getItem('accessToken');
// console.log("저장된 토큰값 " + authStore.accessToken);

if (storedToken) {
    // console.log("요청전송");
    axios.get("https://backend.haedal.store/profile/edit", {
        headers: {
            //   Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
            Authorization: `Bearer ${storedToken}`
        },
    })
        .then(response => {
            switch (response.data.servicePurpose) {
                case 'MOKDON':
                    servicepurposechange = '목돈 마련'
                    break;
                case 'FORCAR':
                    servicepurposechange = '자동차 구매'
                    break;
                case 'FORHOUSE':
                    servicepurposechange = '주택 구매'
                    break;
                case 'OTHERS':
                    servicepurposechange = '기타'
                    break;
            }

            // console.log(response.data);
            username.value = response.data.name;
            accountNumber.value = response.data.accountNumber;
            asset.value = response.data.asset; // 잔고 남은 금액
            servicePurpose.value = servicepurposechange;
            totalPresentAsset.value = response.data.asset; // 현재남은 잔고를 초기값으로 설정
        })
}

// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: 'https://backend.haedal.store',
    // baseURL: 'http://15.164.189.153:8080', // 서버의 주소

})

watchEffect(() => {
    axiosInstance.get('/subscribe/portfolio/dibs', {
        headers: {
            Authorization: `Bearer ${localStorage.getItem('accessToken')}`
        }
    }).then((res) => {
        // console.log("정상동작")
        let tempArr = [...res.data]
        tempArr.forEach((item) => {
            // console.log(item)
            listData.value.push(item)
        })

        // 모든 데이터를 받아온 후에 찜 여부를 확인
        listData.value.forEach((item) => {
            axiosInstance.get(`/dibs/${item.productId}/check`, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}`
                }
            }).then((res) => {
                item.isDibs = res.data; // 상품 객체에 찜 여부 추가
                // console.log(item.isDibs);
            }).catch((error) => {
                // 로그인 되어 있지 않을 시 무조건 false
                item.isDibs = false;
            });
        });
        // console.log(listData);
    }).catch((error) => {
        // router.push('/error');
    })
})

// 찜하기 버튼 누를 시
const dibs = (item) => {
    if (!item.isDibs) {
        // console.log("찜!");
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
        // console.log("찜 취소");
        axios({
            method: "delete",
            url: `https://backend.haedal.store/dibs/${item.productId}/delete`,
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


//신청하기 버튼
const subscribeProduct = (item) => {
    const productId = item.productId;
    const productName = item.productName
    // console.log(productName);
    // console.log(item.deposit);
    if (item.deposit == true) {
        router.push(
            {
                name: 'subscribeD',
                params: {
                    id: productId,
                },
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("accessToken")}`
                }
            })
    } else if (item.deposit == false) {
        router.push({
            name: 'subscribeI',
            params: {
                id: productId,
            },
            headers: {
                Authorization: `Bearer ${localStorage.getItem("accessToken")}`
            }
        })
    }
}

// 여기서부터 위에 계좌 컬럼 두개띄우기위한 뻘코드..

axios({
    method: "get",
    url: "https://backend.haedal.store/subscribe/portfolio",
    headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 토큰 포함
    },
}).then(res => {

    let tempArr = [...res.data]
    tempArr.forEach((item) => {
        // console.log(item)
        // listData.value.push(item)

        const subscribeDate = new Date(item.subscribeDate);
        const endSubscribeDate = new Date(item.endSubscribeDate);
        const timeDifferenceInMilliseconds = endSubscribeDate - subscribeDate;

        // 밀리초를 일로 변환
        const daysDifference = timeDifferenceInMilliseconds / (1000 * 60 * 60 * 24);

        const minusfirtsdatefinishdate = daysDifference;
        // console.log(minusfirtsdatefinishdate); // 예금 만기일 - 시작일
        const money = item.startMoney; //시작금액
        let n = item.period;

        let totalAmount = 0;
        //예금일때
        if (item.deposit == true) {
            totalAmount = parseFloat(money + (money * (item.interestRate) / 100));
            // console.log(totalAmount + "예금" + money);

            const plusMoney = ((totalAmount - money) / minusfirtsdatefinishdate) * (item.progressdate); //현재 수익
            item.cleanplusMoney = parseInt(plusMoney); //현재 수익 변수 값 초기화
            const plusPercentage = ((item.interestRate) / minusfirtsdatefinishdate) * (item.progressdate);// 현재 수익율
            item.plusPercentage = parseFloat(plusPercentage).toFixed(3);

            const totalMoney = item.presentMoney + item.cleanplusMoney; //현재 총 잔고 더하기
            item.totalMoney = totalMoney;
            totalPresentAsset.value += item.cleanplusMoney;
            // console.log(item.cleanplusMoney);
        }
        else { // 적금일때
            totalAmount = money; // 초기 예금액
            for (let i = 0; i < item.progressdate; i++) {
                totalAmount += (totalAmount * ((item.interestRate / 100) / minusfirtsdatefinishdate)); // 각 날짜에 대한 이자 계산
            }

            const plusMoney = (totalAmount - money); // 현재 수익
            item.cleanplusMoney = parseInt(plusMoney); // 현재 수익 변수 값 초기화
            const plusPercentage = ((totalAmount - money) / money) * 100; // 수익률 계산
            item.plusPercentage = parseFloat(plusPercentage).toFixed(3);


            const totalMoney = item.presentMoney + item.cleanplusMoney; //현재 총 잔고 더하기
            item.totalMoney = totalMoney;
            totalPresentAsset.value += item.cleanplusMoney;
            // console.log(item.cleanplusMoney);
            // console.log(totalAmount + " 적금 " + money);
        }
    })
})
    // POST 요청 실패 시 로직
    .catch(error => {
        console.error(error);
    });

</script>

<style lang="scss" scoped>
.mx-auto {
    text-align: center;
    justify-content: center;
}

.mx-auto button {
    margin: auto;
}

.button-style {
    width: 10rem;
    border-radius: 10px;
    box-shadow: none;
    background: rgba(0, 179, 255, 0.826);
    color: white;
    margin-top: 14px;
    font-weight: bolder;
    font-size: 18px;
}

.mx-auto {
    padding: 1rem;
    margin: 1rem 0rem 4rem 0rem;
    box-shadow:
        -4px 4px 10px 0 rgba(51, 96, 133, 0.252),
        12px -12px 16px rgba(255, 255, 255, 0.25);
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