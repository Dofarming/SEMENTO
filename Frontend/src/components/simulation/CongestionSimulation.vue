<script setup>
import { ref, onMounted, watch } from "vue";
import * as d3 from "d3";
import { simulationComponentStore } from "@/stores/simulationComponent";
const { getCongestionSimulation } = simulationComponentStore();

const props = defineProps({
  errorData: {
    type: Object,
    default: [],
  },
  number: {
    type: Number,
    default: 0,
  },

});

const timeOrder = ref(0);
const ohtLogs = ref({
  "simulation-log": [
    {
      time: "0000-00-00T00:00:00",
      data: [
        {
          "oht-id": 2586,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 128.08,
            "point-y": 451.96,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2587,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 103.988,
            "point-y": 373.837,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2588,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 74.601,
            "point-y": 361.889,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2589,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 72.977,
            "point-y": 397.697,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2590,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 164.901,
            "point-y": 455.516,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2591,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 146.53,
            "point-y": 361.485,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2592,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 125.712,
            "point-y": 441.864,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2593,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 92.834,
            "point-y": 421.956,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2594,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 113.684,
            "point-y": 423.616,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2595,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 80.53,
            "point-y": 406.078,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2596,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 124.813,
            "point-y": 419.596,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2597,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 188.738,
            "point-y": 406.428,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2598,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 161.378,
            "point-y": 366.059,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2599,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 183.06,
            "point-y": 417.662,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2600,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 125.507,
            "point-y": 437.985,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2601,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 169.553,
            "point-y": 387.306,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2602,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 174.441,
            "point-y": 445.753,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2603,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 67.067,
            "point-y": 415.047,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2604,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 73.438,
            "point-y": 415.316,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2605,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 106.169,
            "point-y": 413.25,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2606,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 150.538,
            "point-y": 376.329,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2607,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 172.346,
            "point-y": 362.028,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2608,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 138.067,
            "point-y": 392.523,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2609,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 158.06,
            "point-y": 446.283,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2610,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 88.453,
            "point-y": 431.255,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2611,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 174.27,
            "point-y": 433.468,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2612,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 163.823,
            "point-y": 398.983,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2613,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 87.83,
            "point-y": 440.167,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2614,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 186.766,
            "point-y": 459.99,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2615,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 148.092,
            "point-y": 386.196,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
      ],
    },
    {
      time: "0000-00-00T00:00:01",
      data: [
        {
          "oht-id": 2586,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 128.08,
            "point-y": 451.96,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2587,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 103.988,
            "point-y": 373.837,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2588,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 74.601,
            "point-y": 361.889,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2589,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 72.977,
            "point-y": 397.697,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2590,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 164.901,
            "point-y": 455.516,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2591,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 146.53,
            "point-y": 361.485,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2592,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 125.712,
            "point-y": 441.864,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2593,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 92.834,
            "point-y": 421.956,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2594,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 113.684,
            "point-y": 423.616,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2595,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 80.53,
            "point-y": 406.078,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2596,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 124.813,
            "point-y": 419.596,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2597,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 188.738,
            "point-y": 406.428,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2598,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 161.378,
            "point-y": 366.059,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2599,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 183.06,
            "point-y": 417.662,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2600,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 125.507,
            "point-y": 437.985,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2601,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 169.553,
            "point-y": 387.306,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2602,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 174.441,
            "point-y": 445.753,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2603,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 67.067,
            "point-y": 415.047,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2604,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 73.438,
            "point-y": 415.316,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2605,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 106.169,
            "point-y": 413.25,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2606,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 150.538,
            "point-y": 376.329,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2607,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 172.346,
            "point-y": 362.028,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2608,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 138.067,
            "point-y": 392.523,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2609,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 158.06,
            "point-y": 446.283,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2610,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 88.453,
            "point-y": 431.255,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2611,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 174.27,
            "point-y": 433.468,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2612,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 163.823,
            "point-y": 398.983,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2613,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 87.83,
            "point-y": 440.167,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2614,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 186.766,
            "point-y": 459.99,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
        {
          "oht-id": 2615,
          location: {
            path: null,
            "curr-node": null,
            "point-x": 148.092,
            "point-y": 386.196,
          },
          status: "I",
          carrier: false,
          error: 0,
          speed: 0.0,
          fail: false,
        },
      ],
    },
  ],
});

// 임의의 노드와 링크 생성
const nodes = [
  // Top row
  { id: "10", x: 380.0, y: 360.0 },
  { id: "8", x: 365.0, y: 330.0 },
  { id: "7", x: 350.0, y: 320.0 },
  { id: "6", x: 320.0, y: 320.0 },
  { id: "5", x: 290.0, y: 320.0 },
  { id: "4", x: 275.0, y: 330.0 },
  { id: "2", x: 260.0, y: 360.0 },
  { id: "1", x: 240.0, y: 360.0 },
  { id: "tn", x: 185.6594299542066, y: 360.0 },
  { id: "22", x: 565.0, y: 360.0 },
  { id: "23", x: 595.0, y: 360.5 },
  { id: "44", x: 930.0, y: 360.0 },
  { id: "45", x: 960.0, y: 360.0 },
  { id: "66", x: 1295.0, y: 360.0 },
  { id: "67", x: 1325.0, y: 360.0 },
  { id: "88", x: 1660.0, y: 360.0 },
  { id: "89", x: 1690.0, y: 360.0 },
  { id: "110", x: 2025.0, y: 360.0 },
  { id: "111", x: 2040.0, y: 370.0 },
  { id: "112", x: 2040.0, y: 390.0 },
  { id: "113", x: 2025.0, y: 400.0 },
  { id: "115", x: 1890.0, y: 400.0 },
  { id: "118", x: 1825.0, y: 400.0 },
  { id: "120", x: 1690.0, y: 400.0 },
  { id: "121", x: 1660.0, y: 400.0 },
  { id: "123", x: 1525.0, y: 400.0 },
  { id: "126", x: 1460.0, y: 400.0 },
  { id: "128", x: 1325.0, y: 400.0 },
  { id: "129", x: 1295.0, y: 400.0 },
  { id: "132", x: 1160.0, y: 400.0 },
  { id: "135", x: 1095.0, y: 400.0 },
  { id: "138", x: 960.0, y: 400.0 },
  { id: "139", x: 930.0, y: 400.0 },
  { id: "141", x: 795.0, y: 400.0 },
  { id: "144", x: 745.0, y: 400.0 },
  { id: "146", x: 595.0, y: 400.5 },
  { id: "147", x: 565.0, y: 400.0 },
  { id: "149", x: 430.0, y: 400.0 },
  { id: "152", x: 360.0, y: 400.0 },
  { id: "154", x: 240.0, y: 400.0 },
  { id: "155", x: 225.0, y: 390.0 },
  { id: "304", x: 225.0, y: 490.0 },
  { id: "156", x: 240.0, y: 480.0 },
  { id: "158", x: 360.0, y: 480.0 },
  { id: "159", x: 375.0, y: 470.0 },
  { id: "151", x: 375.0, y: 410.0 },
  { id: "150", x: 415.0, y: 410.0 },
  { id: "160", x: 415.0, y: 470.0 },
  { id: "161", x: 430.0, y: 480.0 },
  { id: "3", x: 275.0, y: 350.0 },
  { id: "9", x: 365.0, y: 350.0 },
  { id: "163", x: 565.0, y: 480.0 },
  { id: "164", x: 595.0, y: 480.0 },
  { id: "166", x: 745.0, y: 480.0 },
  { id: "167", x: 760.0, y: 470.0 },
  { id: "143", x: 760.0, y: 410.0 },
  { id: "142", x: 780.0, y: 410.0 },
  { id: "168", x: 780.0, y: 470.0 },
  { id: "169", x: 795.0, y: 480.0 },
  { id: "171", x: 930.0, y: 480.0 },
  { id: "172", x: 960.0, y: 480.0 },
  { id: "174", x: 1095.0, y: 480.0 },
  { id: "175", x: 1110.0, y: 470.0 },
  { id: "134", x: 1110.0, y: 410.0 },
  { id: "133", x: 1145.0, y: 410.0 },
  { id: "176", x: 1145.0, y: 470.0 },
  { id: "177", x: 1160.0, y: 480.0 },
  { id: "179", x: 1295.0, y: 480.0 },
  { id: "180", x: 1325.0, y: 480.0 },
  { id: "182", x: 1460.0, y: 480.0 },
  { id: "183", x: 1475.0, y: 470.0 },
  { id: "125", x: 1475.0, y: 410.0 },
  { id: "124", x: 1510.0, y: 410.0 },
  { id: "184", x: 1510.0, y: 470.0 },
  { id: "185", x: 1525.0, y: 480.0 },
  { id: "187", x: 1660.0, y: 480.0 },
  { id: "188", x: 1690.0, y: 480.0 },
  { id: "190", x: 1825.0, y: 480.0 },
  { id: "191", x: 1840.0, y: 470.0 },
  { id: "117", x: 1840.0, y: 410.0 },
  { id: "116", x: 1875.0, y: 410.0 },
  { id: "192", x: 1875.0, y: 470.0 },
  { id: "193", x: 1890.0, y: 480.0 },
  { id: "195", x: 2025.0, y: 480.0 },
  { id: "196", x: 2040.0, y: 490.0 },
  { id: "197", x: 2040.0, y: 510.0 },
  { id: "198", x: 2025.0, y: 520.0 },
  { id: "12", x: 415.0, y: 360.0 },
  { id: "20", x: 535.0, y: 360.0 },
  { id: "19", x: 520.0, y: 350.0 },
  { id: "18", x: 520.0, y: 330.0 },
  { id: "17", x: 505.0, y: 320.0 },
  { id: "16", x: 475.0, y: 320.0 },
  { id: "15", x: 445.0, y: 320.0 },
  { id: "14", x: 430.0, y: 330.0 },
  { id: "13", x: 430.0, y: 350.0 },
  { id: "24", x: 625.0, y: 360.0 },
  { id: "32", x: 745.0, y: 360.0 },
  { id: "34", x: 780.0, y: 360.0 },
  { id: "42", x: 900.0, y: 360.0 },
  { id: "41", x: 885.0, y: 350.0 },
  { id: "40", x: 885.0, y: 330.0 },
  { id: "39", x: 870.0, y: 320.0 },
  { id: "38", x: 840.0, y: 320.0 },
  { id: "37", x: 810.0, y: 320.0 },
  { id: "36", x: 795.0, y: 330.0 },
  { id: "35", x: 795.0, y: 350.0 },
  { id: "31", x: 730.0, y: 350.0 },
  { id: "30", x: 730.0, y: 330.0 },
  { id: "29", x: 715.0, y: 320.0 },
  { id: "28", x: 685.0, y: 320.0 },
  { id: "27", x: 655.0, y: 320.0 },
  { id: "26", x: 640.0, y: 330.0 },
  { id: "25", x: 640.0, y: 350.0 },
  { id: "46", x: 990.0, y: 360.0 },
  { id: "54", x: 1110.0, y: 360.0 },
  { id: "56", x: 1145.0, y: 360.0 },
  { id: "63", x: 1250.0, y: 350.0 },
  { id: "62", x: 1250.0, y: 330.0 },
  { id: "61", x: 1235.0, y: 320.0 },
  { id: "60", x: 1205.0, y: 320.0 },
  { id: "59", x: 1175.0, y: 320.0 },
  { id: "58", x: 1160.0, y: 330.0 },
  { id: "57", x: 1160.0, y: 350.0 },
  { id: "53", x: 1095.0, y: 350.0 },
  { id: "52", x: 1095.0, y: 330.0 },
  { id: "51", x: 1080.0, y: 320.0 },
  { id: "50", x: 1050.0, y: 320.0 },
  { id: "49", x: 1020.0, y: 320.0 },
  { id: "48", x: 1005.0, y: 330.0 },
  { id: "47", x: 1005.0, y: 350.0 },
  { id: "64", x: 1265.0, y: 360.0 },
  { id: "68", x: 1355.0, y: 360.0 },
  { id: "76", x: 1475.0, y: 360.0 },
  { id: "78", x: 1510.0, y: 360.0 },
  { id: "86", x: 1630.0, y: 360.0 },
  { id: "85", x: 1615.0, y: 350.0 },
  { id: "84", x: 1615.0, y: 330.0 },
  { id: "83", x: 1600.0, y: 320.0 },
  { id: "82", x: 1570.0, y: 320.0 },
  { id: "81", x: 1540.0, y: 320.0 },
  { id: "80", x: 1525.0, y: 330.0 },
  { id: "79", x: 1525.0, y: 350.0 },
  { id: "75", x: 1460.0, y: 350.0 },
  { id: "74", x: 1460.0, y: 330.0 },
  { id: "73", x: 1445.0, y: 320.0 },
  { id: "72", x: 1415.0, y: 320.0 },
  { id: "71", x: 1385.0, y: 320.0 },
  { id: "70", x: 1370.0, y: 330.0 },
  { id: "69", x: 1370.0, y: 350.0 },
  { id: "90", x: 1720.0, y: 360.0 },
  { id: "98", x: 1840.0, y: 360.0 },
  { id: "100", x: 1875.0, y: 360.0 },
  { id: "108", x: 1995.0, y: 360.0 },
  { id: "107", x: 1980.0, y: 350.0 },
  { id: "106", x: 1980.0, y: 330.0 },
  { id: "105", x: 1965.0, y: 320.0 },
  { id: "104", x: 1935.0, y: 320.0 },
  { id: "103", x: 1905.0, y: 320.0 },
  { id: "102", x: 1890.0, y: 330.0 },
  { id: "101", x: 1890.0, y: 350.0 },
  { id: "97", x: 1825.0, y: 350.0 },
  { id: "96", x: 1825.0, y: 330.0 },
  { id: "95", x: 1810.0, y: 320.0 },
  { id: "94", x: 1780.0, y: 320.0 },
  { id: "93", x: 1750.0, y: 320.0 },
  { id: "92", x: 1735.0, y: 330.0 },
  { id: "91", x: 1735.0, y: 350.0 },
  { id: "199", x: 1995.0, y: 520.0 },
  { id: "200", x: 1980.0, y: 530.0 },
  { id: "201", x: 1980.0, y: 550.0 },
  { id: "202", x: 1965.0, y: 560.0 },
  { id: "203", x: 1935.0, y: 560.0 },
  { id: "204", x: 1905.0, y: 560.0 },
  { id: "205", x: 1890.0, y: 550.0 },
  { id: "206", x: 1890.0, y: 530.0 },
  { id: "207", x: 1875.0, y: 520.0 },
  { id: "209", x: 1840.0, y: 520.0 },
  { id: "210", x: 1825.0, y: 530.0 },
  { id: "211", x: 1825.0, y: 550.0 },
  { id: "212", x: 1810.0, y: 560.0 },
  { id: "213", x: 1780.0, y: 560.0 },
  { id: "214", x: 1750.0, y: 560.0 },
  { id: "215", x: 1735.0, y: 550.0 },
  { id: "216", x: 1735.0, y: 530.0 },
  { id: "217", x: 1720.0, y: 520.0 },
  { id: "219", x: 1690.0, y: 520.0 },
  { id: "220", x: 1660.0, y: 520.0 },
  { id: "221", x: 1630.0, y: 520.0 },
  { id: "222", x: 1615.0, y: 530.0 },
  { id: "223", x: 1615.0, y: 550.0 },
  { id: "224", x: 1600.0, y: 560.0 },
  { id: "225", x: 1570.0, y: 560.0 },
  { id: "226", x: 1540.0, y: 560.0 },
  { id: "227", x: 1525.0, y: 550.0 },
  { id: "228", x: 1525.0, y: 530.0 },
  { id: "229", x: 1510.0, y: 520.0 },
  { id: "231", x: 1475.0, y: 520.0 },
  { id: "232", x: 1460.0, y: 530.0 },
  { id: "233", x: 1460.0, y: 550.0 },
  { id: "234", x: 1445.0, y: 560.0 },
  { id: "235", x: 1415.0, y: 560.0 },
  { id: "236", x: 1385.0, y: 560.0 },
  { id: "237", x: 1370.0, y: 550.0 },
  { id: "238", x: 1370.0, y: 530.0 },
  { id: "239", x: 1355.0, y: 520.0 },
  { id: "241", x: 1325.0, y: 520.0 },
  { id: "242", x: 1295.0, y: 520.0 },
  { id: "243", x: 1265.0, y: 520.0 },
  { id: "244", x: 1250.0, y: 530.0 },
  { id: "245", x: 1250.0, y: 550.0 },
  { id: "246", x: 1235.0, y: 560.0 },
  { id: "247", x: 1205.0, y: 560.0 },
  { id: "248", x: 1175.0, y: 560.0 },
  { id: "249", x: 1160.0, y: 550.0 },
  { id: "250", x: 1160.0, y: 530.0 },
  { id: "251", x: 1145.0, y: 520.0 },
  { id: "252", x: 1110.0, y: 520.0 },
  { id: "253", x: 1095.0, y: 530.0 },
  { id: "254", x: 1095.0, y: 550.0 },
  { id: "255", x: 1080.0, y: 560.0 },
  { id: "256", x: 1050.0, y: 560.0 },
  { id: "257", x: 1020.0, y: 560.0 },
  { id: "258", x: 1005.0, y: 550.0 },
  { id: "259", x: 1005.0, y: 530.0 },
  { id: "260", x: 990.0, y: 520.0 },
  { id: "261", x: 960.0, y: 520.0 },
  { id: "262", x: 930.0, y: 520.0 },
  { id: "263", x: 900.0, y: 520.0 },
  { id: "264", x: 885.0, y: 530.0 },
  { id: "265", x: 885.0, y: 550.0 },
  { id: "266", x: 870.0, y: 560.0 },
  { id: "267", x: 840.0, y: 560.0 },
  { id: "268", x: 810.0, y: 560.0 },
  { id: "269", x: 795.0, y: 550.0 },
  { id: "270", x: 795.0, y: 530.0 },
  { id: "271", x: 780.0, y: 520.0 },
  { id: "272", x: 745.0, y: 520.0 },
  { id: "273", x: 730.0, y: 530.0 },
  { id: "274", x: 730.0, y: 550.0 },
  { id: "275", x: 715.0, y: 560.0 },
  { id: "276", x: 685.0, y: 560.0 },
  { id: "277", x: 655.0, y: 560.0 },
  { id: "278", x: 640.0, y: 550.0 },
  { id: "279", x: 640.0, y: 530.0 },
  { id: "280", x: 625.0, y: 520.0 },
  { id: "282", x: 595.0, y: 520.0 },
  { id: "283", x: 565.0, y: 520.0 },
  { id: "284", x: 540.0, y: 520.0 },
  { id: "285", x: 525.0, y: 530.0 },
  { id: "286", x: 525.0, y: 550.0 },
  { id: "287", x: 510.0, y: 560.0 },
  { id: "288", x: 480.0, y: 560.0 },
  { id: "289", x: 450.0, y: 560.0 },
  { id: "290", x: 435.0, y: 550.0 },
  { id: "291", x: 435.0, y: 530.0 },
  { id: "292", x: 420.0, y: 520.0 },
  { id: "293", x: 385.0, y: 520.0 },
  { id: "294", x: 370.0, y: 530.0 },
  { id: "295", x: 370.0, y: 550.0 },
  { id: "296", x: 355.0, y: 560.0 },
  { id: "297", x: 325.0, y: 560.0 },
  { id: "298", x: 295.0, y: 560.0 },
  { id: "299", x: 280.0, y: 550.0 },
  { id: "300", x: 280.0, y: 530.0 },
  { id: "301", x: 265.0, y: 520.0 },
  { id: "302", x: 240.0, y: 520.0 },
  { id: "303", x: 225.0, y: 510.0 },
  { id: "11", x: 320.0, y: 360.0 },
  { id: "153", x: 285.0, y: 400.0 },
  { id: "157", x: 300.0, y: 480.0 },
  { id: "311", x: 325.0, y: 520.0 },
  { id: "305", x: 225.0, y: 435.0 },
  { id: "162", x: 495.0, y: 480.0 },
  { id: "148", x: 495.0, y: 400.0 },
  { id: "21", x: 475.0, y: 360.0 },
  { id: "33", x: 685.0, y: 360.0 },
  { id: "43", x: 840.0, y: 360.0 },
  { id: "55", x: 1050.0, y: 360.0 },
  { id: "65", x: 1220.0, y: 360.0 },
  { id: "77", x: 1415.0, y: 360.0 },
  { id: "87", x: 1570.0, y: 360.0 },
  { id: "99", x: 1780.0, y: 360.0 },
  { id: "109", x: 1935.0, y: 360.0 },
  { id: "114", x: 1970.0, y: 400.0 },
  { id: "194", x: 1955.0, y: 480.0 },
  { id: "208", x: 1920.0, y: 520.0 },
  { id: "306", x: 2040.0, y: 440.0 },
  { id: "218", x: 1780.0, y: 520.0 },
  { id: "119", x: 1740.0, y: 400.0 },
  { id: "189", x: 1775.0, y: 480.0 },
  { id: "186", x: 1590.0, y: 480.0 },
  { id: "122", x: 1605.0, y: 400.0 },
  { id: "230", x: 1555.0, y: 520.0 },
  { id: "240", x: 1415.0, y: 520.0 },
  { id: "127", x: 1390.0, y: 400.0 },
  { id: "181", x: 1390.0, y: 480.0 },
  { id: "307", x: 1205.0, y: 520.0 },
  { id: "130", x: 1245.0, y: 400.0 },
  { id: "178", x: 1230.0, y: 480.0 },
  { id: "308", x: 1050.0, y: 520.0 },
  { id: "137", x: 1010.0, y: 400.0 },
  { id: "173", x: 1025.0, y: 480.0 },
  { id: "309", x: 840.0, y: 520.0 },
  { id: "170", x: 865.0, y: 480.0 },
  { id: "140", x: 880.0, y: 400.0 },
  { id: "310", x: 480.0, y: 520.0 },
  { id: "281", x: 700.0, y: 520.0 },
  { id: "145", x: 695.0, y: 400.0 },
  { id: "165", x: 675.0, y: 480.0 },
  { id: "136", x: 1055.0, y: 400.0 },
  { id: "131", x: 1205.0, y: 400.0 },
];

const links = [
  { source: "20", target: "22" },
  { source: "42", target: "44" },
  { source: "64", target: "66" },
  { source: "86", target: "88" },
  { source: "10", target: "12" },
  { source: "8", target: "9" },
  { source: "7", target: "8" },
  { source: "6", target: "7" },
  { source: "5", target: "6" },
  { source: "4", target: "5" },
  { source: "2", target: "3" },
  { source: "1", target: "2" },
  { source: "tn", target: "1" },
  { source: "2", target: "11" },
  { source: "22", target: "23" },
  { source: "23", target: "24" },
  { source: "44", target: "45" },
  { source: "45", target: "46" },
  { source: "66", target: "67" },
  { source: "67", target: "68" },
  { source: "88", target: "89" },
  { source: "89", target: "90" },
  { source: "108", target: "110" },
  { source: "110", target: "111" },
  { source: "111", target: "112" },
  { source: "112", target: "113" },
  { source: "113", target: "114" },
  { source: "115", target: "118" },
  { source: "118", target: "119" },
  { source: "120", target: "121" },
  { source: "121", target: "122" },
  { source: "123", target: "126" },
  { source: "126", target: "127" },
  { source: "128", target: "129" },
  { source: "129", target: "130" },
  { source: "132", target: "135" },
  { source: "135", target: "136" },
  { source: "138", target: "139" },
  { source: "139", target: "140" },
  { source: "141", target: "144" },
  { source: "144", target: "145" },
  { source: "146", target: "147" },
  { source: "147", target: "148" },
  { source: "149", target: "152" },
  { source: "152", target: "153" },
  { source: "154", target: "155" },
  { source: "304", target: "305" },
  { source: "304", target: "156" },
  { source: "156", target: "157" },
  { source: "158", target: "159" },
  { source: "159", target: "151" },
  { source: "151", target: "152" },
  { source: "149", target: "150" },
  { source: "150", target: "160" },
  { source: "160", target: "161" },
  { source: "158", target: "161" },
  { source: "3", target: "4" },
  { source: "9", target: "10" },
  { source: "161", target: "162" },
  { source: "163", target: "164" },
  { source: "164", target: "165" },
  { source: "166", target: "167" },
  { source: "167", target: "143" },
  { source: "143", target: "144" },
  { source: "141", target: "142" },
  { source: "142", target: "168" },
  { source: "168", target: "169" },
  { source: "166", target: "169" },
  { source: "169", target: "170" },
  { source: "171", target: "172" },
  { source: "172", target: "173" },
  { source: "174", target: "175" },
  { source: "175", target: "134" },
  { source: "134", target: "135" },
  { source: "132", target: "133" },
  { source: "133", target: "176" },
  { source: "176", target: "177" },
  { source: "174", target: "177" },
  { source: "177", target: "178" },
  { source: "179", target: "180" },
  { source: "180", target: "181" },
  { source: "182", target: "183" },
  { source: "183", target: "125" },
  { source: "125", target: "126" },
  { source: "123", target: "124" },
  { source: "124", target: "184" },
  { source: "184", target: "185" },
  { source: "182", target: "185" },
  { source: "185", target: "186" },
  { source: "187", target: "188" },
  { source: "188", target: "189" },
  { source: "190", target: "191" },
  { source: "191", target: "117" },
  { source: "117", target: "118" },
  { source: "115", target: "116" },
  { source: "116", target: "192" },
  { source: "192", target: "193" },
  { source: "190", target: "193" },
  { source: "193", target: "194" },
  { source: "195", target: "196" },
  { source: "196", target: "197" },
  { source: "197", target: "198" },
  { source: "12", target: "21" },
  { source: "19", target: "20" },
  { source: "18", target: "19" },
  { source: "17", target: "18" },
  { source: "16", target: "17" },
  { source: "15", target: "16" },
  { source: "14", target: "15" },
  { source: "13", target: "14" },
  { source: "12", target: "13" },
  { source: "24", target: "33" },
  { source: "32", target: "34" },
  { source: "34", target: "43" },
  { source: "41", target: "42" },
  { source: "40", target: "41" },
  { source: "39", target: "40" },
  { source: "38", target: "39" },
  { source: "37", target: "38" },
  { source: "36", target: "37" },
  { source: "35", target: "36" },
  { source: "34", target: "35" },
  { source: "31", target: "32" },
  { source: "30", target: "31" },
  { source: "29", target: "30" },
  { source: "28", target: "29" },
  { source: "27", target: "28" },
  { source: "26", target: "27" },
  { source: "25", target: "26" },
  { source: "24", target: "25" },
  { source: "46", target: "55" },
  { source: "54", target: "56" },
  { source: "56", target: "65" },
  { source: "63", target: "64" },
  { source: "62", target: "63" },
  { source: "61", target: "62" },
  { source: "60", target: "61" },
  { source: "59", target: "60" },
  { source: "58", target: "59" },
  { source: "57", target: "58" },
  { source: "56", target: "57" },
  { source: "53", target: "54" },
  { source: "52", target: "53" },
  { source: "51", target: "52" },
  { source: "50", target: "51" },
  { source: "49", target: "50" },
  { source: "48", target: "49" },
  { source: "47", target: "48" },
  { source: "46", target: "47" },
  { source: "68", target: "77" },
  { source: "76", target: "78" },
  { source: "78", target: "87" },
  { source: "85", target: "86" },
  { source: "84", target: "85" },
  { source: "83", target: "84" },
  { source: "82", target: "83" },
  { source: "81", target: "82" },
  { source: "80", target: "81" },
  { source: "79", target: "80" },
  { source: "78", target: "79" },
  { source: "75", target: "76" },
  { source: "74", target: "75" },
  { source: "73", target: "74" },
  { source: "72", target: "73" },
  { source: "71", target: "72" },
  { source: "70", target: "71" },
  { source: "69", target: "70" },
  { source: "68", target: "69" },
  { source: "90", target: "99" },
  { source: "98", target: "100" },
  { source: "100", target: "109" },
  { source: "107", target: "108" },
  { source: "106", target: "107" },
  { source: "105", target: "106" },
  { source: "104", target: "105" },
  { source: "103", target: "104" },
  { source: "102", target: "103" },
  { source: "101", target: "102" },
  { source: "100", target: "101" },
  { source: "97", target: "98" },
  { source: "96", target: "97" },
  { source: "95", target: "96" },
  { source: "94", target: "95" },
  { source: "93", target: "94" },
  { source: "92", target: "93" },
  { source: "91", target: "92" },
  { source: "90", target: "91" },
  { source: "112", target: "306" },
  { source: "198", target: "199" },
  { source: "199", target: "200" },
  { source: "200", target: "201" },
  { source: "201", target: "202" },
  { source: "202", target: "203" },
  { source: "203", target: "204" },
  { source: "204", target: "205" },
  { source: "205", target: "206" },
  { source: "206", target: "207" },
  { source: "199", target: "208" },
  { source: "207", target: "209" },
  { source: "209", target: "210" },
  { source: "210", target: "211" },
  { source: "211", target: "212" },
  { source: "212", target: "213" },
  { source: "213", target: "214" },
  { source: "214", target: "215" },
  { source: "215", target: "216" },
  { source: "216", target: "217" },
  { source: "209", target: "218" },
  { source: "217", target: "219" },
  { source: "219", target: "220" },
  { source: "220", target: "221" },
  { source: "221", target: "222" },
  { source: "222", target: "223" },
  { source: "223", target: "224" },
  { source: "224", target: "225" },
  { source: "225", target: "226" },
  { source: "226", target: "227" },
  { source: "227", target: "228" },
  { source: "228", target: "229" },
  { source: "221", target: "230" },
  { source: "229", target: "231" },
  { source: "231", target: "232" },
  { source: "232", target: "233" },
  { source: "233", target: "234" },
  { source: "234", target: "235" },
  { source: "235", target: "236" },
  { source: "236", target: "237" },
  { source: "237", target: "238" },
  { source: "238", target: "239" },
  { source: "231", target: "240" },
  { source: "239", target: "241" },
  { source: "241", target: "242" },
  { source: "242", target: "243" },
  { source: "243", target: "244" },
  { source: "244", target: "245" },
  { source: "245", target: "246" },
  { source: "246", target: "247" },
  { source: "247", target: "248" },
  { source: "248", target: "249" },
  { source: "249", target: "250" },
  { source: "250", target: "251" },
  { source: "243", target: "307" },
  { source: "251", target: "252" },
  { source: "252", target: "253" },
  { source: "253", target: "254" },
  { source: "254", target: "255" },
  { source: "255", target: "256" },
  { source: "256", target: "257" },
  { source: "257", target: "258" },
  { source: "258", target: "259" },
  { source: "259", target: "260" },
  { source: "252", target: "308" },
  { source: "260", target: "261" },
  { source: "261", target: "262" },
  { source: "262", target: "263" },
  { source: "263", target: "264" },
  { source: "264", target: "265" },
  { source: "265", target: "266" },
  { source: "266", target: "267" },
  { source: "267", target: "268" },
  { source: "268", target: "269" },
  { source: "269", target: "270" },
  { source: "270", target: "271" },
  { source: "263", target: "309" },
  { source: "271", target: "272" },
  { source: "272", target: "273" },
  { source: "273", target: "274" },
  { source: "274", target: "275" },
  { source: "275", target: "276" },
  { source: "276", target: "277" },
  { source: "277", target: "278" },
  { source: "278", target: "279" },
  { source: "279", target: "280" },
  { source: "272", target: "281" },
  { source: "280", target: "282" },
  { source: "282", target: "283" },
  { source: "283", target: "284" },
  { source: "284", target: "285" },
  { source: "285", target: "286" },
  { source: "286", target: "287" },
  { source: "287", target: "288" },
  { source: "288", target: "289" },
  { source: "289", target: "290" },
  { source: "290", target: "291" },
  { source: "291", target: "292" },
  { source: "284", target: "310" },
  { source: "292", target: "293" },
  { source: "293", target: "294" },
  { source: "294", target: "295" },
  { source: "295", target: "296" },
  { source: "296", target: "297" },
  { source: "297", target: "298" },
  { source: "298", target: "299" },
  { source: "299", target: "300" },
  { source: "300", target: "301" },
  { source: "293", target: "311" },
  { source: "301", target: "302" },
  { source: "302", target: "303" },
  { source: "303", target: "304" },
  { source: "11", target: "10" },
  { source: "153", target: "154" },
  { source: "157", target: "158" },
  { source: "311", target: "301" },
  { source: "305", target: "155" },
  { source: "162", target: "163" },
  { source: "148", target: "149" },
  { source: "21", target: "20" },
  { source: "33", target: "32" },
  { source: "43", target: "42" },
  { source: "55", target: "54" },
  { source: "65", target: "64" },
  { source: "77", target: "76" },
  { source: "87", target: "86" },
  { source: "99", target: "98" },
  { source: "109", target: "108" },
  { source: "114", target: "115" },
  { source: "194", target: "195" },
  { source: "208", target: "207" },
  { source: "306", target: "196" },
  { source: "218", target: "217" },
  { source: "119", target: "120" },
  { source: "189", target: "190" },
  { source: "186", target: "187" },
  { source: "122", target: "123" },
  { source: "230", target: "229" },
  { source: "240", target: "239" },
  { source: "127", target: "128" },
  { source: "181", target: "182" },
  { source: "307", target: "251" },
  { source: "130", target: "131" },
  { source: "178", target: "179" },
  { source: "308", target: "260" },
  { source: "137", target: "138" },
  { source: "173", target: "174" },
  { source: "309", target: "271" },
  { source: "170", target: "171" },
  { source: "140", target: "141" },
  { source: "310", target: "292" },
  { source: "281", target: "280" },
  { source: "145", target: "146" },
  { source: "165", target: "166" },
  { source: "136", target: "137" },
  { source: "131", target: "132" },
  { source: "155", target: "1" },
];

const curvedLinks = [
  // { source: "A", target: "B" },
  //왼쪽 커브
  { source: "22", target: "147" },
  { source: "44", target: "139" },
  { source: "66", target: "129" },
  { source: "88", target: "121" },
  { source: "163", target: "283" },
  { source: "171", target: "262" },
  { source: "179", target: "242" },
  { source: "187", target: "220" },

  //오른쪽 커브
  { source: "146", target: "23" },
  { source: "138", target: "45" },
  { source: "120", target: "89" },
  { source: "128", target: "67" },
  { source: "219", target: "188" },
  { source: "241", target: "180" },
  { source: "261", target: "172" },
  { source: "282", target: "164" },
];

//로그

const currentTimeText = ref("2024.01.08 13:38:23");

const formatTime = (time) => {
  // 날짜와 시간을 추출
  const datePart = time.slice(0, 10).split("-");
  const timePart = time.slice(11, 19).split(":");

  // 날짜 부분을 원하는 형식으로 조합
  const formattedDate = `${datePart[0]}.${datePart[1]}.${datePart[2]}`;

  // 시간 부분을 원하는 형식으로 조합
  const formattedTime = `${timePart[0]}:${timePart[1]}:${timePart[2]}`;

  // 최종적으로 날짜와 시간을 조합하여 원하는 형식으로 출력
  return `${formattedDate} ${formattedTime}`;
};

currentTimeText.value = formatTime(ohtLogs.value["simulation-log"][0]["time"]);

//컬러설정
const pathColor = "#B4B4B4";
const facilityColor = "#292D30";
const ohtColor = "orange";

const container = ref(null);
const svgContainer = ref(null);

let scale = 1.0;
let translateX = 0;
let translateY = 0;
let isDragging = false;
let dragStartX = 0;
let dragStartY = 0;
const idList = [
  "6",
  "16",
  "28",
  "38",
  "50",
  "60",
  "72",
  "82",
  "94",
  "104",
  "21",
  "43",
  "65",
  "77",
  "87",
  "99",
  "114",
  "119",
  "127",
  "131",
  "136",
  "140",
  "145",
  "153",
  "157",
  "162",
  "165",
  "170",
  "178",
  "186",
  "189",
  "194",
  "208",
  "230",
  "240",
  "308",
  "281",
  "311",
  "297",
  "288",
  "276",
  "267",
  "256",
  "247",
  "235",
  "225",
  "213",
  "203",
];
const updateTransform = () => {
  //svgContainer.value.style.transition = "transform 0.3s ease-in-out"; // 확대 및 축소 시에만 transition 추가
  svgContainer.value.style.transform = `scale(${scale}) translate(${translateX}px, ${translateY}px)`;
};

const handleWheel = (event) => {
  const delta = event.deltaY > 0 ? -0.1 : 0.1;
  scale += delta;
  scale = Math.min(Math.max(0.8, scale), 3);
  updateTransform();
};

const startDrag = (event) => {
  isDragging = true;
  dragStartX = event.clientX;
  dragStartY = event.clientY;
};

const drag = (event) => {
  if (!isDragging) return;
  const deltaX = event.clientX - dragStartX;
  const deltaY = event.clientY - dragStartY;
  translateX += deltaX;
  translateY += deltaY;
  dragStartX = event.clientX;
  dragStartY = event.clientY;
  updateTransform();
};

const endDrag = () => {
  isDragging = false;
};
const parentElement = ref(null);

// timeOrder의 변화를 감지하여 새로운 시뮬레이션 데이터를 가져오고 movePoint 실행
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
let svg = null;
let path = null;

let width = 120;
let height = 25;
let padding = 43;

// 최대 및 최소 좌표 계산
const xExtent = d3.extent(nodes, (d) => d.x);
const yExtent = d3.extent(nodes, (d) => d.y);

// 스케일 함수 설정
let xScale = d3
  .scaleLinear()
  .domain([xExtent[0], xExtent[1]])
  .range([padding, width - padding]);

let yScale = d3
  .scaleLinear()
  .domain([yExtent[0], yExtent[1]])
  .range([padding, height - padding]);

// 점 생성 및 이동 경로 설정
const ohts = [];
const ohtTexts = [];
const length = 13; //oht를 표시하는 사각형의 길이

function drawSimulation(width, height) {
  
  const tempSvg = d3
    .select(svgContainer.value)
    .append("svg")
    .attr("width", width)
    .attr("height", height);

  let initialTransform = "";

  // ohtLogs가 있을 경우에만(조회한 시간대에 시뮬레이션 로그가 있는 경우에만) 초기 포커싱 위치 설정
  if (ohtLogs.value["simulation-log"].length !== 0) {
    const causeOhtId = props.errorData["cause-oht"];
    const causeOht = ohtLogs.value["simulation-log"][0]["data"].find(
      (log) => log["oht-id"] === causeOhtId
    );

    if (causeOht) {
      const initialX = causeOht["location"]["point-x"];
      const initialY = causeOht["location"]["point-y"];
      // initialTransform = `translate(${width / 2 - xScale(initialX)}, ${height / 2 - yScale(initialY)})`;
      // translate와 scale을 함께 사용하여 초기 위치와 확대 설정
      const scale = 2; // 2배 확대 예시
      initialTransform = `translate(${width / 2 - xScale(initialX) * scale}, ${height / 2 - yScale(initialY) * scale}) scale(${scale})`;
    }
  }

  const svg = tempSvg.append("g")
    .attr("transform", initialTransform);

  // 노드와 링크 그리기
  svg
    .selectAll(".link")
    .data(links)
    .enter()
    .append("line")
    .attr("class", "link")
    .attr("x1", (d) => xScale(nodes.find((node) => node.id === d.source).x))
    .attr("y1", (d) => yScale(nodes.find((node) => node.id === d.source).y))
    .attr("x2", (d) => xScale(nodes.find((node) => node.id === d.target).x))
    .attr("y2", (d) => yScale(nodes.find((node) => node.id === d.target).y))
    .attr("stroke", pathColor);

  svg
    .selectAll(".curvedLink")
    .data(curvedLinks)
    .enter()
    .append("path")
    .attr("class", "curvedLink")
    .attr("d", function (d, i) {
      const start = nodes.find((node) => node.id === d.source);
      const end = nodes.find((node) => node.id === d.target);
      const midIndex = curvedLinks.length / 2;
      const curveX = 8;
      const curveY = 4;

      const sx = xScale(start.x), // 시작점 x 좌표
        sy = yScale(start.y), // 시작점 y 좌표
        ex = xScale(end.x), // 종료점 x 좌표
        ey = yScale(end.y); // 종료점 y 좌표

      // 제어점 두 개를 두고(직선 사이에 점 두 개를 두고) 제어점 기준으로 곡선 생선
      let cx, cy1, cy2;
      if (i < midIndex) {
        cx = sx + curveX;
        cy1 = (sy + ey) / 2 - curveY;
        cy2 = (sy + ey) / 2 + curveY;
      } else {
        cx = sx - curveX;
        cy1 = (sy + ey) / 2 - curveY + 8;
        cy2 = (sy + ey) / 2 + curveY - 8;
      }

      return `M ${sx} ${sy} C ${cx} ${cy1}, ${cx} ${cy2}, ${ex} ${ey}`;
    })
    .attr("stroke", pathColor)
    .attr("fill", "none");

  const nodeGroup = svg
    .selectAll(".node")
    .data(nodes)
    .enter()
    .append("g")
    .attr("class", "")
    // .attr("transform", (d) => `translate(${d.x},${d.y})`);
    .attr("transform", (d) => `translate(${xScale(d.x)},${yScale(d.y)})`);

  nodeGroup
    .append("circle")
    .attr("r", 5)
    // .attr("fill", "steelblue")
    .attr("fill", (d) => (idList.includes(d.id) ? facilityColor : pathColor));

  nodeGroup
    .append("text")
    .attr("text-anchor", "middle")
    .attr("dy", ".35em")
    .attr("font-size", "0.33rem")
    .attr("fill", "white")
    .text((d) => d.id);


  //ohtLogs가 있을 경우에만(조회한 시간대에 시뮬레이션 로그가 있는 경우에만) oht 그려주기
  if (ohtLogs.value["simulation-log"].length !== 0) {
    const excludeIds = [2600, 2604, 2607, 2593, 2587, 2586];
    // for문을 사용하여 30개의 원을 생성하고 ohts 배열에 저장합니다.
    for (
      let i = 0;
      i < ohtLogs.value["simulation-log"][0]["data"].length;
      i++
    ) {
      // if (excludeIds.includes(ohtLogs.value["simulation-log"][0]["data"][i]["oht-id"])) {
      //   continue;
      // }
      const point = svg
        .append("rect")
        .attr("width", length)
        .attr("height", length)
        .attr(
          "x",
          xScale(
            ohtLogs.value["simulation-log"][0]["data"][i]["location"][
              "point-x"
            ] -
              length / 2 -
              4
          )
        )
        .attr(
          "y",
          yScale(
            ohtLogs.value["simulation-log"][0]["data"][i]["location"][
              "point-y"
            ] -
              length / 2 -
              1
          )
        )
        .attr("rx", 3) // 모서리 둥근 처리를 위한 x축 반경
        .attr("fill", excludeIds.includes(ohtLogs.value["simulation-log"][0]["data"][i]["oht-id"]) ? "white" : ohtColor)
        .text(ohtLogs.value["simulation-log"][0]["data"][i]["oht-id"]);
        
      const ohtId = svg
        .append("text")
        .attr(
          "x",
          xScale(
            ohtLogs.value["simulation-log"][0]["data"][i]["location"]["point-x"]
          )
        )
        .attr(
          "y",
          yScale(
            ohtLogs.value["simulation-log"][0]["data"][i]["location"][
              "point-y"
            ] -
              length / 2
          )
        )
        .attr("rx", 3) // 모서리 둥근 처리를 위한 x축 반경
        .attr("fill", excludeIds.includes(ohtLogs.value["simulation-log"][0]["data"][i]["oht-id"]) ? "white" : ohtColor)
        .attr("text-anchor", "middle")
        .attr("font-size", 7)
        .text(ohtLogs.value["simulation-log"][0]["data"][i]["oht-id"]);

      if (
        ohtLogs.value["simulation-log"][0]["data"][i]["oht-id"] ===
        props.errorData["cause-oht"]
      ) {
        point.attr("stroke", "#AD29B6").attr("stroke-width", 2);
        ohtId.attr("fill", "#AD29B6").attr("font-weight", 10);
      }

      // 생성된 원을 배열에 추가합니다.
      ohts.push(point);
      ohtTexts.push(ohtId);
    }
  }

  path = svg.append("path").attr("fill", "none");
}

//다중 simulation 구현

let nowTime = 0;

function movePoint(currentTime) {
  const excludeIds = [2600, 2604, 2607, 2593, 2587, 2586];
  ohts.forEach((point, idx) => {
    const currentOht =
      ohtLogs.value["simulation-log"][currentTime]["data"][idx];
    const nextOht =
      ohtLogs.value["simulation-log"][currentTime + 1]["data"][idx];

    ohtTexts[idx]
      .transition()
      .duration(1000) //1초의 시간을 할당
      .attr("x", xScale(currentOht["location"]["point-x"]) - 1) //현재ohtx좌표 설정
      .attr("y", yScale(currentOht["location"]["point-y"] - length / 2 - 2)) //현재ohty좌표 설정
      .on("end", () => {
        //애니메이션 종료시 실행할작업
        ohtTexts[idx].attr(
          "d", //새로운 경로를 정의한다.
          `M${xScale(currentOht["location"]["point-x"])},${yScale(
            currentOht["location"]["point-y"]
          )},
          ${xScale(nextOht["location"]["point-x"])},${yScale(
            nextOht["location"]["point-y"]
          )}`
        );
      });

    if (currentOht["status"] === "W") {
      ohtTexts[idx].attr("fill", "#F27A16");
      point.attr("fill", "#F27A16");
    } else {
      if(props.number == 1) {
        ohtTexts[idx].attr("fill", excludeIds.includes(currentOht["oht-id"]) ? "white" : ohtColor)
        point.attr("fill", excludeIds.includes(currentOht["oht-id"]) ? "#FFFFFF" : ohtColor)  
      } else {
        ohtTexts[idx].attr("fill", ohtColor)
        point.attr("fill", ohtColor)
      }
    }

    if (currentOht["status"] !== "W" && currentOht["error"] === 0) {
      if(props.number == 1) {
        ohtTexts[idx].attr("fill", excludeIds.includes(currentOht["oht-id"]) ? "white" : ohtColor)
        point.attr("fill", excludeIds.includes(currentOht["oht-id"]) ? "#FFFFFF" : ohtColor)  
      } else {
        ohtTexts[idx].attr("fill", ohtColor)
        point.attr("fill", ohtColor)
      }
    } else if (currentOht["status"] !== "W" && currentOht["error"] === 200) {
      ohtTexts[idx].attr("fill", "red");
      point.attr("fill", "red");
    } else if (currentOht["error"] === 300) {
      ohtTexts[idx].attr("fill", "blue");
      point.attr("fill", "blue");
    }

    if (currentOht["oht-id"] === props.errorData["cause-oht"]) {
      ohtTexts[idx].attr("fill", "#AD29B6");
    }

    point
      .transition()
      .duration(1000) //1초의 시간을 할당
      .attr("x", xScale(currentOht["location"]["point-x"] - length / 2 - 4)) //현재ohtx좌표 설정
      .attr("y", yScale(currentOht["location"]["point-y"] - length / 2 - 1)) //현재ohty좌표 설정
      .on("end", async () => {
        //애니메이션 종료시 실행할작업
        path.attr(
          "d", //새로운 경로를 정의한다.
          `M${xScale(currentOht["location"]["point-x"])},${yScale(
            currentOht["location"]["point-y"]
          )},
          ${xScale(nextOht["location"]["point-x"])},${yScale(
            nextOht["location"]["point-y"]
          )}`
        );

        if (idx == ohtLogs.value["simulation-log"][0]["data"].length - 1) {
          //해당 시간초 점을 모두 이동하면 다음 시간초 이동으로 넘어가기
          // currentTime = currentTime + 1; //현재인덱스를 다음으로 변경
          if (currentTime + 2 >= ohtLogs.value["simulation-log"].length) {
            //현재 시간 시뮬레이션이 끝나면 다음 시뮬레이션 api 호출해야함. 다음초 + 인덱스 = +2
            nowTime = 0;
            timeOrder.value += 1;
            return;
          }
          currentTimeText.value = formatTime(
            ohtLogs.value["simulation-log"][currentTime + 1]["time"]
          );
          

          const text = currentTimeText.value
          // 시작이 특정 날짜로 시작하는지 확인
          let targetDate = "2024.04.30";
          let newDate = "2024.05.20";

          // 텍스트가 targetDate로 시작하는 경우
          if (text.startsWith(targetDate)) {
              // targetDate를 newDate로 변경
              currentTimeText.value = newDate + text.slice(targetDate.length);
          }
          movePoint(currentTime + 1);
        }
      });
  });
}

watch(timeOrder, async () => {
  currentTimeText.value = formatTime(
    ohtLogs.value["simulation-log"][0]["time"]
  );

  movePoint(nowTime);
});

onMounted(async () => {
  const start = new Date(props.errorData["start-date"]); //에러 시작 시간을 마지막 시간으로 설정해야함
  const end = new Date(props.errorData["end-date"]);
  start.setSeconds(start.getSeconds() - 30);
  start.setHours(start.getHours() + 9);
  end.setHours(end.getHours() + 9);

  ohtLogs.value = await getCongestionSimulation(
    start.toISOString().slice(0, -5),
    end.toISOString().slice(0, -5),
    []
    // [props.errorData["cause-oht"]]
  );

  parentElement.value = document.querySelector(".white-box");

  // 부모 요소의 가로와 세로 크기를 가져옵니다.
  const parentWidth = parentElement.value.clientWidth;
  const parentHeight = parentElement.value.clientHeight;
  width = parentWidth;
  height = parentHeight * 0.7;
  padding = 43;

  xScale = d3
    .scaleLinear()
    .domain([xExtent[0], xExtent[1]])
    .range([padding, width - padding]);

  yScale = d3
    .scaleLinear()
    .domain([yExtent[0], yExtent[1]])
    .range([padding, height - padding]);

  drawSimulation(width, height);
  movePoint(nowTime);
});
</script>

<template>
  <div
    class="container"
    ref="container"
    @wheel.prevent="handleWheel"
    @wheel="handleWheel"
    @mousedown.prevent="startDrag"
    @mousemove.prevent="drag"
    @mouseup.prevent="endDrag"
  >
    <div class="timer">{{ currentTimeText }}</div>
    <div ref="svgContainer"></div>
  </div>
</template>

<style scoped>
/* 시뮬레이션 컨테이너 */
.container {
  overflow: hidden;
  border: 1px dashed rgb(207, 207, 207);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
}

.timer {
  position: absolute;
  top: 0px;
  right: 0px;
  z-index: 10;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  font-weight: 600;
  font-size: x-large;
  padding: 2px 10px;
  border-radius: 5px;
}
</style>
