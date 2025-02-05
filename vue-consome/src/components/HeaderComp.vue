<template>
  <header class="app-header">
    <nav>
      <router-link to="/">메인</router-link> |
      <router-link to="/user/signup">회원가입</router-link>
    </nav>
  </header>
</template>
<script>
import "@/assets/css/Header.css";

export default {
  name: "HeaderComp"
};
</script>
