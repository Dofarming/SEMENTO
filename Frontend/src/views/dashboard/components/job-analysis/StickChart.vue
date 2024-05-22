<script setup>
import { reactive, onMounted, ref, watchEffect, watch } from 'vue';
import { Chart, registerables } from 'chart.js';
import ChartDataLabels from 'chartjs-plugin-datalabels';
import ChartDeferred from 'chartjs-plugin-deferred';

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
  startTime: {
    type: String
  },
  endTime: {
    type: String
  }
});

// 시간
const labels = [
    "0h", "1h", "2h", "3h", "4h", "5h", 
    "6h", "7h", "8h", "9h", "10h", "11h", 
    "12h", '13h', '14h', '15h', '16h', '17h', 
    '18h', '19h', '20h', '21h', '22h', '23h'
];

// Chart.js 컴포넌트 등록
Chart.register(...registerables);
Chart.register(ChartDataLabels);
Chart.register(ChartDeferred);


const ohtJobHourlyCount = ref([]);
const chartRef = ref(null);
let chart = null;

// char 데이터
const chartData = reactive({
  labels,
  datasets: [
    {
      label: false,
      backgroundColor: [
          '#BCE0F2', '#BCE0F2','#BCE0F2', '#BCE0F2', '#BCE0F2','#BCE0F2',
          '#BCE0F2', '#BCE0F2','#BCE0F2', '#BCE0F2', '#BCE0F2','#BCE0F2',
          '#BCE0F2', '#BCE0F2','#BCE0F2', '#BCE0F2', '#BCE0F2','#BCE0F2',
          '#BCE0F2', '#BCE0F2','#BCE0F2', '#BCE0F2', '#BCE0F2',
          '#003CB0', // 마지막 막대만 색깔 다르게
      ],
      data: ohtJobHourlyCount.value,
      borderRadius: 5,
      barThickness: 20
    }
  ]
});

// chart 옵션
const chartOptions = reactive({
    layout: { 
        padding: 20
    },
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        tooltip: {
            displayColors: false, // 색상 제거
        },
        datalabels: { // 막대별 값 설정
            color: '#555555',
            font: {
                weight: 'bold',
                size: 13,
            },
            anchor: 'end',
            align: 'top',
            offset: -1,
            formatter: function(value, context) {
                if(value == 0) return "";
                return context.dataset.data[context.dataIndex];// 각 막대 위에 데이터 값을 표시
            }
        },
        legend: { // 범례 제거
            display: false
        },
        // deferred: {
        //   xOffset: 150,   // defer until 150px of the canvas width are inside the viewport
        //   yOffset: '50%', // defer until 50% of the canvas height are inside the viewport
        //   delay: 500      // delay of 500 ms after the canvas is considered inside the viewport
        // }
    },
    scales: {
        x: {
            grid: { // 그리드 제거
                color: 'transparent',
            },
            border: { // x축 제거
                display: false,
            }
        },
        y: {
            ticks: { // Y축 라벨 제거
                drawTicks: false,
                callback: (value, index) => {
                    return "";
                }
            },
            grid: { // 그리드 제거
                color: 'transparent',
            },
            border: { // y축 제거
                display: false,
            },
        }
    },
});

function drawChart() {
    const ctx = chartRef.value.getContext("2d");
    chart = new Chart(ctx, {
        type: "bar",
        data: chartData,
        options: chartOptions
    });
}

onMounted(async() => {
    ohtJobHourlyCount.value = dashboardStore.ohtJobHourlyData.map(item => item.work);
    chartData.datasets[0].data = [...ohtJobHourlyCount.value];
    drawChart();
});

</script>

<template>
    <div :style="{ width: width, height: height}">
        <canvas ref="chartRef"> </canvas>
    </div>
</template>

<style scoped>
</style>
