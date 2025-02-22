import { fileURLToPath, URL } from 'node:url'
import path from 'path'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import { viteStaticCopy } from 'vite-plugin-static-copy'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    viteStaticCopy({
      targets: [
        {
          src: 'src/assets',
          dest: 'assets'
        }
      ]
    }),
    vueDevTools(),
  ],
  build: {
    rollupOptions: {
      input: {
        main: path.resolve(__dirname, 'index.html'),
        home: path.resolve(__dirname, 'src/components/home.vue'),
        services: path.resolve(__dirname, 'src/components/services.vue'),
        training: path.resolve(__dirname, 'src/components/training.vue'),
        pricing: path.resolve(__dirname, 'src/components/pricing.vue'),
        order: path.resolve(__dirname, 'src/components/order.vue'),
        contact: path.resolve(__dirname, 'src/components/contact.vue'),
        login: path.resolve(__dirname, 'src/components/login.vue')
      }
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    host: '0.0.0.0',
    port: 5173,
    open: true
  }
})
