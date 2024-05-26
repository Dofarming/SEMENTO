<script setup>
import { ref, onMounted, watch } from "vue";
import HeadText from "@/components/Text/HeadText.vue";
import DeadLineSimulation from "@/components/simulation/DeadLineSimulation.vue";
import Table from "@/components/table/SideTabInfoTable.vue";
import Cardhead from "@/components/Text/Cardhead.vue";
import { defineExpose } from "vue";
import { simulationComponentStore } from "@/stores/simulationComponent";
const { getCongestionSimulation } = simulationComponentStore();

//test-data
import simulationWorkLogSimulationData from '/test_data/Simulation/SimulationWorkLogSimulation.json'

const headerText = ref("");
const tapNum = ref(0);
const datas = ref([]);
const responseData = ref(null);
const tableView = ref(null);
const simulationView = ref(null);

async function setPage(data, ohtId, idx) {
  if (idx === -1) {
    //test-data
    // simulationView.value.checkPropsChange(null);
    return;
  }
  // 문자열을 분리하여 각 시간 부분을 추출
  const [startString, endString] = data[1].split(" - ");
  headerText.value = data[1];
  tapNum.value = idx + 1;

  // 각각의 문자열을 Date 객체로 변환
  const startDate = new Date(startString);
  const endDate = new Date(endString);

  startDate.setHours(startDate.getHours() + 9);
  endDate.setHours(endDate.getHours() + 9);

  // const response = await getCongestionSimulation(
  //   startDate.toISOString(),
  //   endDate.toISOString(),
  //   [ohtId]
  // );

  //test-data
  const response = simulationWorkLogSimulationData

  responseData.value = response;

  const newDatas = [];

  response["simulation-log"].forEach((log) => {
    const data = [
      log["time"],
      log["data"][0]["oht-id"],
      log["data"][0]["location"]["path"],
      log["data"][0]["status"],
      log["data"][0]["carrier"],
      log["data"][0]["error"],
      log["data"][0]["speed"],
      log["data"][0]["fail"],
    ];

    newDatas.push(data);
  });

  datas.value = newDatas;
  tableView.value.checkPropsChange(datas.value);
  simulationView.value.checkPropsChange(responseData.value);
}

defineExpose({
  setPage,
});
</script>

<template>
  <div class="background">
    <div class="container">
      <div class="left">
        <section class="head-title">
          <div class="badge">#{{ tapNum }}</div>
          <HeadText :headerText="headerText" />
        </section>
      </div>
      <section class="white-box simulation-box">
        <section class="title">
          <Cardhead headerText="시뮬레이션" contentText=""></Cardhead>
        </section>
        <section class="content">
          <DeadLineSimulation ref="simulationView" />
        </section>
      </section>
      <section class="table">
        <Table
          ref="tableView"
          width="100%"
          bodyFontSize="14px"
          headerFontSize="12px"
          header-color="white"
          header-text-color="black"
          :columns="[
            '시간',
            'OHT 호기',
            '경로',
            '상태',
            '물류 적재여부',
            '에러',
            '속도',
            '작업 성공여부',
          ]"
          :data="datas"
        ></Table>
      </section>
    </div>
  </div>
</template>

<style scoped>
.left {
  width: 95%;
}
.head-title {
  margin-top: 30px;
  display: flex;
  gap: 20px;
  align-items: center;
}
.background {
  background-color: #f3f2f7;
  width: 100%;
  z-index: 5;
  /* position: fixed; */
}
.container {
  /* background-color: azure; */
  min-height: 100px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
}
.badge {
  border-radius: 5px;
  background-color: #383839;
  color: white;
  width: 50px;
  height: 40px;
  font-size: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
}
.simulation-box {
  min-height: 400px;
  overflow: hidden;
  width: 95%;
}
.table {
  width: 95%;
}
</style>
