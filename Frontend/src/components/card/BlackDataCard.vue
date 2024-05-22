<!-- 
** BlackDataCard 사용법 **

 <BlackDataCard
 title="Card 제목"
 content="value 값"
 percentage="증감율"
 fontColor="blue or red"
 height="높이값"
 width="너비값"
 />

-->
<script setup>
const props = defineProps({
  title: {
    type: String,
    default: "",
  },
  content: {
    type: String,
    default: "",
  },
  percentage: {
    type: String,
    default: "",
  },
  fontColor: {
    type: String,
    default: "blue",
  },
  height: {
    type: String,
    default: "auto",
  },
  width: {
    type: String,
    default: "377px",
  },
});

</script>

<template>
  <div
    class="card-container"
    :style="{
      width: width,
      height: height,
    }"
  >
    <div class="card-title">{{ title }}</div>
    <div class="card-content-container">
      <div class="card-content">{{ content }}</div>
      <div class="card-percentage" :style="{ color: fontColor }">
        {{ percentage }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.card-container {
  background-color: black;
  color: white;
  border-radius: 5px;
  padding: 25px 20px;
}

.card-title {
  font-weight: 400;
  margin-bottom: 10px; /* 제목 아래 여백 추가 */
  font-size: 1.1rem;
}

.card-content-container {
  padding: 10px 0;
  display: flex; /* 가로로 나열 */
  justify-content: space-between; /* 양쪽 정렬 */
  align-items: center; /* 수직 가운데 정렬 */
}

.card-content {
  color: #F3F3F3;
  font-size: 1.3rem;
  font-weight: bold;
}

.card-percentage {
  font-weight: bold;
  font-size: 1.3rem;
}
</style>
