import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { instance } from '@/util/axios-util'


export const useDashboardStore = defineStore('dashboard', () => {
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
    const {data} = await instance.post("/dashboard/oht-job-analysis", {"start-time":startTime, "end-time":endTime});
    ohtJobAnalysisData.value = data;
  }

  const getOhtJobHourly = async(startTime, endTime) => {
    const {data} = await instance.post("/dashboard/oht-job-hourly", {"start-time":startTime, "end-time":endTime});
    ohtJobHourlyData.value = data;
  }

  const getJobResultAnalysis = async(startTime, endTime) => {
    const {data} = await instance.post("/dashboard/job-result-analysis", {"start-time":startTime, "end-time":endTime});
    jobResultAnalysisData.value = data;
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
    const {data} = await instance.post("/dashboard/state-analysis", {"start-time":startTime, "end-time":endTime});
    stateAnalysisData.value = data;
  }


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
  }
})
