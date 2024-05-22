<script setup>
import { ref, onMounted, watchEffect, watch } from "vue";
import { Chart, registerables } from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";
import { useAnalysisStore } from "@/stores/analysis";

const analysisStore = useAnalysisStore();

const props = defineProps({
  width: {
    type: String,
    default: "100%",
  },
  height: {
    type: String,
    default: "100%",
  },
});

const labels = ref([]);
const duration_times = ref([]);

const congestion_times = [
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
  "2024.01.08 15:10:00",
];

Chart.register(...registerables);
Chart.register(ChartDataLabels);

const lineChart = ref(null);

//update를 위한 변수
let lineChartRef = null;

watch(
  () => analysisStore.computedDetectionResult,
  (newValue, oldValue) => {
    const len = newValue.length;
    labels.value = Array.from({ length: len }, (_, i) => (i + 1).toString());

    duration_times.value = newValue.map((result) => {
      const timeDiff =
        (new Date(result["end-date"]) - new Date(result["start-date"])) / 1000;
      return timeDiff;
    });
    if (lineChartRef) {
      lineChartRef.data.labels = labels.value;
      lineChartRef.data.datasets[0].data = duration_times.value;
      lineChartRef.update();
    }
  },
  { immediate: true }
);

onMounted(async () => {
  //라인차트
  drawLine();
  //실시간 변화시 차트 업데이트
  watchEffect(() => {
    //라인차트 업데이트
    if (lineChartRef) {
      lineChartRef.data.labels = labels.value;
      lineChartRef.data.datasets[0].data = duration_times.value;
      lineChartRef.update();
    }
  });
});

function drawLine() {
  const ctx = lineChart.value.getContext("2d");
  let gradientFill = ctx.createLinearGradient(0, 0, 0, 200);
  gradientFill.addColorStop(0, "rgba(45, 156, 219, 0.6)");
  gradientFill.addColorStop(1, "rgba(45, 156, 219, 0.05)");

  const data = {
    labels: labels.value,

    datasets: [
      {
        label: "Duration Time",
        data: duration_times.value,
        borderColor: "#2D9CDB",
        tension: 0.5,
        pointStyle: "Rounded",
        pointRadius: 1,
        pointBorderWidth: 0.1,
        pointHoverRadius: 5,
        pointHoverBorderWidth: 5,
        pointBackgroundColor: "#2D9CDB",
        fill: true, // 선 배경 채우기
        backgroundColor: gradientFill, // 배경 그라데이션
        borderWidth: 4,
        hoverOffset: 10,
      },
    ],
  };

  lineChartRef = new Chart(ctx, {
    type: "line",
    data: data,
    options: {
      interaction: {
        intersect: false,
      },
      maintainAspectRatio: false,
      plugins: {
        tooltip: {
          enabled: true, // 튤팁 활성화 (기본값 true)
          backgroundColor: "#ffffff", // 튤팁 색상
          padding: 10, // 튤팁 패딩
          titleColor: "#555555",
          titleAlign: "left",
          titleFont: {
            weight: "bold",
            size: 10,
            lineHeight: 1.5,
          },
          bodyColor: "#555555",
          bodyAlign: "center",
          bodyFont: {
            weight: "bold",
            size: 14,
            lineHeight: 1.5,
          },
          displayColors: false, // 색상 제거
        },
        datalabels: {
          formatter: function (value, context) {
            return "";
          },
        },
        legend: {
          display: false,
        },
      },
      scales: {
        y: {
          ticks: {
            color: "#A2A3A5",
            callback: (value, index) => {
              return "";
            },
          },
          grid: {
            color: "transparent",
          },
          border: {
            display: false,
          },
        },
        x: {
          ticks: {
            color: "#A2A3A5",
            callback: (value, index) => {
              return "정체 " + (value + 1);
            },
          },
          grid: {
            display: false,
          },
          border: {
            display: false,
          },
        },
      },
    },
  });
}
</script>

<template>
  <div :style="{ width: width, height: height }">
    <canvas ref="lineChart"> </canvas>
  </div>
</template>

<style scoped></style>
