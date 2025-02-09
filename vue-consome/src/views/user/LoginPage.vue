<template>
  <v-container class="login-container">
    <v-card class="pa-6 mx-auto" max-width="400">
      <v-card-title class="text-h5 text-center">로그인</v-card-title>
      <v-card-text>
        <v-form ref="loginForm" @submit.prevent="handleLogin">
          <v-text-field v-model="loginId" label="아이디" :rules="[requiredRule]" density="compact" min-width="300" outlined></v-text-field>

          <v-text-field
            v-model="password"
            label="비밀번호"
            type="password"
            :rules="[requiredRule]"
            density="compact"
            min-width="300"
            outlined
          ></v-text-field>

          <v-btn color="primary" block type="submit" :loading="loading">로그인</v-btn>

          <v-alert v-if="errorMessage" type="error" class="mt-2">{{ errorMessage }}</v-alert>
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

const requiredRule = (v: string) => !!v || "필수 입력 항목입니다.";

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
    alert("로그인 실패: " + error.response.data);
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
