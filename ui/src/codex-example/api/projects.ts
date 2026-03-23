import { apiClient } from './client'
import type { Project, ProjectPayload } from './types'

export const fetchProjects = async (): Promise<Project[]> => {
  const { data } = await apiClient.get<Project[]>('/projects')
  return data
}

export const createProject = async (payload: ProjectPayload): Promise<Project> => {
  const { data } = await apiClient.post<Project>('/projects', payload)
  return data
}
