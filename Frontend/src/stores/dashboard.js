import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { instance } from '@/util/axios-util'
//test-data import
import dashboardOhtJobAnalysisData from '/test_data/Dashboard/DashboardOhtJobAnalysis.json'
import dashboardOhtJobHourlyData from '/test_data/Dashboard/DashboardOhtJobHourly.json'
import dashboardJobResultAnalysisData from '/test_data/Dashboard/DashboardJobResultAnalysis.json'
import dashboardStateAnalysisData from '/test_data/Dashboard/DashboardStateAnalysis.json'
import dashboardStateHourlyAnalysisData from '/test_data/Dashboard/DashboardStateHourlyAnalysis.json'


export const useDashboardStore = defineStore('dashboard', () => {

  const startTime = ref("");
  const endTime = ref("");
  const ohtJobAnalysisData = ref({
      "oht-count": {
          "data": "loading..",
          "percent": 0
      },
      "total-work": {
          "data": "loading..",
          "percent": 0
      },
      "average-work": {
          "data": "loading..",
          "percent": 0
      }
  })

  const ohtJobHourlyData = ref([]) 

  const jobResultAnalysisData = ref({})

  const getOhtJobAnalysis = async(startTime, endTime) => {
    // const {data} = await instance.post("/dashboard/oht-job-analysis", {"start-time":startTime, "end-time":endTime});
    // ohtJobAnalysisData.value = data;
    ohtJobAnalysisData.value = dashboardOhtJobAnalysisData
  }

  const getOhtJobHourly = async(startTime, endTime) => {
    // const {data} = await instance.post("/dashboard/oht-job-hourly", {"start-time":startTime, "end-time":endTime});
    // ohtJobHourlyData.value = data;
    ohtJobHourlyData.value = dashboardOhtJobHourlyData
  }

  const getJobResultAnalysis = async(startTime, endTime) => {
    // const {data} = await instance.post("/dashboard/job-result-analysis", {"start-time":startTime, "end-time":endTime});
    // jobResultAnalysisData.value = data;
    jobResultAnalysisData.value = dashboardJobResultAnalysisData
  }

  const watchedJobResultAnalysisData = computed(() => jobResultAnalysisData.value)

  const stateAnalysisData = ref({
    "deadline": {
        "data": 0,
        "percent": 0.0
    },
    "average-work-time": {
        "data": 0,
        "percent": 0.0
    },
    "average-idle-time": {
        "data": 0,
        "percent": 0.0
    }
});

  const getStateAnalysis = async(startTime, endTime) => {
    // const {data} = await instance.post("/dashboard/state-analysis", {"start-time":startTime, "end-time":endTime});
    // stateAnalysisData.value = data;
    stateAnalysisData.value = dashboardStateAnalysisData
  }

  const stateHourlyAnalysisData = ref({});

  const getStateHourlyAnalysis = async(startTime, endTime) => {
    // const {data} = await instance.post("/dashboard/state-hourly-analysis", {"start-time":startTime, "end-time":endTime});
    // stateHourlyAnalysisData.value = data;
    stateHourlyAnalysisData.value = dashboardStateHourlyAnalysisData
  }

  const watchedStateHourlyAnalysisData = computed(() => stateHourlyAnalysisData.value)


  return { 
    ohtJobAnalysisData, 
    getOhtJobAnalysis,
    ohtJobHourlyData,
    getOhtJobHourly, 
    jobResultAnalysisData,
    getJobResultAnalysis,
    watchedJobResultAnalysisData,
    stateAnalysisData,
    getStateAnalysis,
    stateHourlyAnalysisData,
    getStateHourlyAnalysis,
    watchedStateHourlyAnalysisData,
    startTime,
    endTime
  }
})
