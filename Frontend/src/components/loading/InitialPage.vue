<script setup>
import Illustration from "@/assets/Illustration.svg";
</script>

<template>
  <div class="container">
    <img src="@/assets/test.gif" />
    <p>시간을 선택하면 SEMENTO가 분석결과를 제공해드려요.</p>
  </div>
</template>

<style scoped>
.container {
  margin-left: 20px;
  width: 97%;
  height: 70vh;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  /* border: 1px solid black; */
}

img {
  border-radius: 10px;
  width: 40%;
  clip-path: circle(40%); /* 원 모양으로 자름 */
}
p {
  font-weight: bold;
  font-size: 20px;
}
</style>
