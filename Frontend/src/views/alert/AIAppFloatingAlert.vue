<script setup>
import { storeToRefs } from "pinia";
import { useAISolutionNotificationStore } from "@/stores/ainotification";
const store = useAISolutionNotificationStore();
const { showAISolutionNotification, closeNotification } = storeToRefs(store);

const beforeEnter = (el) => {
  el.style.opacity = "0";
  el.style.transform = "translateX(100%)"; // 오른쪽 밖에서 시작
};

const enter = (el, done) => {
  el.offsetHeight; // 강제 리플로우를 유발하여 애니메이션 트리거
  el.style.transition = "opacity 0.5s, transform 0.5s";
  el.style.opacity = "1";
  el.style.transform = "translateX(0)"; // 원래 위치로 이동
  done();
};

const leave = (el, done) => {
  el.style.transition = "opacity 0.5s, transform 0.5s";
  el.style.opacity = "0";
  el.style.transform = "translateX(100%)"; // 오른쪽으로 사라짐
  done();
};
</script>

<template>
  <div>
    <transition
      name="notification-fade"
      appear
      @before-enter="beforeEnter"
      @enter="enter"
      @leave="leave"
    >
      <div
        key="notification"
        v-if="showAISolutionNotification"
        class="notification"
      >
        <!-- 상단 파란색 네모 -->
        <div class="notification-header">
          <span
            ><span
              ><font-awesome-icon
                :icon="['fas', 'square']"
                style="color: #3a57e8"
            /></span>
            &nbsp;<b>SEMENTO AI의 Solution이 도착하였습니다!</b></span
          >
          <span class="close-button" @click="store.closeAISolutionNotification">
            <span></span>
            <font-awesome-icon
              :icon="['fas', 'xmark']"
              size="xl"
              style="color: #4c5365"
          /></span>
        </div>
        <!-- 하단 내용 -->
        <div class="notification-content">
          <p>AI Solution 버튼을 통해 Solution 결과를 확인해보세요.</p>
        </div>
      </div>
    </transition>
    <!-- 나머지 내용 -->
  </div>
</template>

<style scoped>
.notification {
  position: fixed;
  right: 30px; /* 우측 끝에 위치 */
  bottom: 40px; /* 하단 끝에 위치 */
  background-color: #fff;
  /* border: 2px solid #ccc; */
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column; /* 상하로 배치 */
  width: 400px; /* 가로 길이 조절 */
  border-radius: 5px;
  overflow: hidden;
}

.notification-header {
  background-color: #ffffff;
  color: #505050; /* 글자색 */
  font-weight: bold;
  font-size: 0.9rem;
  padding: 6px 10px;
  display: flex;
  justify-content: space-between; /* 간격 벌리기 */
  margin: 5px;
}
b {
  font-weight: bold;
}
.close-button {
  cursor: pointer;
}

.notification-content {
  background-color: #c5c5c5; /* 진회색 배경색 */
  padding: 10px;
}

.notification-content p {
  margin: 5px;
  color: #111111; /* 글자색 */
  font-size: 0.9rem;
  font-weight: 500;
}

.time {
  color: #cdcdcd;
  font-weight: lighter;
  font-size: 0.9rem;
}
</style>
