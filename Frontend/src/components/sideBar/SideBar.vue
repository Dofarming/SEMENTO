<script setup>
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";

const activeItem = ref("");
const router = useRouter();
const route = useRoute();

function setActive(item, path) {
  activeItem.value = item;
  router.push(path);
}

onMounted(() => {
  // 현재 경로를 확인하여 activeItem을 설정
  const currentPath = route.path;
  if (currentPath.startsWith("/")) {
    activeItem.value = "dashboard";
  } else if (currentPath.startsWith("/analytics")) {
    activeItem.value = "analytics";
  } else if (currentPath.startsWith("/simulation")) {
    activeItem.value = "simulation-logs";
  }
});
</script>

<template>
  <div class="side-bar-container">
    <div class="logo-container">
      <div class="logo">Semento<span class="logo-dot">.</span></div>
      <div class="logo-subheading">OHT AI 분석 시스템</div>
    </div>
    <div class="nav-bar-container">
      <div
        class="dashboard-container"
        :class="{ active: activeItem === 'dashboard' }"
        @click="setActive('dashboard', '/dashboard')"
        :style="
          activeItem === 'dashboard' ? 'background-color: #0057ff6e;' : ''
        "
      >
        <font-awesome-icon :icon="['fas', 'house']" style="color: white" />
        <span
          class="dashboard-word"
          :style="activeItem === 'dashboard' ? 'font-weight: bold; ' : ''"
          >대시보드</span
        >
      </div>
      <div
        class="analytics-container"
        :class="{ active: activeItem === 'analytics' }"
        @click="setActive('analytics', '/analytics')"
        :style="
          activeItem === 'analytics' ? 'background-color: #0057ff6e;' : ''
        "
      >
        <font-awesome-icon
          :icon="['fas', 'chart-simple']"
          style="color: white"
        />
        <span
          class="analytics-word"
          :style="activeItem === 'analytics' ? 'font-weight: bold; ' : ''"
          >AI 분석</span
        >
      </div>
      <div
        class="simulation-log-container"
        :class="{ active: activeItem === 'simulation-logs' }"
        @click="setActive('simulation-logs', '/simulation')"
        :style="
          activeItem === 'simulation-logs' ? 'background-color: #0057ff6e;' : ''
        "
      >
        <font-awesome-icon
          :icon="['fas', 'square-poll-horizontal']"
          style="color: white"
        />
        <span
          class="simulation-log-word"
          :style="activeItem === 'simulation-logs' ? 'font-weight: bold; ' : ''"
          >시뮬레이션</span
        >
      </div>
    </div>
  </div>
</template>

<style scoped>
.logo-container {
  padding-left: 10%;
  padding-bottom: 15%;
}

.logo {
  color: white;
  font-size: 2rem;
  font-weight: bold;
}

.logo .logo-dot {
  color: #5932ea;
  font-size: 4rem;
}

.logo-subheading {
  padding-top: 3%;
  color: #d3d6d6;
  font-size: 20px;
}

.dashboard-container {
  padding: 4% 0 4% 15%;
  margin: 3% 0 0 3%;
  width: 90%;
  border-radius: 8px;
}

.dashboard-container :hover {
  cursor: pointer;
}

.dashboard-word {
  padding-left: 7.6%;
  color: white;
}

.analytics-container {
  padding: 6% 0 4% 15%;
  margin: 3% 0 0 3%;
  width: 90%;
  border-radius: 8px;
}

.analytics-container :hover {
  cursor: pointer;
}

.analytics-word {
  padding-left: 10%;
  color: white;
}

.simulation-log-container {
  padding: 6% 0 4% 15%;
  margin: 3% 0 0 3%;
  width: 90%;
  border-radius: 8px;
}

.simulation-log-container :hover {
  cursor: pointer;
}

.simulation-log-word {
  padding-left: 10%;
  color: white;
}
</style>
