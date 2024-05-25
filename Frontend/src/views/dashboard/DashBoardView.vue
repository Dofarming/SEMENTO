<script setup>
// 컴포넌트
import BlackDataCard from "@/components/card/BlackDataCard.vue";
import Line from "@/components/line/Line.vue";
import HeadText from "@/components/Text/HeadText.vue";
import Cardhead from "@/components/Text/Cardhead.vue";
import Table from "@/components/table/Table.vue";
import WhiteCard from "./components/state-analysis/WhiteCard.vue";
import Loading from "@/components/loading/Loading.vue";
import Button from "@/components/button/Button.vue";
import AiResultSideTabView from "@/views/dashboard/AiResultSideTabView.vue";

// 차트
import StickChart from "./components/job-analysis/StickChart.vue";
import PieChart from "./components/job-analysis/PieChart.vue";
import DoughnutChart from "./components/job-analysis/DoughnutChart.vue";
import LineChart from "./components/state-analysis/LineChart.vue";

// vue
import { useDashboardStore } from "@/stores/dashboard";
import { useAISolutionNotificationStore } from "@/stores/ainotification";
const dashboardStore = useDashboardStore();
const AISolutionNotificationStore = useAISolutionNotificationStore();
import { onMounted, watch, ref } from "vue";
import { timeout } from "d3";

// 날짜 계산
const months = [
  "January", // 1월
  "February", // 2월
  "March", // 3월
  "April", // 4월
  "May", // 5월
  "June", // 6월
  "July", // 7월
  "August", // 8월
  "September", // 9월
  "October", // 10월
  "November", // 11월
  "December", // 12월
];
const currentDate = new Date();
const year = currentDate.getFullYear(); // 년도 가져오기
const month = currentDate.getMonth(); // 월 가져오기 (0부터 시작하므로 +1 해줘야 함)
const firstDayOfMonth = new Date(year, month, 1);
const lastDayOfMonth = new Date(year, month + 1, 0);

// 시작 일이랑 마지막 일 계산
const options = {
  timeZone: "Asia/Seoul",
  year: "numeric",
  month: "2-digit",
  day: "2-digit",
};
const dateFormatter = new Intl.DateTimeFormat("ko-KR", options);
const startTime =
  dateFormatter
    .format(firstDayOfMonth)
    .replace(/(\d{4})\. (\d{2})\. (\d{2})\./, "$1-$2-$3") + "T00:00:00";
const endTime =
  dateFormatter
    .format(lastDayOfMonth)
    .replace(/(\d{4})\. (\d{2})\. (\d{2})\./, "$1-$2-$3") + "T23:59:59";

// ===========================
const nowLoading = ref(true); //로딩창 기본 활성화

// ===========================
// 실패 에러 로그
const errorLog = ref([]);
const deadline = ref("");
const averageWork = ref("");
const averageIdle = ref("");
const maxJobTime = ref({
  startTime: "",
  endTime: "",
});
const maxWorkTime = ref({
  startTime: "",
  endTime: "",
});
const maxIdleTime = ref({
  startTime: "",
  endTime: "",
});
const buttonClick = ref(false);
const sideTabView = ref(null);
const buttonLoading = ref(false); // button의 로딩 표시를 위한 변수

function handleButton() {
  // 만약 buttonClick이 false이면
  // spin이 2초동안 돌아야 함
  // 2초가 지나면 check icon으로 변경시킬 예정
  if (!buttonClick.value && !dashboardStore.buttonFirstTime) {
    buttonLoading.value = true;
    dashboardStore.buttonFirstTime = true;
    setTimeout(() => {
      buttonLoading.value = false;
      dashboardStore.buttonLoadDone = true;
      AISolutionNotificationStore.sendAISolutionNotification(); // 알림 띄우기
      // buttonClick.value = !buttonClick.value;
    }, 2000);
  } else {
    buttonClick.value = !buttonClick.value;
  }
}

onMounted(async () => {
  if (dashboardStore.startTime == "" && dashboardStore.endTime == "") {
    await dashboardStore.getOhtJobAnalysis(startTime, endTime);
    await dashboardStore.getJobResultAnalysis(startTime, endTime);
    await dashboardStore.getStateAnalysis(startTime, endTime);
    await dashboardStore.getStateHourlyAnalysis(startTime, endTime);
    await dashboardStore.getOhtJobHourly(startTime, endTime);
    dashboardStore.startTime = startTime;
    dashboardStore.endTime = endTime;
  }
  // 상태 분석 데이터 전처리
  deadline.value =
    Math.floor(dashboardStore.stateAnalysisData["deadline"].data / 60) +
    "m " +
    (dashboardStore.stateAnalysisData["deadline"].data % 60) +
    "s";
  averageWork.value =
    Math.floor(
      dashboardStore.stateAnalysisData["average-work-time"].data / 60
    ) +
    "m " +
    (dashboardStore.stateAnalysisData["average-work-time"].data % 60) +
    "s";
  averageIdle.value =
    Math.floor(
      dashboardStore.stateAnalysisData["average-idle-time"].data / 60
    ) +
    "m " +
    (dashboardStore.stateAnalysisData["average-idle-time"].data % 60) +
    "s";

  // 상태 시간대 분석 데이터 전처리
  timeDataFormatting(
    dashboardStore.stateHourlyAnalysisData["max-job-time"],
    maxJobTime
  );
  timeDataFormatting(
    dashboardStore.stateHourlyAnalysisData["max-work-time"],
    maxWorkTime
  );
  timeDataFormatting(
    dashboardStore.stateHourlyAnalysisData["max-idle-time"],
    maxIdleTime
  );

  // 에러 로그 데이터 전처리
  errorLog.value = [];
  errorLog.value = dashboardStore.jobResultAnalysisData[
    "job-result-error-log"
  ].map((item, index) => [
    index + 1,
    String(item["oht-id"]),
    String(item["error"]),
    String(item["count"]),
  ]);

  nowLoading.value = false;
});

watch(
  () => dashboardStore.watchedJobResultAnalysisData,
  (oldValue, newValue) => {},
  { deep: true }
);

function timeDataFormatting(temp, refData) {
  if (temp < 10) {
    refData.value.startTime = "0" + temp + ":00";
    if (temp + 1 == 24) refData.value.endTime = "00:00";
    else refData.value.endTime = "0" + (temp + 1) + ":00";
  } else {
    refData.value.startTime = temp + ":00";
    if (temp + 1 == 24) refData.value.endTime = "00:00";
    else refData.value.endTime = temp + 1 + ":00";
  }
}

const formatNumber = (value) => {
  const formattedValue = parseFloat(value).toFixed(2);
  return formattedValue.endsWith(".00")
    ? parseInt(formattedValue)
    : formattedValue;
};
</script>
<template>
  <div v-if="nowLoading"><Loading /></div>
  <div v-else="!nowLoading" class="body-container">
    <div class="col container-header">
      <div class="row space-between">
        <div class="search-period">Months</div>
        <div class="search-date">{{ months[month] }}, {{ year }}</div>
        <div class="ai-solution">
          <Button
            title="AI 솔루션"
            backgroundColor="#003CB0"
            fontColor="white"
            width="90px"
            @click="handleButton"
          />
          <font-awesome-icon
            :icon="['fas', 'spinner']"
            style="color: #74c0fc"
            spin
            v-if="buttonLoading"
            class="spinner-icon"
          />
          <font-awesome-icon
            :icon="['fas', 'check']"
            style="color: #74c0fc"
            v-if="dashboardStore.buttonLoadDone"
            class="spinner-icon"
          />
        </div>
      </div>
    </div>
    <Line />
    <div class="container col gap-40">
      <!-- 작업 분석 -->
      <div class="col">
        <HeadText header-text="# Job Analysis" />
        <!-- 작업 분석 - 시간대별 작업량 -->
        <div class="container-item row gap-20">
          <div class="col">
            <BlackDataCard
              title="가용 중인 OHT 수"
              :content="
                dashboardStore.ohtJobAnalysisData['oht-count'].data + ' 대'
              "
              :width="'320px'"
              :height="'130px'"
            />
            <BlackDataCard
              title="총 생산량"
              :content="
                dashboardStore.ohtJobAnalysisData['total-work'].data + ' 건'
              "
              :percentage="
                formatNumber(
                  dashboardStore.ohtJobAnalysisData['total-work'].percent
                ) + '%'
              "
              :fontColor="
                dashboardStore.ohtJobAnalysisData['total-work'].percent >= 0
                  ? 'red'
                  : 'blue'
              "
              :width="'320px'"
              :height="'130px'"
            />
            <BlackDataCard
              title="OHT 한달 평균 생산량"
              :content="
                formatNumber(
                  dashboardStore.ohtJobAnalysisData['average-work'].data
                ) + ' 건'
              "
              :width="'320px'"
              :height="'130px'"
            />
          </div>
          <div class="white-box job-time">
            <div class="title">
              <Cardhead
                header-text="시간대 별 작업량"
                content-text="한달동안 시간대 별 OHT의 작업량을 보여줍니다."
              />
            </div>
            <div class="padding-left-20">
              <StickChart
                width="100%"
                height="310px"
                :startTime="startTime"
                :endTime="endTime"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 작업 분석 - 작업 성공 실패 -->
      <div class="col">
        <div class="row gap-20">
          <div class="col">
            <div class="white-box job-chart">
              <div class="title">
                <Cardhead
                  header-text="작업 성공률"
                  content-text="전체 작업에 대해 데드라인 내에 도착한 OHT의 비율을 보여줍니다."
                />
              </div>
              <PieChart width="200px" height="200px" />
            </div>
            <div class="white-box job-chart">
              <div class="title">
                <Cardhead
                  header-text="작업 실패 중 에러 비율"
                  content-text="실패한 작업 중 에러가 발생한 비율을 보여줍니다."
                />
              </div>
              <DoughnutChart width="200px" height="200px" />
            </div>
          </div>
          <div class="white-box job-table">
            <div class="title">
              <Cardhead
                :header-text="`에러로 실패한 작업 로그(${errorLog.length}건)`"
                content-text="데드라인까지 도착하지 못한 OHT에 대한 에러를 보여줍니다."
              />
            </div>
            <div class="padding-left-20">
              <Table
                width="100%"
                :columns="['번호', 'OHT 호기', '에러', '개수']"
                :data="errorLog"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 상태 분석 -->
      <div class="col">
        <HeadText header-text="# State Analysis" />
        <div class="container-item row gap-20">
          <div class="col">
            <BlackDataCard
              title="OHT 데드라인"
              :content="deadline"
              :width="'320px'"
              :height="'130px'"
            />
            <BlackDataCard
              title="평균 생산 시간"
              :content="averageWork"
              :percentage="
                formatNumber(
                  dashboardStore.stateAnalysisData['average-work-time'].percent
                ) + '%'
              "
              :fontColor="
                dashboardStore.stateAnalysisData['average-work-time'].percent >=
                0
                  ? 'red'
                  : 'blue'
              "
              :width="'320px'"
              :height="'130px'"
            />
            <BlackDataCard
              title="평균 유휴 시간"
              :content="averageIdle"
              :percentage="
                formatNumber(
                  dashboardStore.stateAnalysisData['average-idle-time'].percent
                ) + '%'
              "
              :fontColor="
                dashboardStore.stateAnalysisData['average-idle-time'].percent >=
                0
                  ? 'red'
                  : 'blue'
              "
              :width="'320px'"
              :height="'130px'"
            />
          </div>
          <div class="col" style="width: 100%; height: 100%">
            <div class="white-box line-chart">
              <div class="title">
                <Cardhead
                  header-text="시간대 별 작업/유휴 상태 OHT 수"
                  content-text="시간대 별로 작업 중인 OHT와 유휴 상태의 OHT 수를 보여줍니다."
                />
              </div>
              <div class="padding-left-20">
                <LineChart height="220px" />
              </div>
            </div>
            <div class="row" style="width: 100%">
              <WhiteCard
                title="생산량이 가장 많은 시간대"
                :startTime="maxJobTime.startTime"
                :endTime="maxJobTime.endTime"
                width="33%"
                height="70px"
              />
              <WhiteCard
                title="OHT가 가장 활발한 시간대"
                :startTime="maxWorkTime.startTime"
                :endTime="maxWorkTime.endTime"
                width="33%"
                height="70px"
              />
              <WhiteCard
                title="유휴 상태가 많은 시간대"
                :startTime="maxIdleTime.startTime"
                :endTime="maxIdleTime.endTime"
                width="33%"
                height="70px"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="side-page" :class="{ open: buttonClick }">
      <section>
        <font-awesome-icon
          @click="handleButton"
          :icon="['fas', 'angles-right']"
          size="2xl"
          style="color: #383839; margin-left: 15px; margin-top: 20px"
        />
      </section>
      <AiResultSideTabView ref="sideTabView" />
    </div>
  </div>
  <div class="footer"></div>
</template>
<style scoped>
.row {
  display: flex;
  flex-direction: row;
  gap: 10px;
}

.col {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.gap-20 {
  gap: 20px;
}
.gap-30 {
  gap: 30px;
}
.gap-40 {
  gap: 40px;
}

.padding-left-20 {
  width: 100%;
  padding-left: 30px;
}

.container-header {
  padding-left: 30px;
}
.container {
  width: 100%;
  height: 100%;
  padding: 10px 30px;
  overflow: hidden;
}

.search-period {
  width: 90px;
  height: 30px;
  line-height: 30px;
  background-color: white;
  color: black;
  text-align: center;
  font-size: small;
  font-weight: 700;
  border-radius: 5px;
}
.search-date {
  height: 30px;
  line-height: 30px;
  color: #9f9f9f;
  font-size: small;
  font-weight: 400;
  margin-left: 10px;
}

.ai-solution {
  display: flex;
  align-items: center;
  gap: 10px; /* 간격 추가 */
  margin-left: auto;
  margin-right: 3%;
}

.job-time {
  width: 100%;
}

.bar-chart {
  padding: 20px 0;
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
}

.job-chart {
  width: 320px;
  height: 350px;
}

.job-table {
  width: 100%;
  height: 710px;
  overflow-y: auto;
}

.line-chart {
  width: 100%;
  padding: 20px 0;
}
.space-between {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.side-page {
  width: 62%;
  height: 100%;
  background-color: #f3f2f7;
  position: fixed;
  top: 0;
  right: -62%; /* 초기 위치는 오른쪽 바깥에 있습니다 */
  transition: right 0.5s;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.09);
  overflow: scroll;
  z-index: 2;
}

.side-page.open {
  right: 0; /* 열릴 때 위치 */
}
</style>
