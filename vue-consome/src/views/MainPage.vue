<script lang="ts" setup>
import { useRouter } from "vue-router";

const router = useRouter();

const goToSignup = () => {
  router.push("/user/signup"); // ✅ 회원가입 페이지로 이동
};
</script>

<template>
  <div class="container">
    <h1>메인 페이지</h1>
    <button @click="goToSignup">회원가입</button>
  </div>
</template>

<style scoped>
.container {
  text-align: center;
  margin-top: 50px;
}
button {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 10px;
  cursor: pointer;
  font-size: 16px;
  padding: 10px 20px;
  border-radius: 5px;
}
</style>
