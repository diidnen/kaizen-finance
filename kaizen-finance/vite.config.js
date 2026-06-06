import { fileURLToPath, URL } from 'node:url'
import path from 'path'
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import { viteStaticCopy } from 'vite-plugin-static-copy'
import { submitContact } from './lib/submitContact.js'

// Get __dirname equivalent for ES modules
const __dirname = path.dirname(fileURLToPath(import.meta.url))

function contactApiDevPlugin(mode) {
  return {
    name: 'contact-api-dev',
    configureServer(server) {
      server.middlewares.use(async (req, res, next) => {
        if (req.url !== '/api/send-email' || req.method !== 'POST') {
          return next()
        }

        const env = loadEnv(mode, process.cwd(), '')
        let body = ''

        req.on('data', (chunk) => {
          body += chunk
        })

        req.on('end', async () => {
          try {
            const data = JSON.parse(body)
            await submitContact(data, env.WEB3FORMS_ACCESS_KEY)
            res.setHeader('Content-Type', 'application/json')
            res.statusCode = 200
            res.end(JSON.stringify({ success: true }))
          } catch (error) {
            res.setHeader('Content-Type', 'application/json')
            res.statusCode = error.message?.includes('not configured') ? 503 : 500
            res.end(JSON.stringify({ success: false, message: error.message }))
          }
        })
      })
    },
  }
}

// https://vite.dev/config/
export default defineConfig(({ mode }) => ({
  plugins: [
    vue(),
    contactApiDevPlugin(mode),
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
      input: path.resolve(__dirname, 'index.html')
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        ws: true,
        bypass(req) {
          if (req.url?.startsWith('/api/send-email')) {
            return false
          }
        }
      }
    }
  }
}))
