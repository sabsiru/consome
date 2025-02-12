<template>
  <v-container class="login-container">
    <v-card class="pa-6 mx-auto" max-width="400">
      <v-card-title class="text-h5 text-center">로그인</v-card-title>
      <v-card-text>
        <v-form ref="loginForm" @submit.prevent="handleLogin">
          <v-text-field v-model="loginId" label="아이디" density="compact" min-width="300" outlined></v-text-field>

          <v-text-field v-model="password" label="비밀번호" type="password" density="compact" min-width="300" outlined></v-text-field>

          <v-btn block color="secondary" type="submit" density="comfortable" variant="elevated" :loading="loading">로그인 </v-btn>
          <!-- 아이디 찾기 & 비밀번호 찾기 버튼 (가로 정렬) -->
          <v-row class="mt-2 justify-center">
            <v-col cols="5">
              <v-btn text to="/user/findLoginId" variant="plain" block color="black" density="compact">아이디 찾기</v-btn>
            </v-col>
            <v-col cols="1">|</v-col>
            <v-col cols="5">
              <v-btn text to="/user/findPassword" variant="plain" block color="black" density="compact">비밀번호 찾기</v-btn>
            </v-col>
          </v-row>

          <v-snackbar v-model="showSnackbar" :timeout="3000" color="error" location="top">
            <span style="white-space: pre-line">
              {{ errorMessage }}
            </span>
            <template v-slot:actions>
              <v-btn color="white" text @click="showSnackbar = false">닫기</v-btn>
            </template>
          </v-snackbar>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();
const loginId = ref("");
const password = ref("");
const loading = ref(false);
const errorMessage = ref("");
const showSnackbar = ref(false);

const handleLogin = async () => {
  loading.value = true;
  errorMessage.value = "";
  try {
    const response = await axios.post("/user/login", {
      loginId: loginId.value,
      password: password.value
    });

    if (response.data) {
      localStorage.setItem("accessToken", response.data.token); // ✅ 토큰 저장
      localStorage.setItem("refreshToken", response.data.token); // ✅ 토큰 저장
      localStorage.setItem("user", JSON.stringify(response.data)); // ✅ 유저 정보 저장
      window.location.href = "/"; // ✅ 새로고침 후 메인으로 이동
    }
  } catch (error) {
    errorMessage.value = error.response.data;
    showSnackbar.value = true;
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 화면 중앙 정렬 */
}
</style>
