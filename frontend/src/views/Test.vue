<template>
  <navigation-bar> </navigation-bar>
  <div class="cards-container">
    <div class="card-wrapper">
      <v-card class="mx-auto">
        <v-card-title style="font-weight: bolder; color: rgba(0, 0, 0, 0.826)"
          >{{ username }}님 의 계좌현황입니다.</v-card-title
        >
        <div>
          <Doughnut
            v-if="chartData.data"
            :data="chartData.data"
            :options="chartData.options"
            width="200"
            height="250"
          />
        </div>

        <div style="width: 100%; text-align: left">
          <v-card-text>
            <div>계좌번호 : {{ accountNumber }}</div>
          </v-card-text>

          <v-card-text>
            <div>계좌 잔고 : {{ asset }}원</div>
          </v-card-text>

          <v-card-text>
            <div>
              계좌 총합금액 : {{ totalPresentAsset + totalinvestment }}원
            </div>
          </v-card-text>

          <v-card-text>
            <div>
              총 수익율 :
              <b class="text-red-lighten-1">
                +{{
                  parseFloat(
                    ((totalPresentAsset - asset) / asset) * 100
                  ).toFixed(2)
                }}%</b
              >
            </div>
          </v-card-text>
        </div>
      </v-card>
    </div>

    <div class="card-wrapper">
      <v-card class="mx-auto">
        <v-card-title style="font-weight: bolder; color: rgba(0, 0, 0, 0.826)"
          >{{ username }}님 의 투자현황입니다.</v-card-title
        >
        <div>
          <Bar
            v-if="chartDataForInvest.data"
            :data="chartDataForInvest.data"
            :options="chartDataForInvest.options"
            width="50"
            height="250"
          />
        </div>
        <div style="width: 100%; text-align: left">
          <v-card-text>
            <div>투자 금액 : {{ totalinvestment }}원</div>
          </v-card-text>

          <v-card-text>
            <div>
              투자 수익 :
              <b class="text-gray-lighten-1"> +{{ totalinvestgain }}원</b>
            </div>
          </v-card-text>

          <v-card-text>
            <div>
              투자 수익율 :
              <b class="text-red-lighten-1">
                +{{
                  parseFloat((totalinvestgain / totalinvestment) * 100).toFixed(
                    2
                  )
                }}%</b
              >
            </div>
          </v-card-text>
        </div>
        <v-card-actions class="d-flex justify-center align-center">
          <v-btn
            style="
              background-color: rgba(0, 179, 255, 0.062);
              border-radius: 10px;
              margin: 0rem 0rem 0.5rem 0.5rem;
            "
            color="blue"
            href="https://haedal.store/profile/edit"
          >
            계좌정보 수정하기
          </v-btn>
        </v-card-actions>
      </v-card>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { onBeforeMount, ref } from "vue";
import router from "../router";
import NavigationBar from "@/components/ProfileNavigationBar.vue";
import { useAuthStore } from "@/store/app";
import { useRouter } from "vue-router";
import { onMounted } from "vue";
import { getApi } from "@/api/modules";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Doughnut } from "vue-chartjs";
import { Bar } from "vue-chartjs";
import { Title, BarElement, CategoryScale, LinearScale } from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

ChartJS.register(ArcElement, Tooltip, Legend);

const authStore = useAuthStore();
const username = ref(0);
const servicePurpose = ref(0);
const asset = ref(0);
const totalPresentAsset = ref(0); //계좌 현재 총 자산액(적금액 모두포함)
const accountNumber = ref(0);
const cleanplusMoney = ref(0);
let servicepurposechange = "";
let count = 0;

let totalinvestment = ref(0);
let totalinvestgain = ref(0);

const chartData = ref({});
const chartDataForInvest = ref({});
// const router = useRouter();

const redirectToPortfolioDays = () => {
  // Vue Router를 사용하여 경로를 변경
  router.push("/portfoliodays");
};

const redirectToPortfoliodibs = () => {
  // Vue Router를 사용하여 경로를 변경
  router.push("/portfoliodibs");
};

// 서버에서 받아오는 정보
const listData = ref([]);

console.log("새로고췸");
// Local Storage에서 토큰을 가져와서 store에 저장
const storedToken = localStorage.getItem("accessToken");
console.log("저장된 토큰값 " + authStore.accessToken);

const labelData = ref(["잔액"]); //잔액라벨 초기값 추가
const moneyData = ref([]); // 잔고
const labelDataForInvest = ref([]); //잔액라벨 초기값 추가
const moneyDataForInvest = ref([]); // 잔고

if (storedToken) {
  console.log("요청전송");
  axios
    .get("https://backend.haedal.store/profile/edit", {
      headers: {
        //   Authorization: `Bearer ${authStore.accessToken}`, // 토큰 포함
        Authorization: `Bearer ${storedToken}`,
      },
    })
    .then((response) => {
      console.log(response.data);
      username.value = response.data.name;
      accountNumber.value = response.data.accountNumber;
      moneyData.value[0] = response.data.asset; // 잔고에 차트추가
      console.log(moneyData.value);
      asset.value = response.data.asset;
      servicePurpose.value = servicepurposechange;
      totalPresentAsset.value = response.data.asset; // 현재남은 잔고를 초기값으로 설정
    });
}

axios({
  method: "get",
  url: "https://backend.haedal.store/subscribe/portfolio",
  headers: {
    Authorization: `Bearer ${localStorage.getItem("accessToken")}`, // 토큰 포함
  },
})
  .then((res) => {
    let tempArr = [...res.data];

    tempArr.forEach((item) => {
      console.log(item);
      listData.value.push(item);

      // chips 표시 내용 변환
      switch (item.servicePurpose) {
        case "MOKDON":
          item.servicePurpose = "목돈 마련";
          break;
        case "FORCAR":
          item.servicePurpose = "자동차 구매";
          break;
        case "FORHOUSE":
          item.servicePurpose = "주택 구매";
          break;
        case "OTHERS":
          item.servicePurpose = "기타";
          break;
      }

      switch (item.userAgeGroup) {
        case "ONE":
          item.userAgeGroup = "10대";
          break;
        case "TWO":
          item.userAgeGroup = "20대";
          break;
        case "THREE":
          item.userAgeGroup = "30대";
          break;
        case "FOUR":
          item.userAgeGroup = "40대";
          break;
        case "FIVE":
          item.userAgeGroup = "50대";
          break;
        case "ONETWOTHREEFOURFIVE":
          item.userAgeGroup = "전연령";
          break;
      }

      const subscribeDate = new Date(item.subscribeDate);
      const endSubscribeDate = new Date(item.endSubscribeDate);
      const timeDifferenceInMilliseconds = endSubscribeDate - subscribeDate;

      // 밀리초를 일로 변환
      const daysDifference =
        timeDifferenceInMilliseconds / (1000 * 60 * 60 * 24);

      const minusfirtsdatefinishdate = daysDifference;
      console.log(minusfirtsdatefinishdate); // 예금 만기일 - 시작일
      const money = item.startMoney; //시작금액
      let n = item.period;

      let totalAmount = 0;
      //예금일때
      if (item.deposit == true) {
        totalAmount = parseFloat(money + (money * item.interestRate) / 100);
        console.log(totalAmount + "예금" + money);

        const plusMoney =
          ((totalAmount - money) / minusfirtsdatefinishdate) *
          item.progressdate; //현재 수익
        item.cleanplusMoney = parseInt(plusMoney); //현재 수익 변수 값 초기화
        const plusPercentage =
          (item.interestRate / minusfirtsdatefinishdate) * item.progressdate; // 현재 수익율
        item.plusPercentage = parseFloat(plusPercentage).toFixed(3);

        const totalMoney = item.presentMoney + item.cleanplusMoney; //현재 총 잔고 더하기
        item.totalMoney = totalMoney;
        totalPresentAsset.value += item.cleanplusMoney;
        console.log(item.cleanplusMoney);
      } else {
        // 적금일때
        totalAmount = money; // 초기 예금액
        for (let i = 0; i < item.progressdate; i++) {
          totalAmount +=
            totalAmount * (item.interestRate / 100 / minusfirtsdatefinishdate); // 각 날짜에 대한 이자 계산
        }

        const plusMoney = totalAmount - money; // 현재 수익
        item.cleanplusMoney = parseInt(plusMoney); // 현재 수익 변수 값 초기화
        const plusPercentage = ((totalAmount - money) / money) * 100; // 수익률 계산
        item.plusPercentage = parseFloat(plusPercentage).toFixed(3);

        const totalMoney = item.presentMoney + item.cleanplusMoney; //현재 총 잔고 더하기
        item.totalMoney = totalMoney;
        totalPresentAsset.value += item.cleanplusMoney;
        console.log(item.cleanplusMoney);
        console.log(totalAmount + " 적금 " + money);
      }
      totalinvestment.value += item.startMoney; // 투자금액
      totalinvestgain.value += item.cleanplusMoney;

      labelData.value.push(item.productName);
      moneyData.value.push(item.presentMoney + item.cleanplusMoney);
      labelDataForInvest.value.push(item.productName);
      moneyDataForInvest.value.push(item.presentMoney + item.cleanplusMoney);
    });

    chartData.value = {
      data: {
        labels: labelData.value,
        datasets: [
          {
            backgroundColor: [
              "rgba(255,26,104,0.2)",
              "rgba(54,162,235,0.2)",
              "rgba(255,206,86,0.2)",
              "rgba(75,192,192,0.2)",
              "rgba(127,26,104,0.2)",
              "rgba(100,162,235,0.2)",
              "rgba(232,206,86,0.2)",
              "rgba(65,192,192,0.2)",
              "rgba(187,26,104,0.2)",
              "rgba(10,162,235,0.2)",
              "rgba(120,206,86,0.2)",
              "rgba(55,192,192,0.2)",
            ],
            borderColor: [
              "rgba(255,26,104,0.2)",
              "rgba(54,162,235,0.2)",
              "rgba(255,206,86,0.2)",
              "rgba(75,192,192,0.2)",
              "rgba(127,26,104,0.2)",
              "rgba(100,162,235,0.2)",
              "rgba(232,206,86,0.2)",
              "rgba(65,192,192,0.2)",
              "rgba(187,26,104,0.2)",
              "rgba(10,162,235,0.2)",
              "rgba(120,206,86,0.2)",
              "rgba(55,192,192,0.2)",
            ],
            data: moneyData.value,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: "bottom",
            align: "start",
            // 범례 스타일링
            labels: {
              usePointStyle: true, // 범례 도형 모양과 관련된 속성으로, false일 경우엔 기본 직사각형 도형으로 표시됩니다.
              padding: 15, // 범례 간 가로 간격을 조정할 수 있습니다. 범례의 상하 padding을 지정하는 기능은 따로 지원되지 않아요. ㅠㅠ
              font: {
                // 범례의 폰트 스타일도 지정할 수 있습니다.
                family: "'Noto Sans KR', 'serif'",
                lineHeight: 1,
              },
            },
          },
        },
      },
    };

    chartDataForInvest.value = {
      data: {
        labels: labelDataForInvest.value,
        datasets: [
          {
            label: "투자금액",
            // backgroundColor: getRandomColors(30),
            backgroundColor: [
              "rgba(255,26,104,0.2)",
              "rgba(54,162,235,0.2)",
              "rgba(255,206,86,0.2)",
              "rgba(75,192,192,0.2)",
            ],
            borderColor: [
              "rgba(255,26,104,0.5)",
              "rgba(54,162,235,0.5)",
              "rgba(255,206,86,0.5)",
              "rgba(75,192,192,0.5)",
            ],
            borderWidth: 1, // [막대 테두리 굵기 설정]
            data: moneyDataForInvest.value,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: "bottom",

            // 범례 스타일링
            labels: {
              usePointStyle: false, // 범례 도형 모양과 관련된 속성으로, false일 경우엔 기본 직사각형 도형으로 표시됩니다.
              padding: 10, // 범례 간 가로 간격을 조정할 수 있습니다. 범례의 상하 padding을 지정하는 기능은 따로 지원되지 않아요. ㅠㅠ
              font: {
                // 범례의 폰트 스타일도 지정할 수 있습니다.
                family: "'Noto Sans KR', 'serif'",
                lineHeight: 1,
              },
            },
          },
        },
      },
    };
  })
  // POST 요청 실패 시 로직
  .catch((error) => {
    console.error(error);
  });

const getRandomColor = () => {
  const letters = "0123456789ABCDEF";
  let color = "#";
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
};

const getRandomColors = (length) => {
  const colors = [];
  for (let i = 0; i < length; i++) {
    colors.push(getRandomColor());
  }
  return colors;
};
</script>
