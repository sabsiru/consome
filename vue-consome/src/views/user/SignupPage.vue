<template>
  <v-container>
    <v-card class="pa-6 mx-auto" max-width="500">
      <v-card-title class="text-h5 text-center">회원가입</v-card-title>
      <v-card-text>
        <v-form @submit.prevent="signup">
          <v-text-field
            v-model="user.loginId"
            label="아이디"
            density="comfortable"
            variant="underlined"
            class="mt-3"
            placeholder="한글, 영문, 숫자만 사용할 수 있습니다."
            @blur="checkLoginId"
          >
            <template #append-inner>
              <v-icon :color="loginIdStatus.color">{{ loginIdStatus.icon }}</v-icon>
            </template>
          </v-text-field>
          <p v-if="loginIdStatus.message" :class="loginIdStatus.colorClass">
            {{ loginIdStatus.message }}
          </p>
          <v-text-field
            v-model="user.nickname"
            label="닉네임"
            density="comfortable"
            variant="underlined"
            class="mt-3"
            placeholder="2~10자의 한글, 영문 그리고 숫자의 조합만 사용 가능합니다."
            @blur="checkNickname"
          >
            <template #append-inner>
              <v-icon :color="nicknameStatus.color">{{ nicknameStatus.icon }}</v-icon>
            </template>
          </v-text-field>
          <p v-if="nicknameStatus.message" :class="nicknameStatus.colorClass">
            {{ nicknameStatus.message }}
          </p>
          <v-text-field
            v-model="user.email"
            label="이메일"
            density="comfortable"
            variant="underlined"
            type="email"
            class="mt-3"
            placeholder="email@adress.com"
            @blur="checkEmail"
          >
            <template #append-inner>
              <v-icon :color="emailStatus.color">{{ emailStatus.icon }}</v-icon>
            </template>
          </v-text-field>
          <p v-if="emailStatus.message" :class="emailStatus.colorClass">
            {{ emailStatus.message }}
          </p>
          <v-text-field
            v-model="user.password"
            label="비밀번호"
            type="password"
            class="mt-3"
            density="comfortable"
            variant="underlined"
            placeholder="비밀번호는 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
            @blur="checkPassword"
          >
            <template #append-inner>
              <v-icon :color="passwordStatus.color">{{ passwordStatus.icon }}</v-icon>
            </template>
          </v-text-field>
          <p v-if="passwordStatus.message" :class="passwordStatus.colorClass">
            {{ passwordStatus.message }}
          </p>
          <v-btn color="primary" type="submit" :disabled="!isSignupEnabled" class="mt-3" :loading="loading">회원가입</v-btn>
        </v-form>
      </v-card-text>
    </v-card>
    <!-- 회원가입 성공/실패 알림 모달 -->
    <v-dialog v-model="dialog" width="400" persistent>
      <v-card>
        <v-card-title>{{ dialogTitle }}</v-card-title>
        <v-card-text>{{ dialogMessage }}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="handleDialogClose()">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
import { ref, computed, watch, defineEmits } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const dialog = ref(false); // 모달 상태
const dialogTitle = ref(""); // 모달 제목
const dialogMessage = ref(""); // 백엔드 메시지 저장
const signupSuccess = ref(false); // 회원가입 성공 여부
const loading = ref(false); // 버튼 로딩 상태

const emit = defineEmits(["closeSignup"]);

// 사용자 입력 필드 (User 엔티티 구조와 일치)
const user = ref({
  loginId: "",
  nickname: "",
  email: "",
  password: ""
});

// 검증 상태 (아이콘 및 색상)
const loginIdStatus = ref({ icon: "mdi-help-circle", color: "gray", message: "", colorClass: "" });
const nicknameStatus = ref({ icon: "mdi-help-circle", color: "gray", message: "", colorClass: "" });
const emailStatus = ref({ icon: "mdi-help-circle", color: "gray", message: "", colorClass: "" });
const passwordStatus = ref({ icon: "mdi-help-circle", color: "gray", message: "", colorClass: "" });

// 필드 값이 초기화되면 상태도 초기화
watch(
  () => user.value.loginId,
  (newValue) => {
    if (!newValue) loginIdStatus.value = { icon: "mdi-help-circle", color: "gray", message: "", colorClass: "" };
  }
);
watch(
  () => user.value.nickname,
  (newValue) => {
    if (!newValue) nicknameStatus.value = { icon: "mdi-help-circle", color: "gray", message: "", colorClass: "" };
  }
);
watch(
  () => user.value.email,
  (newValue) => {
    if (!newValue) emailStatus.value = { icon: "mdi-help-circle", color: "gray", message: "", colorClass: "" };
  }
);
watch(
  () => user.value.password,
  (newValue) => {
    if (!newValue) passwordStatus.value = { icon: "mdi-help-circle", color: "gray", message: "", colorClass: "" };
  }
);
// 중복 검사 함수
const checkLoginId = async () => {
  if (!user.value.loginId) return;
  try {
    const response = await axios.post("/user/validLoginId", { loginId: user.value.loginId });
    loginIdStatus.value = response.data.available
      ? { icon: "mdi-check-circle", color: "green", message: response.data.message, colorClass: "success-text" } // 사용 가능
      : { icon: "mdi-close-circle", color: "red", message: response.data.message, colorClass: "error-text" }; // 중복됨
  } catch (error) {
    console.error(error);
  }
};

const checkNickname = async () => {
  if (!user.value.nickname) return;
  try {
    const response = await axios.post("/user/validNickname", { nickname: user.value.nickname });
    nicknameStatus.value = response.data.available
      ? {
          icon: "mdi-check-circle",
          color: "green",
          message: response.data.message,
          colorClass: "success-text"
        }
      : { icon: "mdi-close-circle", color: "red", message: response.data.message, colorClass: "error-text" };
  } catch (error) {
    console.error(error);
  }
};

const checkEmail = async () => {
  if (!user.value.email) return;
  try {
    const response = await axios.post("/user/validEmail", { email: user.value.email });
    emailStatus.value = response.data.available
      ? {
          icon: "mdi-check-circle",
          color: "green",
          message: response.data.message,
          colorClass: "success-text"
        }
      : { icon: "mdi-close-circle", color: "red", message: response.data.message, colorClass: "error-text" };
  } catch (error) {
    console.error(error);
  }
};

const checkPassword = async () => {
  if (!user.value.password) return;
  try {
    const response = await axios.post("/user/validPassword", { password: user.value.password });
    passwordStatus.value = response.data.available
      ? {
          icon: "mdi-check-circle",
          color: "green",
          message: response.data.message,
          colorClass: "success-text"
        }
      : { icon: "mdi-close-circle", color: "red", message: response.data.message, colorClass: "error-text" };
  } catch (error) {
    console.error(error);
  }
};

// 모든 검증이 완료되었는지 확인
const isSignupEnabled = computed(() => {
  return (
    loginIdStatus.value.color === "green" &&
    nicknameStatus.value.color === "green" &&
    emailStatus.value.color === "green" &&
    passwordStatus.value.color === "green"
  );
});

//회원가입
const signup = async () => {
  loading.value = true;
  try {
    const requestData = { ...user.value };
    const response = await axios.post("/user/signup", requestData);

    dialogMessage.value = response.data.message || "회원가입이 완료되었습니다.";
    dialogTitle.value = "회원가입 성공";
    signupSuccess.value = true;

    router.push("/user/login"); // 로그인 페이지로 이동
  } catch (error) {
    alert(error);
  } finally {
    dialog.value = true;
    loading.value = false;
  }
};

// ✅ 모달 닫을 때 동작 (성공 시 로그인 페이지로 이동)
const handleDialogClose = () => {
  dialog.value = false;
  if (signupSuccess.value) {
    emit("closeSignup");
    router.push("/user/login"); // ✅ 로그인 페이지로 이동
  }
};
</script>
<style scoped>
.success-text {
  color: green;
  font-size: 12px;
  margin-top: -20px;
  text-align: right;
}

.error-text {
  color: red;
  font-size: 12px;
  margin-top: -20px;
  text-align: right;
}
</style>
