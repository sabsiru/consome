<template>
  <v-app-bar app density="compact">
    <v-container class="animated-title d-flex">
      <v-toolbar-title>
        <router-link to="/">
          <v-img src="@/assets/logo/logo2.svg" max-height="36" max-width="250" class="ml-4"></v-img>
        </router-link>
      </v-toolbar-title>
      <v-row justify="end" align="start" dense>
        <div v-if="isLoggedIn" class="user-info">
          <div v-if="user && user.role === 'ROLE_ADMIN'">
            <span>관리자님 환영합니다. 포인트: {{ user?.point }}</span>
          </div>
          <div v-else>
            <span>{{ user?.nickname }}님 환영합니다. 포인트: {{ user?.point }}</span>
          </div>
          <v-btn text @click="logout">로그아웃</v-btn>
        </div>
        <div v-else>
          <v-btn text to="/user/login" class="mr-1" density="comfortable">로그인</v-btn>
          <v-btn text @click="signupDialog = true" density="comfortable">회원가입</v-btn>
        </div>
        <!-- 회원가입 모달 -->
        <v-dialog v-model="signupDialog" width="600">
          <v-card>
            <v-card-title class="text-h5"></v-card-title>
            <v-card-text>
              <SignupPage @closeSignup="signupDialog = false" />
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="red" text @click="signupDialog = false">닫기</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-row>
    </v-container>
    <!-- 회원가입 모달 end -->
  </v-app-bar>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import SignupPage from "@/views/user/SignupPage.vue";
import "@/assets/css/header.scss";

const router = useRouter();
const signupDialog = ref(false);
const user = ref<{ nickname: string; point: number } | null>(null);

// ✅ 로그인 상태 확인
const isLoggedIn = computed(() => !!localStorage.getItem("accessToken"));

// ✅ 로컬 스토리지에서 유저 정보 로드
const loadUser = () => {
  const userData = localStorage.getItem("user");
  if (userData) {
    user.value = JSON.parse(userData);
  }
};

// ✅ 로그아웃 함수
const logout = () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  localStorage.removeItem("user");
  window.location.href = "/"; // 새로고침 후 로그인 페이지 이동
};

// ✅ 컴포넌트 마운트 시 유저 정보 불러오기
onMounted(loadUser);
</script>
<style scoped>
.user-info {
  display: flex;
  align-items: center;
  font-size: 14px;
}
</style>
