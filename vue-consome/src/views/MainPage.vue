<template>
  <v-container class="d-flex justify-center">
    <v-sheet class="main-content">
      <h1>메인 페이지</h1>
      <v-row>
        <v-btn text to="/board" class="mr-1">게시판</v-btn>
        <v-btn text to="/board2">게시판2</v-btn>
      </v-row>
    </v-sheet>
  </v-container>
</template>
<script setup lang="ts"></script>
<style scoped>
.main-content {
  width: 100%;
  max-width: 1200px;
  text-align: center;
  min-height: 1036px;
  max-height: 1080px;
  height: 100%;
  background-color: transparent !important; /* 전체 배경색과 맞춤 */
  box-shadow: none;
}
</style>
