import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useAISolutionNotificationStore = defineStore(
  "AISolutionnotificationStore",
  () => {
    //플로팅알림 변수
    const showAISolutionNotification = ref(false);

    const sendAISolutionNotification = async () => {
      showAISolutionNotification.value = true;
    };

    const closeAISolutionNotification = () => {
      showAISolutionNotification.value = false;
    };

    return {
      showAISolutionNotification,
      sendAISolutionNotification,
      closeAISolutionNotification,
    };
  }
);
