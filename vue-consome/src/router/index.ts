import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { defineComponent, h } from "vue";
import MainPage from "@/views/MainPage.vue";
import Signup from "@/views/user/SignupPage.vue";
import Login from "@/views/user/LoginPage.vue";
import findLoginId from "@/views/user/findLoginId.vue";
import findPassword from "@/views/user/findPassword.vue";
import { adminRoutes } from "./admin";
import { RouterView } from "vue-router";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "MainPage",
    component: MainPage
  },
  {
    path: "/user",
    name: "User",
    component: defineComponent({
      name: "UserParentInline",
      setup() {
        return () => h(RouterView);
      }
    }),
    children: [
      { path: "signup", name: "Signup", component: Signup },
      { path: "login", name: "Login", component: Login },
      { path: "findLoginId", name: "FindLoginId", component: findLoginId },
      { path: "findPassword", name: "FindPassword", component: findPassword }
    ]
  },
  ...adminRoutes
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

const getUserRole = (): string | null => {
  const userStr = localStorage.getItem("user");
  if (!userStr) return null;
  try {
    const user = JSON.parse(userStr);
    return user.role; // 예: "ROLE_ADMIN", "ROLE_USER" 등
  } catch (e) {
    console.error("사용자 정보 파싱 에러:", e);
    return null;
  }
};

router.beforeEach((to, from, next) => {
  // /admin으로 시작하는 모든 URL에 대해
  if (to.path.startsWith("/admin")) {
    const role = getUserRole();
    if (!role) {
      // 로그인이 안 된 경우
      window.alert("로그인이 필요합니다.");
      return next({ name: "Login" });
    } else if (role !== "ROLE_ADMIN") {
      // 로그인은 되어 있으나 관리자가 아닌 경우
      window.alert("관리자 권한이 필요합니다.");
      return next({ name: "MainPage" });
    }
  }
  next();
});

export default router;
