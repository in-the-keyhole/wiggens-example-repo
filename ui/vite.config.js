import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';
// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        port: 5173,
        proxy: {
            // Proxy API calls to Spring Boot during development
            '/codex-example': {
                target: 'http://localhost:8080',
                changeOrigin: true
            }
        }
    },
    test: {
        environment: 'jsdom',
        setupFiles: './vitest.setup.ts',
        globals: true
    }
});
