<script setup>
import { RouterLink, RouterView } from "vue-router";
import { ref } from "vue";
import Loading from "@/components/loading/Loading.vue";
import SideBar from "@/components/sideBar/SideBar.vue";
import Header from "@/components/header/Header.vue";
import AppFloatingAlert from "@/views/alert/AppFloatingAlert.vue";
const isAuthenticated = ref(false);
if (isAuthenticated.value == false) confirm();
function confirm() {
  const enteredPassword = prompt("시연을 위해 비밀번호를 입력받고 있습니다.");
  // 여기에서 실제 비밀번호 확인을 수행할 수 있습니다.
  if (enteredPassword === "응세멘토결선1등") {
    isAuthenticated.value = true;
  } else {
    alert("잘못된 비밀번호입니다.");
    confirm();
  }
}
</script>

<template>
  <div v-if="isAuthenticated" class="container">
    <SideBar id="side-bar" />
    <div class="content">
      <Header />
      <RouterView />
    </div>
    <AppFloatingAlert />
  </div>
</template>

<style scoped>
@import "@/assets/reset.css";

.container {
  width: 100%;
  height: 100%;
  background-color: #f3f2f7;
  overflow: scroll;
  overflow-x: hidden; /* 가로스크롤 숨기기 */
}

#side-bar {
  height: 100%;
  width: 17.5%;
  background-color: rgb(35, 35, 35);
  position: fixed;
  top: 0;
  left: 0;
  line-height: 1.2;
  z-index: 100;
}

.content {
  margin-left: 18%;
}
</style>

<style>
.footer {
  height: 200px;
}
.white-box {
  background-color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 5px;
  box-shadow: 0px 5px 20px rgba(0, 0, 0, 0.05);
  gap: 15px;
  padding: 20px 10px 10px 0px;
}

.title {
  width: 100%;
  display: flex;
  flex-direction: start;
}

/* 스크롤바 전체 너비 설정 */
::-webkit-scrollbar {
  width: 10px; /* 스크롤바의 너비 설정 */
  height: 10px;
}
/* 스크롤바 트랙 스타일링 */
::-webkit-scrollbar-track {
  background: #f6f6f6; /* 트랙의 배경 색상 */
}
/* 스크롤바 핸들(썸) 스타일링 */
::-webkit-scrollbar-thumb {
  background: #b9b9b9; /* 핸들의 배경 색상 */
  border-radius: 10px;
}
/* 스크롤바 핸들 호버 시 스타일링 */
::-webkit-scrollbar-thumb:hover {
  background: #555; /* 호버 시 핸들의 배경 색상 변경 */
}
</style>
