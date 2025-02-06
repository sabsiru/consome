<template>
  <v-app>
    <!--    헤더-->
    <v-container class="v-container">
      <Header />
    </v-container>

    <!--    메인-->
    <v-container class="v-container">
      <v-main>
        <router-view />
      </v-main>
    </v-container>
  </v-app>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import Header from "@/components/HeaderComp.vue"; // 헤더 컴포넌트 불러오기
export default defineComponent({
  name: "App",
  components: {
    Header
  }
});
</script>
