<script setup lang="ts">
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

// 사용자 입력 필드 (User 엔티티 구조와 일치)
const user = ref({
  loginId: "",
  nickname: "",
  name: "",
  email: "",
  first: "",
  second: "",
  third: "",
  password: ""
});

// 회원가입 요청
const signup = async () => {
  try {
    const formattedPhoneNumber = `${user.value.first}-${user.value.second}-${user.value.third}`;
    const requestData = {
      loginId: user.value.loginId,
      nickname: user.value.nickname,
      name: user.value.name,
      email: user.value.email,
      password: user.value.password,
      phoneNumber: formattedPhoneNumber // ✅ 변환된 phoneNumber 전송
    };
    const response = await axios.post("/api/users/signup", requestData);
    alert(response.data); // "회원가입 성공 (ID: xxx)"
    router.push("/"); // 회원가입 성공 시 메인 페이지 이동
  } catch (error) {
    // ❌ 중복 검사 실패 시 서버에서 받은 메시지를 alert로 표시
    if (error.response?.status === 400) {
      alert(error.response.data); // "이미 사용 중인 로그인 ID입니다."
    } else {
      alert("회원가입 실패: 서버 오류 발생");
    }
  }
};
/**
 * 숫자만 입력 가능하도록 변환하는 함수
 */
const allowOnlyNumbers = (event) => {
  event.target.value = event.target.value.replace(/[^0-9]/g, ""); // 숫자만 남김
};
</script>

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
