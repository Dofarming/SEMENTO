<!-- 
** Cardhead 사용법 **

 <HeadText 
 headerText = "제목"
 />

-->
<script setup>
const props = defineProps({
  headerText: {
    type: String,
    default: "제목",
  },
});
</script>

<template>
  <h1>{{ headerText }}</h1>
</template>

<style scoped>
h1 {
  font-weight: 700;
  color: #222222;
  font-size: 20px;
}
</style>
