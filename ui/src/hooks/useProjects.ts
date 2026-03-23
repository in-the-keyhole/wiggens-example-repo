import { useCallback, useEffect, useState } from 'react'
import { createProject, fetchProjects } from '../codex-example/api/projects'
import type { Project, ProjectPayload } from '../codex-example/api/types'

interface UseProjectsResult {
  projects: Project[]
  loading: boolean
  error: string | null
  reload: () => Promise<void>
  addProject: (payload: ProjectPayload) => Promise<void>
}

export const useProjects = (): UseProjectsResult => {
  const [projects, setProjects] = useState<Project[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)

  const loadProjects = useCallback(async () => {
    setLoading(true)
    try {
      const data = await fetchProjects()
      setProjects(data)
      setError(null)
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load projects')
    } finally {
      setLoading(false)
    }
  }, [])

  const addProject = useCallback(async (payload: ProjectPayload) => {
    try {
      const project = await createProject(payload)
      setProjects((prev) => [...prev, project])
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create project')
      throw err
    }
  }, [])

  useEffect(() => {
    loadProjects()
  }, [loadProjects])

  return {
    projects,
    loading,
    error,
    reload: loadProjects,
    addProject
  }
}
