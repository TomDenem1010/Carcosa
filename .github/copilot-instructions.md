# Copilot instructions (Hastur / Carcosa)

## Project overview
- Spring Boot service (Spring Boot `4.0.3`) targeting Java `21` (see `pom.xml`).
- Single-module Maven project; entrypoint is `src/main/java/com/home/carcosa/Application.java`.
- HTTP server listens on port `1895` (`src/main/resources/application.properties`) and Docker exposes the same (`Dockerfile`).
- `target/` contains build outputs (generated JAR/classes); do not edit files under `target/`.

## Code organization & patterns
- Package root: `com.home.carcosa`.
- Web layer lives in `com.home.carcosa.controller` (example: `AboutController`).
  - Use Spring MVC annotations (`@RestController`, `@RequestMapping`, `@GetMapping`).
  - Prefer constructor injection; Lombok is used for boilerplate (`@AllArgsConstructor`).
- Business logic lives in `com.home.carcosa.service` (example: `AboutService`).
- API DTOs live in `com.home.carcosa.dto` and use Java records where appropriate (example: `AboutResponse`).

## Request/response logging
- A global servlet filter logs every request/response: `com.home.carcosa.filter.RequestResponseLoggingFilter`.
  - Uses `ContentCachingRequestWrapper`/`ContentCachingResponseWrapper` and MUST call `copyBodyToResponse()`.
  - Bodies are truncated (max 8,192 chars) and payload caching is capped (1 MiB).
- Logging configuration is via `logback.xml`.
  - The filter logger is named `FILTER` (via `@Slf4j(topic = "FILTER")`).

## Formatting
- Formatting is enforced via Spotless + Eclipse formatter config: `eclipse-java-formatter.xml` (see Spotless plugin in `pom.xml`).
- Prefer running formatting before submitting changes:
  - `mvn spotless:apply`

## Common developer commands
- Run locally:
  - `mvn spring-boot:run`
  - or `mvn -DskipTests package` then `java -jar target/*.jar`
- Test:
  - `mvn test` (basic smoke test is `src/test/java/com/home/carcosa/ApplicationTests.java`).

## Docker / CI
- Local image build:
  - `docker build -t carcosa .`
  - `docker run --rm -p 1895:1895 carcosa`
- GitHub Actions workflow: `.github/workflows/docker-image.yml`
  - Computes tag as `${project.version}-${UTC timestamp}` using Maven help:evaluate.
  - CI sets up Java `25` for the workflow step, but the Docker build stage uses Temurin `21`.
  - Builds/pushes using `docker/build-push-action` and `Dockerfile` (multi-stage, Temurin 21).

## When adding new endpoints
- Follow the existing layering: controller → service → DTO record.
- Keep endpoints small; put text/logic in the service (pattern used by `AboutController`/`AboutService`).
