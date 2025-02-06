import { createRouter, createWebHistory } from "vue-router";
import MainPage from "@/views/MainView.vue";
import Signup from "@/views/user/SignupPage.vue";

const router = createRouter({
  history: createWebHistory(), // ✅ Vue Router가 Spring Boot와 정상적으로 연동되도록 설정
  routes: [
    { path: "/", component: MainPage },
    { path: "/user/signup", component: Signup }
  ]
});

export default router;
