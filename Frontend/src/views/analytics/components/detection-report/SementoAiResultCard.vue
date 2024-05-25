<script setup>
import Text from "@/components/Text/Text.vue";
import Table from "@/components/table/Table.vue";
import CongestionSimulation from "@/components/simulation/CongestionSimulation.vue";
import Line from "@/components/line/Line.vue";
import { useAnalysisStore } from "@/stores/analysis";
import { simulationComponentStore } from "@/stores/simulationComponent";
import { ref, onMounted } from "vue";
const { getCongestionSimulation } = simulationComponentStore();

const { detectionResult } = useAnalysisStore();

const props = defineProps({
  location: {
    type: Boolean,
    default: false,
  },
  number: {
    type: Number,
    default: 0,
  },
  text: {
    type: String,
    default: "2024.01.07 12:03:21 ~ 2024.01.07 12:05:59 [총 2분 38초]",
  },
  cause: {
    type: String,
    default: "Facility Error",
  },
  accuracy: {
    type: String,
    default: "",
  },
});

const logs = ref([]);
const datas = ref([]);

onMounted(async () => {
  const start = new Date(detectionResult[props.number - 1]["start-date"]); //에러 시작 시간을 마지막 시간으로 설정해야함
  const end = new Date(detectionResult[props.number - 1]["end-date"]);
  start.setSeconds(start.getSeconds() - 15);
  start.setHours(start.getHours() + 9);
  end.setHours(end.getHours() + 9);
  logs.value = await getCongestionSimulation(
    start.toISOString().slice(0, -5),
    end.toISOString().slice(0, -5),
    [detectionResult[props.number - 1]["cause-oht"]]
  );

  if (start.toISOString().startsWith("2024-04-30")) {
    start.setFullYear(2024);
    start.setMonth(4);
    start.setDate(20);
    end.setFullYear(2024);
    end.setMonth(4);
    end.setDate(20);
  }

  logs.value["simulation-log"].forEach((log) => {
    let [, year, month, day, hour, minute, second] = log["time"].match(
      /(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})/
    );

    if (year == "2024" && month == "04" && day == "30") {
      month = "05";
      day = "20";
    }
    const dateString = `${year}.${month}.${day} ${hour}:${minute}:${second}`;
    const data = [
      dateString,
      log["data"][0]["oht-id"],
      log["data"][0]["location"]["path"],
      log["data"][0]["status"],
      log["data"][0]["carrier"],
      log["data"][0]["error"],
      log["data"][0]["speed"] + "m/s",
      log["data"][0]["fail"],
    ];
    datas.value.push(data);
  });
});
</script>

<template>
  <div class="transparency-box">
    <!-- 제목 -->
    <Text class="title" :location="location" :number="number" :text="text" />
    <Line />
    <!-- 본문 -->
    <section class="content">
      <div class="ai">
        <div class="blue block">[ SEMENTO AI 분석결과 ]</div>
        <div class="white block">{{ cause }} {{ accuracy }}</div>
      </div>
      <div class="simulation-and-table">
        <CongestionSimulation
          class="simulation"
          :errorData="detectionResult[props.number - 1]"
          :number="props.number"
        />
        <div class="table">
          <Table
            width="100%"
            bodyFontSize="14px"
            headerFontSize="12px"
            headerColor="white"
            headerTextColor="black"
            :columns="[
              'TIME',
              'OHT ID',
              'PATH',
              'STATUS',
              'CARRIER',
              'ERROR',
              'SPEED',
              'IS_FAIL',
            ]"
            :data="datas"
          />
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.simulation-and-table {
  /* border: 1px solid black; */
  width: 95%;
  height: 400px;
  display: flex;
  justify-content: space-between;
}
.simulation {
  width: 33%;
  background-color: white;
  border-radius: 5px;
}
.table {
  width: 65%;
  overflow: scroll;
  overflow-x: hidden;
}
.content {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 15px;
}
.ai {
  display: flex;
  width: 90%;
}
.block {
  border-radius: 5px;
  padding: 10px;
  display: flex;
  justify-content: center;
  font-weight: bold;
}
.blue {
  border-radius: 5px;
  background-color: #003cb0;
  color: white;
  width: 50%;
}
.white {
  color: #003cb0;
  background-color: white;
  width: 50%;
}
.title {
  margin-left: 23px;
}
.transparency-box {
  width: 97%;
  background-color: rgb(255, 255, 255, 0.3);
  display: flex;
  flex-direction: column;
  border-radius: 5px;
  box-shadow: 0px 5px 20px rgba(0, 0, 0, 0.05);
  gap: 15px;
  padding: 20px 10px 10px 0px;
  min-height: 400px;
}
</style>
