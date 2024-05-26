import { ref } from "vue";
import { defineStore } from "pinia";
import { instance } from "@/util/axios-util";

//test-data
import simulationData from '/test_data/Simulation/SimulationData.json'

export const simulationComponentStore = defineStore(
  "simulationComponentStore",
  () => {
    const intervals = ref([]);
    const ohts = ref([]);
    const emptyData = {
      "simulation-log": [],
    };

    //==시뮬레이션 데이터 시간 나누기==
    const splitTimeRange = (startDate, endDate) => {
      const start = new Date(startDate);
      const end = new Date(endDate);
      start.setHours(start.getHours() + 9);
      end.setHours(end.getHours() + 9);

      intervals.value = [];
      while (start < end) {
        let intervalEnd = new Date(start);

        intervalEnd.setSeconds(start.getSeconds() + 30);

        if (intervalEnd > end) {
          intervalEnd = end;
          intervalEnd.setSeconds(intervalEnd.getSeconds() + 1);
        }

        intervals.value.push({
          start: start.toISOString().slice(0, -5),
          end: intervalEnd.toISOString().slice(0, -5),
        });

        start.setSeconds(start.getSeconds() + 30);
      }
    };

    //==시뮬레이션 데이터를 로드==
    const getSimulation = async (timeOrder, ohtList) => {
      // try {
      //   const resp = await instance.post("/simulation/simulation-log", {
      //     "start-date": intervals.value[timeOrder].start,
      //     "end-date": intervals.value[timeOrder].end,
      //     "oht-id": ohtList,
      //   });
      //   const { data, error } = resp;
      //   if (error) alert("SimulatiomData Not Found \n", error);
      //   else return data;
      // } catch (error) {
      //   return emptyData;
      // }
      return simulationData
    };

    //==에러 시뮬레이션 데이터를 로드==
    const getCongestionSimulation = async (start, end, ohtList) => {
      // try {
      //   const resp = await instance.post("/simulation/simulation-log", {
      //     "start-date": start,
      //     "end-date": end,
      //     "oht-id": ohtList,
      //   });
      //   const { data, error } = resp;
      //   if (error) alert("SimulatiomData Not Found \n", error);
      //   else return data;
      // } catch (error) {
      //   return emptyData;
      // }
      console.log("시뮬 api 쓰고있다")
      return simulationWorkLogSimulationData
    };

    return {
      intervals,
      ohts,
      splitTimeRange,
      getSimulation,
      getCongestionSimulation,
    };
  }
);
