<!-- 
** SearchInput 사용법 **

 <SearchInput />

-->
<script setup>
import moment from "moment";
// import { useAnalysisStore } from "@/stores/analysis";
import { ref, watch } from "vue";

//== 초기값 변경
const props = defineProps({
  propsStartDate: {
    type: Date,
    default: null,
  },
  propsEndDate: {
    type: Date,
    default: null,
  },
});
//== 초기화코드
const startDate = ref(props.propsStartDate);
const endDate = ref(props.propsEndDate);

const emit = defineEmits(["update-start", "update-end"]);

watch(startDate, (newValue) => {
  emit("update-start", newValue);
});
watch(endDate, (newValue) => {
  emit("update-end", newValue);
});

function transformatDate(date) {
  return moment(date).format("YYYY.MM.DD HH:mm:ss");
}

//== 수혁코드
// const analysisStore = useAnalysisStore();
// function formatDateToISO(date) {
//   return moment(date).format("YYYY-MM-DDTHH:mm:ss");
// }
// // pinia의 startDate와 endDate 업데이트
// watch(startDate, (newDate) => {
//   analysisStore.startDate.value = formatDateToISO(newDate);
// });
// watch(endDate, (newDate) => {
//   analysisStore.endDate.value = formatDateToISO(newDate);
// });
</script>

<template>
  <section class="container">
    <!-- 시작 -->
    <VDatePicker v-model="startDate" mode="dateTime" is24hr>
      <template #default="{ togglePopover }">
        <button class="date-box" @click="togglePopover">
          {{ startDate == null ? "시작 시간" : transformatDate(startDate) }}
          <font-awesome-icon
            :icon="['fas', 'calendar']"
            size="xs"
            style="color: #667085"
          />
        </button>
      </template>
    </VDatePicker>
    <p>-</p>
    <!-- 끝 -->
    <VDatePicker v-model="endDate" mode="dateTime" is24hr>
      <template #default="{ togglePopover }">
        <button class="date-box" @click="togglePopover">
          {{ endDate == null ? "종료 시간" : transformatDate(endDate) }}
          <font-awesome-icon
            :icon="['fas', 'calendar']"
            size="xs"
            style="color: #667085"
          />
        </button>
      </template>
    </VDatePicker>
  </section>
</template>

<style scoped>
p {
  font-weight: bold;
  font-size: 15px;
}
.container {
  display: flex;
  gap: 10px;
  align-items: center;
}
.date-box {
  width: 200px;
  height: 44px;
  background-color: white;
  border-radius: 5px;
  border: 1px solid rgb(229, 229, 229);
  padding: 5px 13px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0px 5px 20px rgba(0, 0, 0, 0.05);
  font-size: 15px;
}

.date-box:hover {
  cursor: pointer;
}
</style>
