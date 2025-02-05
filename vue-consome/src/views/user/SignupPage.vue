<template>
  <div class="signup-container">
    <h2>회원가입</h2>
    <form @submit.prevent="signup">
      <div>
        <label>아이디:</label>
        <input v-model="user.loginId" type="text" required />
      </div>
      <div>
        <label>닉네임:</label>
        <input v-model="user.nickname" type="text" required />
      </div>
      <div>
        <label>이름:</label>
        <input v-model="user.name" type="text" required />
      </div>
      <div>
        <label>이메일:</label>
        <input v-model="user.email" type="email" required />
      </div>
      <label>전화번호:</label>
      <div class="phone-number-container">
        <input v-model="first" type="text" maxlength="3" placeholder="010" @input="allowOnlyNumbers" />
        <span>-</span>
        <input v-model="second" type="text" maxlength="4" placeholder="1234" @input="allowOnlyNumbers" />
        <span>-</span>
        <input v-model="third" type="text" maxlength="4" placeholder="5678" @input="allowOnlyNumbers" />
      </div>
      <div>
        <label>비밀번호:</label>
        <input v-model="user.password" type="password" required />
      </div>
      <button type="submit">회원가입</button>
    </form>
  </div>
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

<style scoped>
.signup-container {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}
form div {
  margin-bottom: 10px;
}
label {
  display: block;
  font-weight: bold;
}
input {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
button {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
button:hover {
  background-color: #0056b3;
}
.phone-number-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%; /* 부모 컨테이너의 전체 너비 사용 */
}

.phone-number-container input {
  width: calc((100% - 40px) / 3); /* 전체 입력칸 너비를 동일하게 분배 */
  max-width: 120px; /* 입력칸이 너무 커지지 않도록 최대 너비 설정 */
  text-align: center;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 8px;
}

.phone-number-container span {
  font-size: 18px;
}
</style>
