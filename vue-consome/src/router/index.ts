import { createRouter, createWebHistory } from "vue-router";
import MainPage from "@/views/MainPage.vue";
import Signup from "@/views/user/SignupPage.vue";
import Login from "@/views/user/LoginPage.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/", component: MainPage },
    { path: "/user/signup", component: Signup },
    { path: "/user/login", component: Login }
  ]
});

export default router;
