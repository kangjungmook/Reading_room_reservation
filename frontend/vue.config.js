const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: "../src/main/resources/static",  // Spring Boot의 정적 파일 디렉토리
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8085', // Spring Boot API 서버 주소
        changeOrigin: true
      }
    }
  }
});
