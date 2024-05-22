<script setup>
import { reactive, onMounted, ref, watch } from 'vue';
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
});

Chart.register(...registerables);
Chart.register(ChartDataLabels);
Chart.register(ChartDeferred);


const jobResult = ref([]); // Success, Fail 순 퍼센트
const jobResultCount = ref([]);
const chartRef = ref(null);
let chart = null;

const chartData = reactive({
  labels: [
    'Success',
    'Fail'
  ],
  datasets: [{
    data: jobResult.value,
    backgroundColor: [
      '#003CB0',
      '#9AC8FC'
    ],
    hoverOffset: 4,
    pointStyle: 'Rounded',
    pointRadius: 1,
  }]
});

const chartOptions = reactive({
    responsive: true,
    maintainAspectRatio: false,
    // rotation: 360 * (jobResult.value[1] / 100) + 45, // 파이차트 시작 위치 조정
    plugins: {
        tooltip: {
            backgroundColor: '#ffffff',
            xAlign: 'center',
            yAlign: 'center',
            titleColor: '#555555',
            titleAlign: 'center',
            titleFont: {
                weight: 'bold',
                size: 20,
                lineHeight: 1.5
            },
            bodyColor: '#555555',
            bodyAlign: 'center',
            bodyFont: {
                size: 15,
                lineHeight: 1.5
            },
            displayColors: false, // 색상 제거
            callbacks: {
                label: function(context) {
                    return jobResultCount.value[context.dataIndex] + '건';
                }
            }
        },
        datalabels: {
            formatter: function(value, context) {
                return value +' %';
            },
            color: '#ffffff'
        },
        legend: {
            labels: {
                usePointStyle: true,
                boxHeight: 6
            }
        },
        deferred: {
          xOffset: 150,   // defer until 150px of the canvas width are inside the viewport
          yOffset: '50%', // defer until 50% of the canvas height are inside the viewport
          delay: 500      // delay of 500 ms after the canvas is considered inside the viewport
        }
    }, 
});

function drawChart() {
    if (chart !== null) {
        chart.destroy();
    }
    const ctx = chartRef.value.getContext("2d");
    chart = new Chart(ctx, {
        type: "pie",
        data: chartData,
        options: chartOptions
    });
}

onMounted(async() => {
  dataUpdate();
  drawChart();
});


watch(() => dashboardStore.watchedJobResultAnalysisData, (oldValue, newValue) => {
  dataUpdate();
  drawChart();
},{ deep: true }); 

function dataUpdate() {
  let newData = []
  newData.push(dashboardStore.jobResultAnalysisData["job-result-ratio"]["success-percentage"].toFixed(2));
  newData.push(dashboardStore.jobResultAnalysisData["job-result-ratio"]["failed-percentage"].toFixed(2));
  jobResult.value = newData;
  chartData.datasets[0].data = [...jobResult.value];

  newData = []
  newData.push(dashboardStore.jobResultAnalysisData["job-result-ratio"]["success-work"]);
  newData.push(dashboardStore.jobResultAnalysisData["job-result-ratio"]["failed-work"]);
  jobResultCount.value = newData;
}


</script>

<template>
    <div :style="{width: width, height: height}">
      <canvas ref="chartRef"> </canvas>
        <!-- <Pie :data="chartData" :options="chartOptions" /> -->
    </div>
</template>

<style scoped></style>
