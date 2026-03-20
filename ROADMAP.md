# VoteChain — Roadmap

Implementation plan to take this from university blockchain core to a deployable SYSEC portfolio product.

---

## Phase 0 — Fix & Refactor Core ✦ *Start here*

**Goal:** Leave the existing Java code clean and bug-free before building on top of it.

- [ ] Fix bug in `BlockChain_Manager.createGenesis()` (no-arg overload) — block is created but never added to chain → crashes at runtime
- [ ] Remove duplicated `generateHash()` — exists in both `SHA256.java` and `BlockChain_Manager.java`. Keep it in `SHA256` as a static util, call it from the manager
- [ ] Replace `double` return type on `getVotesPerCandidate()` with `int` — vote counts are integers
- [ ] Add duplicate voter check in `BlockChain_Manager` — prevent same voter ID from appearing more than once per election
- [ ] Write a minimal `main()` demo in `BlockChain.java` — create election, cast votes, mine blocks, print chain and results. Proves the core works before migration

**Deliverable:** `mvn compile && mvn exec:java` runs a full demo with no errors.

---

## Phase 1 — Spring Boot Migration

**Goal:** Wrap the blockchain core in a Spring Boot project without changing its logic.

- [ ] Bootstrap new Spring Boot 3 project (Java 17, Maven)
- [ ] Move existing `BlockChain/` classes into `src/main/java/com/sysec/votechain/blockchain/`
- [ ] Create `ElectionService` — owns the `BlockChain_Manager` instance, exposes business methods (`createElection`, `castVote`, `getResults`, `validateChain`)
- [ ] Wire `ElectionService` as a Spring `@Service`
- [ ] Verify the project compiles and `ElectionService` unit tests pass

**Deliverable:** Spring Boot starts up, no endpoints yet, but service layer is functional.

---

## Phase 2 — REST API

**Goal:** Expose the blockchain through HTTP endpoints.

- [ ] `POST /api/elections` — create election with name, candidates list, start/end date
- [ ] `POST /api/elections/{id}/vote` — cast a vote (body: voterId, candidateId)
- [ ] `GET  /api/elections/{id}/blockchain` — return full chain as JSON (for public auditing)
- [ ] `GET  /api/elections/{id}/results` — return vote counts per candidate
- [ ] `GET  /api/elections/{id}/validate` — re-run chain validation, return true/false + first invalid block if any
- [ ] Add Swagger/OpenAPI (`springdoc-openapi`) — auto-generated docs at `/swagger-ui`
- [ ] Return proper HTTP status codes and error messages

**Deliverable:** Full API testable via Postman or Swagger UI.

---

## Phase 3 — JWT Authentication

**Goal:** Protect write operations. Read operations stay public (auditability).

- [ ] Add `jjwt` dependency to `pom.xml`
- [ ] Create `User` model — roles: `ADMIN`, `VOTER`
- [ ] `POST /api/auth/register` — register voter
- [ ] `POST /api/auth/login` — return signed JWT
- [ ] Protect `POST /api/elections` with `ADMIN` role
- [ ] Protect `POST /api/elections/{id}/vote` with `VOTER` role
- [ ] `GET` endpoints remain public (no auth required)
- [ ] Store voter ID from JWT token — prevents a user from voting twice under different requests

**Deliverable:** Unauthorized vote attempts return 401. Admin and voter flows work end-to-end.

---

## Phase 4 — PostgreSQL Persistence

**Goal:** Chain survives server restarts.

- [ ] Add Spring Data JPA + PostgreSQL driver to `pom.xml`
- [ ] Create `ElectionEntity`, `BlockEntity`, `VoteEntity` JPA models
- [ ] On startup: load existing elections from DB, reconstruct `BlockChain_Manager` instances in memory
- [ ] On each mine: persist the new block to DB
- [ ] `application.properties` reads DB credentials from environment variables only
- [ ] Add Flyway or Liquibase for DB migrations (schema versioning)

**Deliverable:** Stop and restart the server — all elections and votes are still there.

---

## Phase 5 — Docker & Deployment

**Goal:** Running on Hetzner, accessible via HTTPS, monitored.

- [ ] Write `Dockerfile` for the Spring Boot app (multi-stage build: Maven build → JRE runtime)
- [ ] Write `docker-compose.yml` — services: `votechain-api`, `postgres`
- [ ] All secrets via `.env` file (never committed) — document required vars in `.env.example`
- [ ] Internal Docker network — PostgreSQL not exposed to host
- [ ] Deploy to Hetzner via SSH over WireGuard
- [ ] Configure domain + SSL in Nginx Proxy Manager
- [ ] Add to UptimeKuma

**Deliverable:** `https://votechain.sysec.com.sv/api` is live and monitored.

---

## Phase 6 — Frontend Dashboard *(optional but high impact)*

**Goal:** Visual blockchain explorer that makes the project immediately understandable to non-technical visitors.

- [ ] React app (Vite)
- [ ] Live chain viewer — each block rendered as a card, showing hash, previousHash, vote count
- [ ] Results page — bar chart per candidate, updates on poll
- [ ] Chain validation status — green/red indicator per block
- [ ] Vote casting form (authenticated)
- [ ] Deploy as static build served by Nginx

**Deliverable:** Opening the URL shows a live, interactive blockchain — zero Postman required.

---

## Milestone Summary

| Phase | What it proves | Status |
|-------|---------------|--------|
| 0 — Fix core | Blockchain logic is correct | ⬜ |
| 1 — Spring Boot | Java enterprise architecture | ⬜ |
| 2 — REST API | API design, OpenAPI docs | ⬜ |
| 3 — JWT Auth | Security-first development | ⬜ |
| 4 — PostgreSQL | Production-grade persistence | ⬜ |
| 5 — Docker + Deploy | DevOps, real deployment | ⬜ |
| 6 — Frontend | Full-stack, UX for demos | ⬜ |
