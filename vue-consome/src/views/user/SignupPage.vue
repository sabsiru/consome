<template>
  <v-container class="signup-container">
    <v-card class="pa-6 mx-auto" max-width="500">
      <v-card-title class="text-h5 text-center">회원가입</v-card-title>
      <v-card-text>
        <v-form @submit.prevent="signup">
          <v-text-field v-model="user.loginId" label="아이디" :rules="[requiredRule]" density="comfortable"></v-text-field>
          <v-text-field v-model="user.nickname" label="닉네임" :rules="[requiredRule]" density="comfortable"></v-text-field>
          <v-text-field v-model="user.name" label="이름" :rules="[requiredRule]" density="comfortable"></v-text-field>
          <v-text-field v-model="user.email" label="이메일" :rules="[requiredRule, emailRule]" density="comfortable" type="email"></v-text-field>

          <v-label class="mb-2">전화번호</v-label>
          <v-row>
            <v-col cols="4">
              <v-text-field
                v-model="first"
                maxlength="3"
                placeholder="010"
                @input="allowOnlyNumbers"
                :rules="[requiredRule]"
                density="comfortable"
              ></v-text-field>
            </v-col>
            <v-col cols="4">
              <v-text-field
                v-model="second"
                maxlength="4"
                placeholder="1234"
                @input="allowOnlyNumbers"
                :rules="[requiredRule]"
                density="comfortable"
              ></v-text-field>
            </v-col>
            <v-col cols="4">
              <v-text-field
                v-model="third"
                maxlength="4"
                placeholder="5678"
                @input="allowOnlyNumbers"
                :rules="[requiredRule]"
                density="comfortable"
              ></v-text-field>
            </v-col>
          </v-row>

          <v-text-field v-model="user.password" label="비밀번호" type="password" :rules="[requiredRule]" density="comfortable"></v-text-field>

          <v-btn color="primary" class="v-btn" type="submit">회원가입</v-btn>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();
const API_URL = "/user/signup"; // ✅ URL 상수 선언

// 사용자 입력 필드 (User 엔티티 구조와 일치)
const user = ref({
  loginId: "",
  nickname: "",
  name: "",
  email: "",
  password: ""
});

const first = ref("");
const second = ref("");
const third = ref("");

const formattedPhoneNumber = computed(() => {
  return `${first.value}-${second.value}-${third.value}`;
});
//필수 입력 검증
const requiredRule = (v) => !!v || "필수 입력 항목입니다.";
// 이메일 형식 검증
const emailRule = (v) => /.+@.+\..+/.test(v) || "유효한 이메일 주소를 입력하세요.";

// 회원가입 요청
const signup = () => {
  const requestData = {
    ...user.value,
    phoneNumber: formattedPhoneNumber.value
  };

  axios
    .post(API_URL, requestData)
    .then((response) => {
      alert(response.data);
      router.push("/");
    })
    .catch(handleSignupError);
};
// ✅ 별도 오류 핸들링 함수 추가
const handleSignupError = (error) => {
  const errorMessage = error?.response?.data || "회원가입 실패: 서버 오류 발생";
  alert(errorMessage);
};
const allowOnlyNumbers = (event) => {
  event.target.value = event.target.value.replace(/[^0-9]/g, ""); // 숫자만 남김
};
</script>
