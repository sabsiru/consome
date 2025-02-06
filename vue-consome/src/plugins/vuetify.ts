// Styles
import "@mdi/font/css/materialdesignicons.css";
import "vuetify/styles";

// Vuetify
import { createVuetify } from "vuetify";
import colors from "vuetify/util/colors";
import defaults from "@/plugins/defaults"; // 기본 설정 가져오기

export default createVuetify({
  defaults,
  theme: {
    defaultTheme: "customTheme",
    themes: {
      customTheme: {
        colors: {
          primary: colors.lightBlue.accent1,
          secondary: colors.grey.darken1, // 어두운 회색
          accent: colors.shades.white, // 하얀색
          error: colors.red.accent3, // 빨간색 강조
          success: colors.green.darken1, // 성공 버튼 색상
          warning: colors.amber.darken2, // 경고 버튼 색상
          title: colors.shades.transparent,
          titleBtn: colors.shades.white
        }
      }
    }
  }
});
// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
