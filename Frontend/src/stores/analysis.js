import { ref, computed } from "vue";
import { defineStore } from "pinia";
import { instance } from "@/util/axios-util";
import moment from "moment";

export const useAnalysisStore = defineStore("analysisStore", () => {
  const startDate = ref();
  const endDate = ref();
  const detectionResult = ref([]);
  const totalTime = ref(0);
  const totalCongestionTime = ref(0);
  const congestionRatio = ref(0);

  const getAiDetection = async () => {
    const resp = await instance.post("/analytics/ai-detection", {
      "start-time": startDate.value,
      "end-time": endDate.value,
    });
    const { data, error } = resp;
    if (error) alert("Ai Detection Data Not Found \n", error);
    else {
      detectionResult.value = data["detection-result"];
    }

    detectionResult.value.forEach((result) => {
      totalCongestionTime.value +=
        (new Date(result["end-date"]) - new Date(result["start-date"])) / 1000;
    });

    totalTime.value =
      (new Date(endDate.value) - new Date(startDate.value)) / 1000;

    congestionRatio.value = (totalCongestionTime.value / totalTime.value) * 100;

    // console.log("analysis.js안에서");
    // console.log("detectionResult.value:", detectionResult.value);
    // console.log("totalCongestionTime.value", totalCongestionTime.value);
    // console.log("totalTime.value", totalTime.value);
    // console.log("congestionRatio.value: ", congestionRatio.value);
  };
  const computedDetectionResult = computed(() => detectionResult.value);
  const computedCongestionRatio = computed(() => congestionRatio.value);
  const computedStartDate = computed(() => startDate.value);
  const computedEndDate = computed(() => endDate.value);

  function transformDate(date) {
    return (
      moment(date).format("YYYY-MM-DD") + "T" + moment(date).format("HH:mm:ss")
    );
  }

  const setStartDate = (newDate) => {
    startDate.value = transformDate(newDate.value);
  };
  const setEndDate = (newDate) => {
    endDate.value = transformDate(newDate.value);
  };

  const getNewAIDetection = async (newStartDate, newEndDate) => {
    setStartDate(newStartDate);
    setEndDate(newEndDate);

    await getAiDetection();
  };

  return {
    startDate,
    endDate,
    computedDetectionResult,
    totalCongestionTime,
    totalTime,
    congestionRatio,
    computedCongestionRatio,
    computedStartDate,
    computedEndDate,
    getAiDetection,
    detectionResult,
    getNewAIDetection,
  };
});
