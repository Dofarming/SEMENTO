<!-- 
** Table 사용법 **

 <Table
 column = array
 data = array

 모르겠으면 DashBoardView.vue를 참고하세요
 />

-->
<script setup>
import { ref, onMounted, onUnmounted, nextTick } from "vue";
import Table from "@/components/table/Table.vue";
import SimulationSideTapView from "@/views/simulation/SimulationSideTapView.vue";

const simulationSideTapView = ref(null);

const props = defineProps({
  ohtId: Number,
  columns: Array, // 테이블의 컬럼명 배열
  data: Array, // 테이블 데이터
  height: {
    type: String,
    default: "auto",
  },
  width: {
    type: String,
    default: "100%",
  },
  headerColor: {
    type: String,
    default: "#000000bd",
  },
  headerTextColor: {
    type: String,
    default: "white",
  },
  headerFontSize: {
    type: String,
    default: "19px",
  },
  bodyFontSize: {
    type: String,
    default: "15px",
  },
});

const tableData = ref(props.data);
const isSidePageOpen = ref(false); //사이드탭 기본 비활성화

//== 이벤트 핸들러 ==
function toggleSidePage(idx) {
  isSidePageOpen.value = !isSidePageOpen.value;
  if (idx == -1 || !isSidePageOpen.value) {
    simulationSideTapView.value.setPage(tableData.value, props.ohtId, -1);
    return;
  }
  tableData.value = props.data[idx];

  //simulationSideTapView가 열릴 때, SimulationSideTapView 화면 설정
  if (simulationSideTapView.value && isSidePageOpen.value) {
    simulationSideTapView.value.setPage(tableData.value, props.ohtId, idx);
  }
}

//== 무한스크롤
const pageSize = 20;
const currentPage = ref(1);
const visibleData = ref(props.data.slice(0, pageSize));

const observer = ref(null);

// 데이터 로드 함수
const loadMoreData = () => {
  const nextItemsStart = pageSize * currentPage.value;
  const nextPageData = props.data.slice(
    nextItemsStart,
    nextItemsStart + pageSize
  );
  if (nextPageData.length) {
    visibleData.value.push(...nextPageData);
    currentPage.value++;
  }
};

const sentinel = ref(null);

onMounted(() => {
  observer.value = new IntersectionObserver(
    (entries) => {
      const [entry] = entries;
      if (entry.isIntersecting) {
        loadMoreData();
      }
    },
    {
      root: null, // 뷰포트를 기준으로 감지
      threshold: 1.0, // 완전히 보이는 순간을 감지
    }
  );

  if (sentinel.value) {
    observer.value.observe(sentinel.value);
  }
});

onUnmounted(() => {
  if (observer.value && sentinel.value) {
    observer.value.unobserve(sentinel.value);
  }
});

function formatCellContent(column, cell) {
  if (column === "작업 성공여부") {
    return cell ? "성공" : "실패";
  }
  return cell;
}
</script>

<template>
  <div
    class="table-container"
    :style="{
      width: width,
      height: height,
    }"
  >
    <table>
      <thead>
        <tr>
          <!-- 각 컬럼명을 헤더로 표시, 배경 및 글자색 설정 -->
          <th
            v-for="(column, index) in columns"
            :key="column"
            :class="{
              'first-th': index === 0,
              'last-th': index === columns.length - 1,
            }"
            class="table-header"
            :style="{
              backgroundColor: headerColor,
              color: headerTextColor,
              fontSize: headerFontSize,
            }"
          >
            {{ column }}
          </th>
        </tr>
      </thead>
      <tbody>
        <!-- 데이터를 행과 셀로 반복하여 표시, 셀 텍스트 중앙 정렬 -->
        <tr
          v-for="(row, index) in visibleData"
          :key="index"
          @click="toggleSidePage(index)"
        >
          <td
            v-for="(cell, columnIndex) in row"
            :key="columnIndex"
            :style="{
              fontSize: bodyFontSize,
            }"
            class="table-cell"
          >
            <div
              :class="{
                'error-cell': columns[columnIndex] === 'ERROR',
                'status-cell': columns[columnIndex] === 'STATUS',
              }"
            >
              <div
                class="inner-content"
                :class="{
                  'no-error': columns[columnIndex] === 'ERROR' && cell === '',
                }"
              >
                {{ formatCellContent(columns[columnIndex], cell) }}
              </div>
            </div>
          </td>
        </tr>
        <!-- 센티넬 요소 -->
        <tr ref="sentinel">
          <td colspan="columns.length"></td>
        </tr>
      </tbody>
    </table>

    <div class="side-page" :class="{ open: isSidePageOpen }">
      <!-- 사이드 페이지 내용을 여기에 추가하세요 -->
      <section>
        <font-awesome-icon
          @click="toggleSidePage(-1)"
          :icon="['fas', 'angles-right']"
          size="2xl"
          style="color: #383839; margin-left: 15px; margin-top: 20px"
        />
      </section>
      <SimulationSideTapView
        ref="simulationSideTapView"
        class="table-component"
        width="100%"
        bodyFontSize="14px"
        headerFontSize="12px"
        :columns="[
          'No.',
          'Period',
          'Time Taken',
          'ERROR',
          'Average Speed',
          'Out of DeadLine',
        ]"
        :data="tableData"
      >
      </SimulationSideTapView>
    </div>
  </div>
</template>

<style scoped>
.side-page {
  width: 48%;
  height: 100%;
  background-color: #f3f2f7;
  position: fixed;
  top: 0;
  right: -48%; /* 초기 위치는 오른쪽 바깥에 있습니다 */
  transition: right 0.5s;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.09);
  overflow: scroll;
}

.side-page.open {
  right: 0; /* 열릴 때 위치 */
}
.table-container {
  border-radius: 5px;
  background-color: white;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th.table-header {
  padding: 10px;
  text-align: center;
  /* background-color: #000000bd; */
  color: white;
  font-weight: 600;
  /* font-size: 19px; */
  border-bottom: #e7e7ed 1px solid;
}
th.first-th {
  border-top-left-radius: 5px; /* 첫 번째 th에 왼쪽 둥근 모서리 적용 */
  border-bottom-left-radius: 5px;
}
th.last-th {
  border-top-right-radius: 5px; /* 마지막 th에 오른쪽 둥근 모서리 적용 */
  border-bottom-right-radius: 5px;
}
td.table-cell {
  padding: 10px;
  text-align: center;
  /* background-color: white; */
  color: black;
  /* font-size: 15px; */
  font-weight: 200;
}
td.table-cell div.error-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}
td.table-cell div.error-cell div.inner-content {
  padding: 5px 10px;
  border: 1px solid #e08989;
  display: inline-block;
  color: #ac2e2e;
  font-weight: bold;
}
td.table-cell div.status-cell div.inner-content {
  padding: 5px 10px;
  border: 1px solid #89a7e0;
  display: inline-block;
  color: #2e5aac;
  font-weight: bold;
}

td.table-cell div.error-cell div.inner-content.no-error {
  border: none;
  color: black; /* 오류가 없을 때 텍스트 색상 변경 가능 */
}

/* 마우스 호버 시 행의 배경색을 변경 */
tr:hover {
  background-color: #003cb033;
  cursor: pointer;
}

.selected-row {
  background-color: #89a7e0; /* 선택된 행의 배경색 */
  font-weight: bold;
}
</style>
