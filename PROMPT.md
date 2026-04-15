# Implementation Instructions

You are iteratively building a timesheet application. Read OPEN Github  issues for implementation requirments. Follow the instructions in the document and the Agents.md for architecture and processes.

## Repository URL

repository url = <** Codex CLI please replace this with the specifiec github URL **>


## Instructions

1. Read the Github origin/main for open issues 
2. Implement open issues completely, following the conventions in Agents.md. 
3. **Run the relevant tests** to verify your work:
   - Backend: `cd api && ./mvnw test`
   - Frontend: `cd ui && npm test`
   - If tests fail, fix the issues before proceeding.
4. Make sure source code requiring compilation will compile without error.   
5. Create or Update a Readme.md file with developer instructions about the project and how to run, build, test, and execute the appliction
6. Follow the Github Instructions show below.


### GitHub instructions
- Use issues to implement from GITHUB
- Commit after completing each issue 
- Use conventional commit messages: `feat:`, `fix:`, `chore:`, `test:`
- Keep commits atomic — one story per commit
- Push commits to origin/master use the configured MCP github server to push. The Github Personel Access Token (PAT) defined in the GITHUB_TOKEN environment variable
- Mark the github issue closed


## Rules
- Only work on ONE issue per iteration.
- Do not skip issues 
- Always run tests before marking an issue closed.
- If you get stuck on an issue, describe what went wrong and still attempt to fix it. Do not skip it.
- Follow all conventions in Agents.md (layered architecture, DTOs, test coverage, etc.).
- When done update or create a Readme.md with developer instructions
