<template>
  <v-container class="find-id-container d-flex align-center justify-center">
    <v-card class="pa-8 mx-auto" max-width="400">
      <v-card-title class="text-h5 text-center">아이디 찾기</v-card-title>
      <v-card-text>
        <v-form ref="findIdForm" @submit.prevent="findLoginId">
          <!-- 이메일 입력 필드 -->
          <v-text-field
            v-model="email"
            label="가입한 이메일"
            :rules="[requiredRule, emailRule]"
            density="comfortable"
            variant="outlined"
            class="mt-3"
            min-width="300"
          ></v-text-field>

          <!-- 아이디 찾기 버튼 (아이디를 찾으면 버튼 숨김) -->
          <div v-if="!foundLoginId" class="d-flex justify-center">
            <v-btn color="primary" type="submit" density="comfortable" variant="elevated" :loading="loading" block>아이디 찾기 </v-btn>
          </div>

          <!-- 찾은 아이디 메시지 -->
          <v-alert v-if="foundLoginId" type="success" class="mt-4">
            회원님의 아이디는 <strong>{{ foundLoginId }}</strong> 입니다.
          </v-alert>

          <!-- 비밀번호 찾기 버튼 (아이디를 찾으면 표시됨) -->
          <div v-if="foundLoginId" class="d-flex justify-center">
            <v-btn color="primary" class="mt-3" density="comfortable" variant="elevated" @click="goToFindPassword" block>
              비밀번호 변경하러 가기
            </v-btn>
          </div>

          <!-- 에러 메시지 -->
          <v-alert v-if="errorMessage" type="error" class="mt-4">
            {{ errorMessage }}
          </v-alert>
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
const email = ref("");
const nickname = ref("");
const foundLoginId = ref(""); // 찾은 아이디
const loading = ref(false);
const errorMessage = ref("");

// 필수 입력 검증
const requiredRule = (v: string) => !!v || "필수 입력 항목입니다.";
const emailRule = (v: string) => /.+@.+\..+/.test(v) || "유효한 이메일 주소를 입력하세요.";

// 아이디 찾기 요청
const findLoginId = async () => {
  loading.value = true;
  foundLoginId.value = "";
  errorMessage.value = "";

  try {
    const response = await axios.post("/user/findLoginId", {
      email: email.value,
      nickname: nickname.value || null // 닉네임은 선택사항
    });

    if (response.data.loginId) {
      foundLoginId.value = response.data.loginId; // 찾은 아이디 저장
    } else {
      errorMessage.value = response.data.message;
    }
  } catch (error) {
    errorMessage.value = error.response?.data || "서버 오류 발생. 다시 시도해주세요.";
  } finally {
    loading.value = false;
  }
};

// 비밀번호 찾기 페이지로 이동 (아이디 값 유지)
const goToFindPassword = () => {
  router.push({
    path: "/user/findPassword",
    query: { loginId: foundLoginId.value, email: email.value }
  });
};
</script>

<style scoped>
.find-id-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 전체 화면 높이 사용 */
  max-width: 100%; /* 전체 화면 너비 */
  padding: 20px;
}
</style>
