<script setup>
import { ref } from "vue";
const showInfo = ref(false);
</script>

<template>
  <div class="info-container">
    <div
      class="content"
      @click="showInfo = !showInfo"
      style="display: flex; align-items: center; cursor: pointer"
    >
      <Transition name="fade">
        <div class="info-text" v-if="showInfo" style="margin-right: 10px">
          숫자는 해당 구간의 정체 횟수를 나타냅니다.
        </div>
      </Transition>
      <font-awesome-icon
        :icon="['fas', 'circle-info']"
        class="info-icon"
        style="color: #717171; position: relative"
        size="2xl"
      />
    </div>
  </div>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 1s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
