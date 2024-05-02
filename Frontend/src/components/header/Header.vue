<script setup>
import { ref, watch } from "vue";
import { useRoute } from "vue-router";

// 페이지 제목 반응형 참조로 생성
const pageTitle = ref("Dashboard");
const currentDate = ref(new Date().toLocaleDateString());
const currentYear = ref(currentDate.value.slice(0, 4));
const currentMonth = ref(currentDate.value.slice(5, 7));
const currentDay = ref(currentDate.value.slice(8, 10));

// 현재 라우트 추적
const route = useRoute();

// route 객체의 변화를 감시하고, path에 따라 pageTitle 업데이트
watch(route, (currentRoute) => {
  // 라우트의 이름이 'dashboard'이면 pageTitle을 'dashboard'로 설정
  if (currentRoute.name === "dashboard") {
    pageTitle.value = "Dashboard";
  } else if (currentRoute.name === "analytics") {
    pageTitle.value = "Analytics";
  } else if (currentRoute.name === "simulation") {
    pageTitle.value = "Simulation";
  }
});

const monthNames = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];
currentMonth.value = monthNames[currentDate.value.slice(5, 7) - 1];

function updateDate() {
  currentDate.value = new Date().toLocaleDateString();
}

// 다음 자정까지의 시간을 계산
const now = new Date();
const nextMidnight = new Date(
  now.getFullYear(),
  now.getMonth(),
  now.getDate() + 1
);
const msUntilMidnight = nextMidnight - now;

// 다음 자정에 한 번 실행하고, 이후 매일 자정마다 반복
setTimeout(() => {
  updateDate(); // 자정에 처음으로 업데이트 실행
  setInterval(updateDate, 86400000); // 이후 매일 자정마다 업데이트
}, msUntilMidnight);
</script>

<template>
  <div class="header">
    <div class="left-side">
      <div class="page-title">{{ pageTitle }}</div>
      <div class="current-time">
        <font-awesome-icon
          :icon="['fas', 'angles-right']"
          size="xs"
          style="color: #9f9f9f"
        />
        <span class="time">
          {{ " " + currentMonth }} {{ currentDay }}, {{ currentYear }}</span
        >
      </div>
    </div>
    <div class="right-side">
      <font-awesome-icon :icon="['fas', 'bell']" class="notification-icon" />
      <div class="search-container">
        <input class="search-box" type="text" placeholder="Search here" />
        <div class="search-icon-container">
          <font-awesome-icon
            :icon="['fas', 'magnifying-glass']"
            class="search-icon"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #f3f4f6;
  border-bottom: 1px solid #e1e4e8;
}

.left-side,
.right-side {
  display: flex;
  align-items: center;
}

.page-title,
.current-time,
.notification-icon {
  margin-right: 20px;
}

.search-container {
  position: relative;
}

.search-box {
  padding: 12px 24px 12px 32px;
  border: 1px solid #ffffff;
  border-radius: 12px;
}

.time {
  color: #9f9f9f;
  font-size: 15px;
}

.page-title {
  font-weight: bold;
  font-size: 20px;
}

.right-side {
  display: flex;
  align-items: center;
}

.search-icon-container {
  position: absolute;
  right: 10px; /* 아이콘의 오른쪽 여백 설정 */
  top: 50%;
  transform: translateY(-50%);
}
</style>