<template>
  <v-app>
    <div>
      <!--    헤더-->
      <Header />
      <!--    메인-->
      <v-main>
        <router-view />
      </v-main>
    </div>
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
