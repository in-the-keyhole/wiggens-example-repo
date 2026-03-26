import axios from 'axios'

export const api = axios.create({
  baseURL: 'http://localhost:8080/codex-example/api/v1',
  headers: {
    'Content-Type': 'application/json'
  }
})

