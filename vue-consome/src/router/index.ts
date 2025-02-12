import { createRouter, createWebHistory } from "vue-router";
import MainPage from "@/views/MainPage.vue";
import Signup from "@/views/user/SignupPage.vue";
import Login from "@/views/user/LoginPage.vue";
import findLoginId from "@/views/user/findLoginId.vue";
import findPassword from "@/views/user/findPassword.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/", component: MainPage },
    { path: "/user/signup", component: Signup },
    { path: "/user/login", component: Login },
    { path: "/user/findLoginId", component: findLoginId },
    { path: "/user/findPassword", component: findPassword }
  ]
});

export default router;
