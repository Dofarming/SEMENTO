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

const data = [10, 30, 60];
const error = ref([]); // Success, Fail 순 퍼센트
const errorCount = ref([]);
const chartRef = ref(null);
let chart = null;

Chart.register(...registerables);
Chart.register(ChartDataLabels);
Chart.register(ChartDeferred);


const chartData = reactive({
  labels: ['OHT Error', 'Facility Error'],
  datasets: [
    {
      backgroundColor: ['#59A7FF', '#292D30'],
      data: error,
      borderWidth: 0
    }
  ]
}); 

const chartOptions = reactive({
  responsive: true,
  maintainAspectRatio: false,
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
                    return errorCount.value[context.dataIndex] + '건';
                }
            }
        },
        datalabels: {
            color: '#ffffff',
            formatter: function(value, context) {
                return value+'%';
            },
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
})

function drawChart() {
    if (chart !== null) {
        chart.destroy();
    }
    const ctx = chartRef.value.getContext("2d");
    chart = new Chart(ctx, {
        type: "doughnut",
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
  newData.push(dashboardStore.jobResultAnalysisData["job-result-error"]["oht-error-percentage"].toFixed(2));
  newData.push(dashboardStore.jobResultAnalysisData["job-result-error"]["facility-error-percentage"].toFixed(2));
  error.value = newData;
  chartData.datasets[0].data = [...error.value];

  newData = []
  newData.push(dashboardStore.jobResultAnalysisData["job-result-error"]["oht-error"]);
  newData.push(dashboardStore.jobResultAnalysisData["job-result-error"]["facility-error"]);
  errorCount.value = newData;
}

</script>

<template>
    <div :style="{width:width, height:height}">
      <canvas ref="chartRef"> </canvas>
        <!-- <Doughnut :data="chartData" :options="chartOptions" /> -->
    </div>
</template>

<style scoped></style>
