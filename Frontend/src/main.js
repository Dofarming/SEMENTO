import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";

//Vue Select
import Multiselect from "vue-multiselect";

// VCalendar import
import VCalendar from "v-calendar";
import "v-calendar/style.css";

// fontawesome import
import { library } from "@fortawesome/fontawesome-svg-core";
import { fas } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
library.add(fas);

const app = createApp(App);

//라이브러리 등록
app.component("vue-multiselect", Multiselect);
app.component("font-awesome-icon", FontAwesomeIcon);
app.use(VCalendar, {});

//모듈 등록
app.use(createPinia());
app.use(router);

//연결
app.mount("#app");
