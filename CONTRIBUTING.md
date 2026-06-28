# Contributing to Forge

First off, thank you for considering contributing to Forge! It's people like you that make Forge a great tool.

## Development Setup

1. **Prerequisites**: Ensure you have Java 21+, Node.js 22+, and Docker installed.
2. **Clone the repository**: `git clone https://github.com/forge-org/forge.git`
3. **Start backend services**: We use Docker Compose for backing services (PostgreSQL, Kafka). Run `docker compose up -d postgres kafka`.
4. **Run Backend**: `cd backend && ./gradlew :bootstrap:bootRun`
5. **Run Frontend**: `cd frontend && npm install && npm run dev`

## Branch Naming

We follow a structured branch naming convention:
- `feature/<issue-number>-<brief-description>` (e.g., `feature/42-add-github-actions`)
- `bugfix/<issue-number>-<brief-description>` (e.g., `bugfix/103-fix-docker-logs`)
- `docs/<brief-description>` for documentation updates.

## Commit Message Conventions

We adhere to [Conventional Commits](https://www.conventionalcommits.org/):
- `feat:` A new feature
- `fix:` A bug fix
- `docs:` Documentation only changes
- `style:` Changes that do not affect the meaning of the code (white-space, formatting)
- `refactor:` A code change that neither fixes a bug nor adds a feature
- `test:` Adding missing tests or correcting existing tests
- `chore:` Changes to the build process or auxiliary tools

Example: `feat(api): add trigger execution endpoint`

## Pull Request Process

1. Ensure any install or build dependencies are removed before the end of the layer when doing a build.
2. Update the README.md with details of changes to the interface, if applicable.
3. Your PR must pass all CI checks (formatting, unit tests, build).
4. You may merge the Pull Request in once you have the sign-off of at least one other developer, or if you do not have permission to do that, you may request the reviewer to merge it for you.

## Coding Standards

- **Java**: We strictly enforce constructor injection over `@Autowired` fields. Keep layers strictly separated (refer to `ARCHITECTURE.md`).
- **React/TypeScript**: Use functional components and React hooks. Ensure all types are strictly defined without `any`.

## Testing Expectations

- Provide unit tests for new business logic in the `core` and `application` layers.
- For frontend components, provide Jest/React Testing Library tests where applicable.
