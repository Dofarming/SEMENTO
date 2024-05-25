<script setup>
import { ref, onMounted, watchEffect, watch } from "vue";
import { Chart, registerables } from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";
import ChartDeferred from "chartjs-plugin-deferred";

import { useDashboardStore } from "@/stores/dashboard";
const dashboardStore = useDashboardStore();

const props = defineProps({
  width: {
    type: String,
    default: "100%",
  },
  height: {
    type: String,
    default: "300px",
  },
});

const labels = [
  "0h",
  "1h",
  "2h",
  "3h",
  "4h",
  "5h",
  "6h",
  "7h",
  "8h",
  "9h",
  "10h",
  "11h",
  "12h",
  "13h",
  "14h",
  "15h",
  "16h",
  "17h",
  "18h",
  "19h",
  "20h",
  "21h",
  "22h",
  "23h",
];
const working_ohts = ref([]);
const idle_ohts = [
  17, 23, 20, 22, 32, 30, 27, 29, 19, 25, 30, 18, 21, 22, 16, 21, 22, 26, 29,
  33, 34, 35, 36, 37,
];

Chart.register(...registerables);
Chart.register(ChartDataLabels);
Chart.register(ChartDeferred);

const lineChart = ref(null);

//update를 위한 변수
let lineChartRef = null;

onMounted(async () => {
  dataUpdate();
  drawLine();
});

watch(
  () => dashboardStore.watchedStateHourlyAnalysisData,
  (oldValue, newValue) => {
    dataUpdate();
    drawLine();
  },
  { deep: true }
);

function dataUpdate() {
  working_ohts.value = dashboardStore.stateHourlyAnalysisData[
    "work-hour-count"
  ].map((item) => item.count);
  idle_ohts.value = dashboardStore.stateHourlyAnalysisData[
    "idle-hour-count"
  ].map((item) => item.count);
}

function drawLine() {
  const ctx = lineChart.value.getContext("2d");
  let gradientFill = ctx.createLinearGradient(0, 0, 0, 200);
  gradientFill.addColorStop(0, "rgba(0, 7, 182, 0.1)");
  gradientFill.addColorStop(1, "rgba(0, 0, 0, 0)");

  const data = {
    labels: labels,

    datasets: [
      {
        label: "작업 중",
        data: working_ohts.value,
        borderColor: "#2E8FD6",
        tension: 0.5,
        pointStyle: "Rounded",
        pointRadius: 1,
        // pointBorderWidth: 0,
        pointHoverRadius: 5,
        pointHoverBorderWidth: 1,
        pointBackgroundColor: "#422F8A",
        fill: true, // 선 배경 채우기
        backgroundColor: gradientFill, // 배경 그라데이션
        borderWidth: 2.5,
        hoverOffset: 4,
      },
      {
        label: "유휴 상태",
        data: idle_ohts.value,
        fill: false,
        borderColor: "#ECE9F1",
        tension: 0.5,
        pointStyle: "Rounded",
        pointRadius: 1,
        pointBackgroundColor: "#555555",
        borderWidth: 2.5, // 선의 두께 설정
        hoverOffset: 4,
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
          labels: {
            usePointStyle: true,
            boxHeight: 6,
          },
        },
        deferred: {
          xOffset: 150, // defer until 150px of the canvas width are inside the viewport
          yOffset: "50%", // defer until 50% of the canvas height are inside the viewport
          delay: 500, // delay of 500 ms after the canvas is considered inside the viewport
        },
      },
      scales: {
        y: {
          ticks: {
            color: "#A2A3A5",
            // forces step size to be 50 units
            stepSize: 5,
            callback: (value, index) => {
              if (value == 0) return "";
              return value % 5 === 0 ? value : "";
            },
          },
          grid: {
            drawTicks: false,
            color: "#ECE9F1",
            lineWidth: 1,
          },
          border: {
            display: false,
          },
        },
        x: {
          ticks: {
            color: "#A2A3A5",
            callback: (value, index) => {
              return value + "h";
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
