<script setup>
import { ref } from "vue";

const isLoading = ref(true);
const fullPage = ref(true);

function onCancel() {
  console.log("Loading cancelled by user");
  isLoading.value = false;
}

const props = defineProps({
  title: {
    type: String,
    default: "로그를 분석중입니다",
  },
});
</script>

<template>
  <div class="loading-overlay">
    <div class="loading-content">
      <p>{{ props.title }}</p>
      <span class="loader"></span>
    </div>
  </div>
</template>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 17.5%; /* SideBar 너비만큼 왼쪽에서 시작 */
  width: 82.5%; /* 전체 너비에서 SideBar 너비를 제외한 나머지 */
  height: 100%;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease-out forwards; /* 애니메이션 적용 */
}

.loading-content {
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 40px;
}

.loading-content p {
  color: white;
  font-size: 3rem;
  font-weight: bold;
  margin-top: 20px;
}

.loader {
  width: 80px;
  height: 80px;
  border: 10px solid #fff;
  border-bottom-color: transparent;
  border-radius: 50%;
  display: inline-block;
  box-sizing: border-box;
  animation: rotation 1s linear infinite;
}

@keyframes rotation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
