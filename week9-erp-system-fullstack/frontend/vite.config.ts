import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8081',
      '/swagger-ui.html': 'http://localhost:8081',
      '/swagger-ui': 'http://localhost:8081',
      '/v3': 'http://localhost:8081'
    }
  }
});
