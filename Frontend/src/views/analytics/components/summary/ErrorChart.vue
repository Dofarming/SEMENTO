<script setup>
import { hierarchy, pack } from "d3-hierarchy";
import { useAnalysisStore } from "@/stores/analysis";
import { ref, computed } from "vue";

const analysisStore = useAnalysisStore();

const errorCounts = computed(() => {
  const errorType = {
    "Facility Error": 0,
    "OHT Error": 0,
    "ETC Error": 0,
  };

  if (analysisStore.computedDetectionResult.length > 0) {
    analysisStore.computedDetectionResult.forEach((result) => {
      if (result.cause === "F") {
        errorType["Facility Error"]++;
      } else if (result.cause === "O") {
        errorType["OHT Error"]++;
      } else if (result.cause === "E") {
        errorType["ETC Error"]++;
      }
    });
  }

  return errorType;
});

const errorData = computed(() => {
  return [
    {
      name: "설비 에러",
      amount: errorCounts.value["Facility Error"],
      size: errorCounts.value["Facility Error"] + 2,
      color: "#292D30",
    },
    {
      name: "OHT 에러",
      amount: errorCounts.value["OHT Error"],
      size: errorCounts.value["OHT Error"] + 2,
      color: "#34B3F1",
    },
    {
      name: "명령 쏠림",
      amount: errorCounts.value["ETC Error"],
      size: errorCounts.value["ETC Error"] + 2,
      color: "#BCE0F2",
    },
  ];
  // .filter((error) => error.amount > 0);
});

// const transformedErrorData = {
//   name: "Top Level",
//   children: errorData.map((error) => ({
//     ...error,
//     size: error.amount,
//     parent: "Top Level",
//   })),
// };

const transformedErrorData = computed(() => ({
  name: "Top Level",
  children: errorData.value.map((error) => ({
    ...error,
    size: error.size,
    parent: "Top Level",
  })),
}));

// Generate a D3 hierarchy
// const rootHierarchy = hierarchy(transformedErrorData)
//   .sum((d) => d.size)
//   .sort((a, b) => b.value - a.value);
const rootHierarchy = computed(() =>
  hierarchy(transformedErrorData.value)
    .sum((d) => d.size)
    .sort((a, b) => b.value - a.value)
);

// Pack the circles inside the viewbox
// const layoutData = pack().size([300, 350]).padding(-10)(rootHierarchy);
const layoutData = computed(() =>
  pack().size([280, 330]).padding(-20)(rootHierarchy.value)
);
</script>

<template>
  <div>
    <svg width="330" height="500">
      <g transform="translate(25, 0)">
        <g
          class="error"
          v-for="error in layoutData.children"
          :key="error.data.name"
          :style="{
            transform: `translate(${error.x}px, ${error.y}px)`,
          }"
        >
          <circle
            class="error__circle"
            :r="error.r"
            :fill="error.data.color"
          ></circle>
          <text class="error__label" y="-5">{{ error.data.name }}</text>
          <text class="error__amount" y="20">{{ error.data.amount }}</text>
        </g>
      </g>
    </svg>
    <div class="controls">
      <div class="control" v-for="error in errors" :key="error.name">
        <label>{{ error.name }}</label>
      </div>
    </div>
  </div>
</template>

<style scoped>
svg {
  display: block;
  margin: 0 auto;
}

.error {
  transition: transform 0.2s ease-in-out;
  text-anchor: middle;
}

.error__circle {
  transition: r 0.2s ease-in-out;
}

.error__label {
  fill: #fff;
  font-weight: bold;
  text-shadow: 0 3px 10px rgba(0, 0, 0, 0.6);
  font-size: 23px;
}

.error__amount {
  fill: #fff;
  font-weight: bold;
  font-size: 25px;
  text-shadow: 0 3px 10px rgba(0, 0, 0, 0.6);
}

.controls {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.control {
  display: inline-flex;
  flex-direction: column;
  margin: 0 4px;
}

.control label {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 4px;
}

.control input {
  display: block;
  font: inherit;
  width: 100px;
}
</style>
