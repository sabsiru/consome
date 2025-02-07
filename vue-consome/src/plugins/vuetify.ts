// Styles
import "@mdi/font/css/materialdesignicons.css";
import "vuetify/styles";

// Vuetify
import { createVuetify } from "vuetify";
import colors from "vuetify/util/colors";

export default createVuetify({
  theme: {
    defaultTheme: "customTheme",
    themes: {
      customTheme: {
        dark: false,
        colors: {
          primary: colors.lightBlue.accent1,
          secondary: colors.lightBlue.darken1, // 어두운 회색
          accent: colors.blue.accent1, // 하얀색
          error: colors.red.accent3, // 빨간색 강조
          success: colors.green.darken1, // 성공 버튼 색상
          warning: colors.amber.darken2, // 경고 버튼 색상
          title: colors.shades.transparent,
          transparent: colors.shades.transparent
        }
      }
    }
  },
  defaults: {
    VBtn: {
      rounded: "md",
      style: "width: 80px",
      variant: "elevated"
    }
  }
});
// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
