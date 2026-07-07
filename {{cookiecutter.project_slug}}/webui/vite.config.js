import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    // Quinoa is configured (build-dir: dist) to pick up this output.
    outDir: 'dist',
  },
  server: {
    // Port Quinoa uses when proxying the dev server in `quarkusDev`.
    port: 5173,
  },
})
