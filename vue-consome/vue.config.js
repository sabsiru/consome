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
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        logLevel: "debug",
        ws: false,
        secure: false,
        pathRewrite: { "^/": "" }
      }
    }
  },
  pluginOptions: {
    vuetify: {
      // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
    }
  },
  chainWebpack: (config) => {
    config.plugin("html").tap((args) => {
      args[0].favicon = "public/logo.svg";
      return args;
    });
  }
});
