import { ref, computed } from "vue";
import { defineStore } from "pinia";
import { instance } from "@/util/axios-util";

export const useNotificationStore = defineStore("notificationStore", () => {
  //플로팅알림 변수
  const showNotification = ref(false);

  //플로팅 알림 띄우기
  const sendNotification = async () => {
    showNotification.value = true;
    // setTimeout(() => {
    //   closeNotification();
    // }, 5000); // 5초 후에 알림을 닫음 (조절 가능)
  };

  //플로팅 알림 닫기
  const closeNotification = () => {
    showNotification.value = false;
  };

  return {
    showNotification,
    sendNotification,
    closeNotification,
  };
});
