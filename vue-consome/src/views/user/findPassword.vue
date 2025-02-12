<template>
  <v-container class="find-container d-flex align-center justify-center">
    <v-card class="pa-6 mx-auto" max-width="400">
      <v-card-title class="text-h5 text-center">비밀번호 찾기</v-card-title>
      <v-card-text>
        <v-form @submit.prevent="showPasswordFields ? updatePassword() : findPassword()">
          <v-text-field
            v-if="!showPasswordFields"
            v-model="loginId"
            label="아이디"
            outlined
            min-width="300"
            @update:modelValue="updateLoginId"
          ></v-text-field>
          <v-text-field v-if="!showPasswordFields" v-model="email" label="이메일" outlined min-width="300"></v-text-field>

          <!-- 비밀번호 입력 필드 (성공 시 표시) -->
          <v-text-field
            v-if="showPasswordFields"
            v-model="password1"
            label="새 비밀번호"
            type="password"
            outlined
            min-width="300"
            placeholder="비밀번호는 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
            @blur="updatePassword"
          ></v-text-field>

          <v-text-field
            v-if="showPasswordFields"
            v-model="password2"
            label="비밀번호 확인"
            type="password"
            outlined
            min-width="300"
            placeholder="비밀번호를 똑같이 입력해주세요."
          ></v-text-field>

          <!-- 버튼 텍스트 변경 -->
          <v-btn color="primary" type="submit" density="comfortable" variant="elevated" block>
            {{ showPasswordFields ? "비밀번호 변경하기" : "비밀번호 찾기" }}
          </v-btn>
        </v-form>

        <v-alert v-if="showSnackbar" :type="snackbarType" class="mt-2" :color="snackbarColor">
          <span style="white-space: pre-line">
            {{ successMessage }}
          </span>
        </v-alert>
      </v-card-text>
    </v-card>

    <!-- ✅ 비밀번호 변경 후 사용자에게 이동할지 선택하는 모달 -->
    <v-dialog v-model="showConfirmDialog" max-width="400">
      <v-card>
        <v-card-title class="text-h5 text-center">비밀번호 변경 완료</v-card-title>
        <v-card-text class="text-center"> 비밀번호가 성공적으로 변경되었습니다.<br />로그인 화면 혹은 메인 화면으로 이동하시겠습니까? </v-card-text>
        <v-card-actions class="d-flex justify-center">
          <v-btn color="primary" @click="goToLogin">로그인 화면</v-btn>
          <v-btn color="primary" @click="goToMain">메인 화면</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";

const route = useRoute();
const router = useRouter();
const loginId = ref("");
const email = ref("");
const password1 = ref("");
const password2 = ref("");
const successMessage = ref("");
const showPasswordFields = ref(false); // ✅ 비밀번호 입력 필드 표시 여부
const showSnackbar = ref(false);
const snackbarColor = ref("error");
const snackbarType = ref("error");

// ✅ 새로운 confirm 창 표시 상태
const showConfirmDialog = ref(false);

// 페이지 로드 시 loginId, email 가져오기
onMounted(() => {
  loginId.value = route.query.loginId || "";
  email.value = route.query.email || "";
});

const updateLoginId = (value: string) => {
  loginId.value = value;
};

// 비밀번호 찾기 요청
const findPassword = async () => {
  try {
    const response = await axios.post("/user/findPassword", {
      loginId: loginId.value,
      email: email.value
    });
    if (response.data.available) {
      successMessage.value = response.data.message;
      showPasswordFields.value = true; // ✅ 비밀번호 입력 필드 표시
      showSnackbar.value = true;
      snackbarColor.value = "success";
      snackbarType.value = "success";
    } else {
      successMessage.value = response.data.message;
      showSnackbar.value = true;
      showPasswordFields.value = false;
      snackbarColor.value = "error";
      snackbarType.value = "warning";
    }
  } catch (error) {
    successMessage.value = "서버 오류 발생";
    showSnackbar.value = true;
  }
};

// 비밀번호 변경 요청
const updatePassword = async () => {
  try {
    const response = await axios.post("/user/updatePassword", {
      loginId: loginId.value,
      email: email.value,
      password1: password1.value,
      password2: password2.value
    });
    if (response.data.available) {
      successMessage.value = response.data.message;
      showConfirmDialog.value = true; // ✅ 변경 완료 시 confirm 창 표시
    } else {
      showSnackbar.value = true;
      successMessage.value = response.data.message;
      snackbarColor.value = "error";
      snackbarType.value = "warning";
    }
  } catch (error) {
    successMessage.value = error.data?.message || "서버 오류 발생";
    showSnackbar.value = true;
  }
};

// ✅ 로그인 화면으로 이동
const goToLogin = () => {
  showConfirmDialog.value = false;
  router.push("/user/login");
};

// ✅ 메인 페이지로 이동
const goToMain = () => {
  showConfirmDialog.value = false;
  router.push("/");
};
</script>

<style scoped>
.find-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 전체 화면 높이 사용 */
  max-width: 100%; /* 전체 화면 너비 */
  padding: 20px;
}
</style>
