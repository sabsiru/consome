<template>
  <div id="app">
    <Header />
    <router-view />
  </div>
</template>

<script>
import Header from "@/components/HeaderComp.vue";
import "@/assets/css/app.scss"; // App 전용 CSS 파일 불러오기

export default {
  name: "App",
  components: {
    Header
  }
};
</script>
