# Implementation Instructions

You are iteratively building a timesheet application. Read OPEN Github  issues for implementation requirments. Follow the instructions in the document and the Agents.md for architecture and processes.

## Instructions

1. Read the Github origin/main for open issues 
2. Implement open issues completely, following the conventions in Agents.md. 
3. **Run the relevant tests** to verify your work:
   - Backend: `cd api && ./mvnw test`
   - Frontend: `cd ui && npm test`
   - If tests fail, fix the issues before proceeding.
4. Create or Update the Readme.md file for developer instructions   
4. Follow the Github Instructions show below.


### GitHub instructions
- Use Github issues to implement from GITHUB obtain issues using MCP server. Use the Persnonnal Access Token (PAT) approach in the URL. The PAT is avialble in the GITHUB_TOKEN environment variable
- Commit after completing each issue 
- Use conventional commit messages: `feat:`, `fix:`, `chore:`, `test:`
- Keep commits atomic — one story per commit
- Push commits to origin/master Use MCP with the PAT in the URL. The PAT is available in the GITHUB_TOKEN environment
- Mark the github issue closed. Use MCP with the PAT in the URL. The PAT is available in the GITHUB_TOKEN environment variable


## Rules
- Only work on ONE issue per iteration.
- Do not skip issues 
- Always run tests before marking an issue closed.
- If you get stuck on an issue, describe what went wrong and still attempt to fix it. Do not skip it.
- Follow all conventions in Agents.md (layered architecture, DTOs, test coverage, etc.).
- When done update or create a Readme.md with developer instructions
