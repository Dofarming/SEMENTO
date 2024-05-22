<!-- 
** Text 사용법 **

 <Text 
 text = "내용" 
 size = "폰트크기" />
 locaion = false
 number =1 />
-->
<script setup>
const props = defineProps({
  location: {
    type: Boolean,
    default: false,
  },
  number: {
    type: String,
    default: "",
  },
  text: {
    type: String,
    default: "내용",
  },
  size: {
    type: String,
    default: "15px",
  },
});
</script>

<template>
  <div class="flex">
    <p class="tag" v-if="location">
      <font-awesome-icon
        class="icon"
        :icon="['fas', 'location-dot']"
        size="lg"
        style="color: #003cb0"
      />[DETECTION {{ number }}]
    </p>
    <p :style="{ fontSize: size }" class="content">{{ text }}</p>
  </div>
</template>

<style scoped>
.flex {
  display: flex;
}
.icon {
  margin-right: 10px;
}
.tag {
  color: #003cb0;
  font-weight: bold;
}
.content {
  margin-left: 5px;
  color: black;
}
</style>
