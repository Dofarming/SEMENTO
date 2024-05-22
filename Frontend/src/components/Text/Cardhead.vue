<!-- 
** Cardhead 사용법 **

 <Cardhead 
 headerText = "카드제목"
 contentText="카드설명" />

-->
<script setup>
const props = defineProps({
  headerText: {
    type: String,
    default: "제목",
  },
  contentText: {
    type: String,
    default: "설명",
  },
});
</script>

<template>
  <section>
    <h1>{{ headerText }}</h1>
    <p>{{ contentText }}</p>
  </section>
</template>

<style scoped>
section {
  margin: 0px 30px;
  padding: 0px 0px;
}
h1 {
  font-weight: 700;
  color: #464255;
  font-size: 19px;
}
p {
  font-weight: 500;
  color: #b9bbbd;
  font-size: 15px;
}
</style>
