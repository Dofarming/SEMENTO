<script setup>
import { ref, onMounted, watch } from "vue";
import HeadText from "@/components/Text/HeadText.vue";
import Cardhead from "@/components/Text/Cardhead.vue";

const error200 = ref(29);
const error300 = ref(27);
const maxJobTime = ref("10:00 - 11:00");
const maxWorkTime = ref("00:00 - 01:00");
const maxIdleTime = ref("00:00 - 01:00");
const avgWorkTime = ref("3분 40초");
const avgIdleTime = ref("0분 39초");
const workSuccuessRatecontentText = ref(
  "OHT의 작업 스케줄링 및 효율성 개선을 위한 AI 기반 예측 및 최적화 시스템 도입.이를 통해 작업 분배를 최적화하고, 데드라인 내 완료율을 높일 수 있습니다."
);

const errorContentText = ref(
  "에러 코드를 분석하여 각각의 에러 원인을 파악하고, OHT의 소프트웨어 및 하드웨어 점검을 강화해야 합니다.또한, 실시간 모니터링 시스템을 도입하여 에러 발생 시 즉각 대응할 수 있도록 해야 합니다."
);

const idleContentText = ref(
  "시간대 별 작업량 분포를 재조정하여 특정 시간대에 작업이 몰리지 않도록 해야 합니다.특히 유휴 시간이 많은 시간대에 작업을 분배함으로써 전체적인 효율성을 높일 수 있습니다."
);

const ohtContentText = ref(
  "OHT의 스케줄링을 최적화하고, 작업량 예측을 통해 OHT 배치를 동적으로 조정할 수 있는 시스템을 도입합니다.이를 통해 OHT 활용률을 극대화하고, 불필요한 유휴 시간을 최소화할 수 있습니다."
);

function splitText(text) {
  return text.split(".").filter((line) => line.trim() !== "");
}
</script>

<template>
  <div class="analysis-results">
    <HeadText header-text="AI가 제공하는 문제점 및 솔루션" />
    <div class="section">
      <div class="problem-solution">
        <Cardhead
          header-text="작업 성공률 향상"
          contentText=""
          class="ai-solution-head"
        />
        <div class="problem-content">
          <font-awesome-icon
            :icon="['fas', 'triangle-exclamation']"
            size="lg"
          />
          <span class="problem-sentence"
            >현재 작업 성공률이 40.17%로 매우 낮습니다.</span
          >
        </div>
        <div class="solution-content">
          <div>
            <font-awesome-icon
              :icon="['fas', 'hand-point-up']"
              size="xl"
              style="color: #74c0fc"
            />
          </div>
          <div class="solution-sentence">
            <span
              v-for="(line, index) in splitText(workSuccuessRatecontentText)"
              :key="index"
            >
              {{ line }}<br />
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="problem-solution">
        <Cardhead
          header-text="에러율 감소"
          contentText=""
          class="ai-solution-head"
        />
        <div class="problem-content">
          <font-awesome-icon
            :icon="['fas', 'triangle-exclamation']"
            size="lg"
          />
          <span class="problem-sentence">
            모든 실패 작업이 에러로 인해 발생합니다. OHT 에러 {{ error200 }}건,
            설비 에러 {{ error300 }}건으로 OHT 에러와 설비 에러가
            주요원인입니다.</span
          >
        </div>
        <div class="solution-content">
          <font-awesome-icon
            :icon="['fas', 'hand-point-up']"
            size="xl"
            style="color: #74c0fc"
          />
          <div class="solution-sentence">
            <span
              v-for="(line, index) in splitText(errorContentText)"
              :key="index"
            >
              {{ line }}<br />
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="problem-solution">
        <Cardhead
          header-text="작업 및 유휴 시간대 조정"
          contentText=""
          class="ai-solution-head"
        />
        <div class="problem-content">
          <font-awesome-icon
            :icon="['fas', 'triangle-exclamation']"
            size="lg"
          />
          <span class="problem-sentence"
            >00:00 - 01:00 시간대에 작업이 활발하면서 동시에 유휴 상태가 많은
            것은 비효율적입니다.</span
          >
        </div>
        <div class="solution-content">
          <font-awesome-icon
            :icon="['fas', 'hand-point-up']"
            size="xl"
            style="color: #74c0fc"
          />
          <div class="solution-sentence">
            <span
              v-for="(line, index) in splitText(idleContentText)"
              :key="index"
            >
              {{ line }}<br />
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="section">
      <div class="problem-solution">
        <Cardhead
          header-text="OHT 활용률 최적화"
          contentText=""
          class="ai-solution-head"
        />
        <div class="problem-content">
          <font-awesome-icon
            :icon="['fas', 'triangle-exclamation']"
            size="lg"
          />
          <span class="problem-sentence"
            >OHT 당 평균 작업량이 낮고, 일부 시간대에 유휴 상태가 많이
            발생합니다.</span
          >
        </div>
        <div class="solution-content">
          <font-awesome-icon
            :icon="['fas', 'hand-point-up']"
            size="xl"
            style="color: #74c0fc"
          />
          <div class="solution-sentence">
            <span
              v-for="(line, index) in splitText(idleContentText)"
              :key="index"
            >
              {{ line }}<br />
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="section">
      <Cardhead header-text="결론" contentText="" class="ai-solution-head" />
      <p>
        위의 문제점 및 솔루션을 바탕으로 다음과 같은 액션 플랜을 제시합니다:
      </p>
      <ul>
        <li>AI 기반 예측 및 최적화 시스템 도입.</li>
        <li>에러 코드 분석 및 대응 방안 마련.</li>
        <li>작업 및 유휴 시간대 조정을 통한 효율성 향상.</li>
        <li>OHT 활용률 최적화를 위한 동적 배치 시스템 구축.</li>
      </ul>
      <p>
        이를 통해 작업 성공률을 높이고, 에러율을 감소시키며, 전체적인 OHT 운영
        효율성을 크게 향상시킬 수 있을 것입니다.
      </p>
    </div>
  </div>
</template>

<style scoped>
.analysis-results {
  padding: 20px;
}

.cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.section {
  margin-bottom: 20px;
  padding: 20px;
  border-radius: 10px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: 10px;
}

.problem-solution {
  margin-bottom: 10px;
}

.problem-solution h3 {
  margin-bottom: 5px;
}

.ai-solution-head {
  margin: 0;
}

.problem-content {
  padding: 10px 0 0 0;
  white-space: pre-wrap;
}
.solution-content {
  padding: 10px 0 0 0;
  white-space: pre-wrap;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.solution-sentence {
  margin-left: 10px;
}

.problem-sentence {
  margin-left: 10px;
}
</style>
