import axios from 'axios'

export const api = axios.create({
  baseURL: '/codex-example/api/v1'
})

