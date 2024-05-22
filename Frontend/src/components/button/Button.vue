<!-- 
** Button 사용법 **

 <Button
 title="버튼제목" 
 backgroundColor="black"
 fontColor="white"/>

-->
<script setup>
const props = defineProps({
  title: {
    type: String,
    default: "Button Title",
  },
  backgroundColor: {
    type: String,
    default: "black",
  },
  fontColor: {
    type: String,
    default: "white",
  },
  width: {
    type: String,
    default: "",
  },
});
</script>

<template>
  <button
    :style="{
      border: backgroundColor,
      backgroundColor: backgroundColor,
      color: fontColor,
      width: width,
    }"
    class="button"
  >
    {{ title }}
  </button>
</template>

<style scoped>
button {
  height: 44px;
  padding: 0px 9px;
  border-radius: 5px;
  font-weight: bold;
  font-size: 15px;
}

.button:hover {
  cursor: pointer;
}
</style>
