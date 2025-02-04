<template>
  <div class="main">
    <img alt="Vue logo" src="../assets/logo.png" />
    <HelloWorld msg="Welcome to Spring Boot + Vue.js" />
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import HelloWorld from "@/components/HelloWorld.vue";
import MainWorld from "@/components/MainWorld.vue"; // @ is an alias to /src

@Options({
  components: {
    MainWorld
  }
})
export default class MainView extends Vue {}
</script>
