import { ref, computed } from "vue";
import { defineStore } from "pinia";
import { instance } from "@/util/axios-util";
import moment from "moment";

//test-data
import simulationBlackCardData from '/test_data/Simulation/SimulationBlackCardData.json'
import simulationGraphData from '/test_data/Simulation/SimulationGraphData.json'
import simulationWorkLogData from '/test_data/Simulation/SimulationWorkLogData.json'

export const simulationStore = defineStore("simulationStore", () => {
  const startDate = ref();
  const endDate = ref();
  const ohtId = ref();

  //==결과 데이터
  const classificationLog = ref(null);
  const chartData = ref(null);
  const comparedData = ref(null);
  const isDataLoaded = ref(false);

  //testData
  comparedData.value = simulationBlackCardData
  chartData.value = simulationGraphData
  classificationLog.value = simulationWorkLogData

  //차트관련
  const timeArray = ref([null]);
  const meArray = ref([null]);
  const averageArray = ref([null]);
  function setChart() {
    timeArray.value = chartData.value["work-per-time"].map((item) => item.time);
    meArray.value = chartData.value["work-per-time"].map((item) => item.me);
    averageArray.value = chartData.value["work-per-time"].map(
      (item) => item.average
    );
  }

  //==비교관련
  const totalWork = ref();
  const outOfDeadline = ref();
  const averageSpeed = ref();
  const ohtError = ref();
  function setComparedData() {
    totalWork.value = {
      data: comparedData.value["total-work"].data,
      percent: formatNumber(comparedData.value["total-work"].percent),
    };
    outOfDeadline.value = {
      data: comparedData.value["out-of-dead-line"].data,
      percent: formatNumber(comparedData.value["out-of-dead-line"].percent),
    };
    averageSpeed.value = {
      data: formatNumber(comparedData.value["average-speed"].data),
      percent: formatNumber(comparedData.value["average-speed"].percent),
    };
    ohtError.value = {
      data: comparedData.value["oht-error"].data,
      percent: formatNumber(comparedData.value["oht-error"].percent),
    };
  }
  const formatNumber = (value) => {
    const formattedValue = parseFloat(value).toFixed(2);
    return formattedValue.endsWith(".00")
      ? parseInt(formattedValue)
      : formattedValue;
  };

  //== 작업별 분류 관련 데이터
  const logPerWork = ref(null);
  const totalCnt = ref(null);
  function setclassificationLogData() {
    totalCnt.value = classificationLog.value["total-cnt"];
    logPerWork.value = formatLogPerWork(
      classificationLog.value["log-per-work"]
    );
  }
  // 날짜 변환 함수
  function transformatDate(date) {
    return (
      moment(date).format("YYYY-MM-DD") + " " + moment(date).format("HH:mm:ss")
    );
  }

  const formatLogPerWork = (logs) => {
    return logs.map((log, index) => [
      index + 1,
      `${transformatDate(log["start-time"])} - ${transformatDate(
        log["end-time"]
      )}`,
      `${Math.floor(
        (new Date(log["end-time"]).getTime() -
          new Date(log["start-time"]).getTime()) /
          60000
      )}m ${(
        ((new Date(log["end-time"]).getTime() -
          new Date(log["start-time"]).getTime()) %
          60000) /
        1000
      ).toFixed(0)}s`,
      log.errors.join(", "),
      `${log["average-speed"].toFixed(2)} m/s`,
      log["out-of-deadline"].toString().toUpperCase(),
    ]);
  };

  //==세터
  const setStartDate = (newDate) => {
    startDate.value = transformDate(newDate.value);
  };
  const setEndDate = (newDate) => {
    endDate.value = transformDate(newDate.value);
  };
  const setOhtId = (newOhtId) => {
    ohtId.value = newOhtId.value.value;
  };

  //==기타 함수
  function transformDate(date) {
    return (
      moment(date).format("YYYY-MM-DD") + "T" + moment(date).format("HH:mm:ss")
    );
  }

  //==Input 업데이트시 모든 데이터를 새로 업로드
  const getNewResult = async (newStartDate, newEndDate, newOhtId) => {
    if (newStartDate.value == null || newStartDate.value == undefined) {
      //현재 날짜에서 24-05-11로 default 날짜 설정 변경(날짜 임의로 임시 설정함)
      newStartDate.value = new Date(2024, 4, 27).setHours(14, 0, 0, 0);
      newEndDate.value = new Date(2024, 4, 27).setHours(18, 0, 0, 0);
    }

    setStartDate(newStartDate);
    setEndDate(newEndDate);
    setOhtId(newOhtId);

    // await getComparedData();
    setComparedData();

    // await getChartData();
    setChart();

    // await getClassificationLog();
    setclassificationLogData();

    isDataLoaded.value = true;
  };

  //==작업별로 분류된 로그 데이터를 로드==
  const getClassificationLog = async () => {
    const resp = await instance.post("/simulation/classification-log", {
      "start-date": startDate.value,
      "end-date": endDate.value,
      "oht-id": ohtId.value,
    });
    const { data, error } = resp;
    if (error) alert("Classification Log Not Found \n", error);
    else classificationLog.value = data;
  };

  //==작업량 평균비교==
  const getChartData = async () => {
    const resp = await instance.post("/simulation/compare-work", {
      "start-date": startDate.value,
      "end-date": endDate.value,
      "oht-id": ohtId.value,
    });
    const { data, error } = resp;
    if (error) alert("Work Data Not Found \n", error);
    else chartData.value = data;
  };

  //==각종 비교 컴포넌트용==
  const getComparedData = async () => {
    const resp = await instance.post("/simulation/work-information", {
      "start-date": startDate.value,
      "end-date": endDate.value,
      "oht-id": ohtId.value,
    });
    const { data, error } = resp;
    if (error) alert("Compared Data Not Found \n", error);
    else comparedData.value = data;
  };

  return {
    ohtId,
    startDate,
    endDate,
    isDataLoaded,
    //axios 통신
    getNewResult,
    //차트데이터
    timeArray,
    meArray,
    averageArray,

    //비교데이터
    ohtError,
    averageSpeed,
    outOfDeadline,
    totalWork,

    //작업별 데이터
    totalCnt,
    logPerWork,
  };
});
