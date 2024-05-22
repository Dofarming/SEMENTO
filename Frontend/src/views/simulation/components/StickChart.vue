<script setup>
import { defineProps, computed, onMounted } from "vue";
import moment from "moment";
import { Bar } from "vue-chartjs";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from "chart.js";
import ChartDeferred from "chartjs-plugin-deferred";

// 차트 구성 요소 등록
ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
);
ChartJS.register(ChartDeferred);

// Props 정의
const props = defineProps({
  workPerOne: {
    type: Array,
    default: () => [0],
  },
  workPerAll: {
    type: Array,
    default: () => [0],
  },
  labels: {
    type: Array,
    default: () => ["2024-01-01T01:00:00"], // 기본값을 날짜 형식으로 변경
  },
  ohtId: {
    type: Number,
    default: 0,
  },
});

// 날짜 변환 함수
function transformatDate(date) {
  return [moment(date).format("MM/DD"), moment(date).format("HH:mm:ss")];
}

// 변환된 labels를 계산
const formattedLabels = computed(() => {
  return props.labels.map((label) => {
    const [date, time] = transformatDate(label);
    return `${date} ${time}`;
  });
});

// 차트 데이터 설정
const chartData = computed(() => ({
  labels: formattedLabels.value,
  datasets: [
    {
      barPercentage: 0.5,
      label: `${props.ohtId} 작업량`,
      backgroundColor: "#003CB0",
      data: props.workPerOne,
      barThickness: 20, // 막대의 두께(px).
    },
    {
      barPercentage: 0.5,
      label: "전체 OHT 작업량 평균",
      backgroundColor: "#E8E8E8",
      data: props.workPerAll,
      barThickness: 20, // 막대의 두께(px).
    },
  ],
}));

// 차트 옵션 설정
const chartOptions = {
  borderRadius: 5,
  plugins: {
    legend: {
      display: false,
      align: "start",
    },
    tooltip: {
      enabled: true,
      backgroundColor: "#000",
      padding: 10,
    },
    datalabels: {
      display: false,
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
        display: true,
        stepSize: () => {
          const max1 = Math.max(...props.workPerOne);
          const max2 = Math.max(...props.workPerAll);
          const max = Math.max(max1, max2);
          // 대략 4단계로 나뉘면 좋을것 같으므로
          return max / 4;
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
      grid: {
        display: false,
      },
    },
  },
  maintainAspectRatio: false,
  responsive: true,
};

onMounted(() => {});
</script>

<template>
  <div class="container">
    <Bar :options="chartOptions" :data="chartData" />
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  height: 100%;
}
</style>
