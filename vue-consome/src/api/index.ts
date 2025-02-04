import axios from "axios";

const apiClient = axios.create({
  baseURL: "/api", // ✅ API 요청 시 자동으로 백엔드(`/api` → `localhost:8080`으로 프록시)
  headers: {
    "Content-Type": "application/json"
  }
});

export default apiClient;
