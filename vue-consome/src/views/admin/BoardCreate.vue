<template>
  <v-container>
    <v-card class="pa-6 mx-auto" max-width="500">
      <v-card-title class="text-h5 text-center">게시판 생성</v-card-title>
      <v-card-text>
        <v-form @submit.prevent="createBoard">
          <v-text-field
            v-model="board.name"
            label="게시판 이름"
            density="comfortable"
            variant="underlined"
            class="mt-3"
            placeholder="게시판 이름을 입력하세요."
            required
          ></v-text-field>
          <v-textarea
            v-model="board.description"
            label="게시판 설명"
            density="comfortable"
            variant="underlined"
            class="mt-3"
            placeholder="게시판에 대한 설명을 입력하세요."
            required
          ></v-textarea>
          <v-select
            v-model="board.parentId"
            :items="parentBoards"
            item-text="name"
            item-value="id"
            label="부모 게시판 선택 (없으면 최상위)"
            density="comfortable"
            variant="underlined"
            class="mt-3"
          ></v-select>
          <v-btn block color="primary" variant="elevated" type="submit" :disabled="!board.name || !board.description" :loading="loading" class="mt-3">
            게시판 생성
          </v-btn>
        </v-form>
      </v-card-text>
    </v-card>
    <!-- 성공/실패 알림 모달 -->
    <v-dialog v-model="dialog" width="400" persistent>
      <v-card>
        <v-card-title class="text-h6">{{ dialogTitle }}</v-card-title>
        <v-card-text>{{ dialogMessage }}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="handleDialogClose()">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const board = ref({
  name: "",
  description: "",
  parentId: null
});

const parentBoards = ref([]); // 부모 게시판 목록

const loading = ref(false);
const dialog = ref(false);
const dialogTitle = ref("");
const dialogMessage = ref("");
const createSuccess = ref(false);

// 부모 게시판 목록 로드
const loadParentBoards = async () => {
  try {
    const response = await axios.get("/api/boards/parents");
    // 예: response.data가 [{id: 1, name: "게임"}, ...] 형태
    parentBoards.value = response.data;
  } catch (error) {
    console.error("부모 게시판 목록 로드 실패:", error);
  }
};

onMounted(() => {
  loadParentBoards();
});

// 게시판 생성 API 호출
const createBoard = async () => {
  loading.value = true;
  try {
    const response = await axios.post("/api/boards/create", board.value);
    dialogTitle.value = "게시판 생성 성공";
    dialogMessage.value = response.data.message || "게시판이 성공적으로 생성되었습니다.";
    createSuccess.value = true;
  } catch (error) {
    console.error("게시판 생성 실패:", error);
    dialogTitle.value = "게시판 생성 실패";
    dialogMessage.value = error.response?.data?.message || "게시판 생성에 실패했습니다.";
    createSuccess.value = false;
  } finally {
    loading.value = false;
    dialog.value = true;
  }
};

const handleDialogClose = () => {
  dialog.value = false;
  if (createSuccess.value) {
    router.push("/admin/boards");
  }
};
</script>

<style scoped>
/* 필요에 따라 스타일 추가 */
</style>
