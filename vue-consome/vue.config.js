const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    client: {
      logging: "info",
      overlay: true,
      progress: true,
      reconnect: 3
    },
    proxy: {
      "/": {
        target: "http://localhost:8080",
        changeOrigin: true,
        logLevel: "debug",
        ws: true,
        pathRewrite: { "^/": "" }
      }
    }
  },

  pluginOptions: {
    vuetify: {
      // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
    }
  }
});
