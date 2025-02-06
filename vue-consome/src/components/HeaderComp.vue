<template>
  <v-container class="app-container">
    <v-app-bar app color="title" class="gradient-toolbar">
      <v-toolbar-title class="custom-title animated-title">
        <router-link to="/">
          <v-img src="@/assets/logo/logo.svg" max-height="40" max-width="40" class="ml-4"></v-img>
        </router-link>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-text-field label="아이디" max-width="130px" class="mr-1"></v-text-field>
      <v-text-field label="비밀번호" max-width="130px" class="mr-1" type="password"></v-text-field>
      <v-btn class="mr-1">로그인</v-btn>
      <v-btn text @click="signupDialog = true">회원가입</v-btn>
      <!-- 회원가입 모달 -->
      <v-dialog v-model="signupDialog" width="600">
        <v-card>
          <v-card-title class="text-h5"></v-card-title>
          <v-card-text>
            <SignupPage />
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="red" text @click="signupDialog = false">닫기</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <!-- 회원가입 모달 end -->
    </v-app-bar>
  </v-container>
</template>

<script lang="ts">
import { ref } from "vue";
import SignupPage from "@/views/user/SignupPage.vue";
import "@/assets/css/header.scss";

export default {
  components: { SignupPage },
  setup() {
    const signupDialog = ref(false);

    return { signupDialog };
  }
};
</script>
