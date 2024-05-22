import { createRouter, createWebHistory } from "vue-router";
import DashBoard from "@/views/dashboard/DashBoardView.vue";
import Analytics from "@/views/analytics/AnalyticsView.vue";
import Simulation from "@/views/simulation/SimulationView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/dashboard",
    },
    {
      path: "/dashboard",
      name: "dashboard",
      component: DashBoard,
    },
    {
      path: "/analytics",
      name: "analytics",
      component: Analytics,
    },
    {
      path: "/simulation",
      name: "simulation",
      component: Simulation,
    },
  ],
});

export default router;
