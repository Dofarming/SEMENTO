import axios from "axios";
const { VITE_SPRING_URL } = import.meta.env;

//axios 헤더 설정
const instance = axios.create({
  baseURL: VITE_SPRING_URL,
  headers: {
    "Content-Type": "application/json;charset=utf-8",
  },
  withCredentials: true, // withCredentials 설정 추가
});

//요청 인터셉터 설정 (store를 밖에서 선언하면 오류남 주의)
// instance.interceptors.request.use(async (config) => {
//   return config;
// });

//응답 인터셉터 설정
instance.interceptors.response.use(
  async (response) => {
    return response;
  },
  async (error) => {
    const {
      config,
      response: { status },
    } = error;

    // if (status === httpStatusCode.UNAUTHORIZED) {
    // }

    return Promise.reject(error);
  }
);

export { instance };
