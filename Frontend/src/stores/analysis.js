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
  const nowLoading = ref(false);

  const getAiDetection = async () => {
    const tempStartDate = new Date(startDate.value);
    const tempEndDate = new Date(endDate.value);

    if (tempStartDate.toISOString().startsWith("2024-05-20")) {
      tempStartDate.setFullYear(2024);
      tempStartDate.setMonth(3);
      tempStartDate.setDate(30);
      tempEndDate.setFullYear(2024);
      tempEndDate.setMonth(3);
      tempEndDate.setDate(30);
    }

    const resp = await instance.post("/analytics/ai-detection", {
      "start-time": transformDate(tempStartDate),
      "end-time": transformDate(tempEndDate),
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

    // totalTime.value =
    //   (new Date(endDate.value) - new Date(startDate.value)) / 1000;
    totalTime.value = 3600;

    congestionRatio.value = (totalCongestionTime.value / totalTime.value) * 100;
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
    nowLoading,
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
