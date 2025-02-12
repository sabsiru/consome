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
          primary: colors.shades.black,
          secondary: colors.grey.lighten4,
          accent: colors.blue.accent1,
          error: colors.red.accent3,
          success: colors.green.darken1,
          warning: colors.amber.darken2,
          title: colors.shades.transparent,
          transparent: colors.shades.transparent,
          lightBlue: colors.lightBlue.lighten1,
          darkGrey: colors.grey.darken4
        }
      }
    }
  },
  defaults: {
    VBtn: {
      color: "primary",
      rounded: "md",
      style: "width: 80px",
      variant: "plain"
    }
  }
});
// https://vuetifyjs.com/en/introduction/why-vuetify/#feature-guides
