<template>
  <v-app-bar app density="comfortable">
    <v-container class="animated-title d-flex">
      <v-toolbar-title>
        <router-link to="/">
          <v-img src="@/assets/logo/logo.svg" max-height="46" max-width="46" class="ml-4"></v-img>
        </router-link>
      </v-toolbar-title>
      <v-row justify="end" align="start" dense>
        <v-btn class="mr-1" density="comfortable">로그인</v-btn>
        <v-btn text @click="signupDialog = true" density="comfortable">회원가입</v-btn>
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
      </v-row>
    </v-container>
    <!-- 회원가입 모달 end -->
  </v-app-bar>
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
