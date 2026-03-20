# VoteChain — Blockchain Voting System

> A tamper-evident electronic voting system built on a custom blockchain implementation, designed for auditability and integrity without relying on a central authority.

**Built by [SYSEC Technologies](https://sysec.com.sv) — El Salvador**

---

## What It Is

VoteChain is a voting platform where each vote is an immutable transaction recorded in a cryptographically linked chain of blocks. No vote can be altered after it is cast without invalidating every block that follows it — making fraud detectable by anyone with access to the chain.

This project implements the blockchain primitives from scratch in Java (no web3j, no external blockchain libraries), then exposes them through a secured REST API.

---

## How the Blockchain Works

```
[Genesis Block]  →  [Block 1]  →  [Block 2]  →  [Block N]
  hash: 0000a3       hash: 0000f7   hash: 0000c2
  prevHash: 000...   prevHash: 0000a3  prevHash: 0000f7
  votes: [...]       votes: [...]      votes: [...]
```

Each block contains:
- A list of **Vote** records (voter ID + candidate + timestamp)
- The **hash of the previous block** — this is what forms the chain
- A **nonce** found via Proof-of-Work
- Its own **SHA-256 hash**

### Proof-of-Work

Before a block is accepted, it must be *mined*: the system iterates a nonce from 0 upward, hashing `(block_content + nonce)` each time until the resulting hash starts with N zeroes (configurable difficulty). This makes brute-force tampering computationally expensive.

```
nonce=0      → SHA256("block_data0")      = "a3f2c1..." ✗
nonce=1      → SHA256("block_data1")      = "7b9e4a..." ✗
...
nonce=48291  → SHA256("block_data48291") = "0000a7f3..." ✓ → block mined
```

### Tamper Detection

If anyone modifies a vote inside Block N:
1. `block_content` changes → its hash changes
2. Block N+1's `previousHash` no longer matches → Block N+1's hash changes
3. The entire chain from N onward is invalid

This is detectable by any node that re-validates the chain.

---

## Architecture

### Current (Core — Java)

```
src/main/java/
├── BlockChain/
│   ├── SHA256.java            # SHA-256 hashing utility
│   ├── Vote.java              # Immutable vote record
│   ├── Block.java             # Block with vote list + PoW fields
│   └── BlockChain_Manager.java # Chain management: mine, validate, query
└── Clientes/
    └── ClienteManager.java    # (Planned: client-facing logic)
```

### Target (Full Stack)

```
[React Dashboard]          ← Live blockchain explorer + vote interface
        ↓ HTTPS
[Spring Boot REST API]     ← JWT-secured endpoints
        ↓
[BlockChain Core]          ← Existing Java implementation (refactored)
        ↓
[PostgreSQL]               ← Persistent chain storage
        ↓
[Docker + Nginx]           ← Deployed on Hetzner Cloud (Ubuntu 24.04)
```

---

## API Endpoints (Planned)

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/elections` | Admin JWT | Create a new election |
| `POST` | `/api/elections/{id}/vote` | Voter JWT | Cast a vote |
| `GET` | `/api/elections/{id}/blockchain` | Public | Full chain for auditing |
| `GET` | `/api/elections/{id}/results` | Public | Vote count per candidate |
| `GET` | `/api/elections/{id}/validate` | Public | Re-validate entire chain |
| `POST` | `/api/auth/login` | — | Obtain JWT |

---

## Security Properties

| Property | Implementation |
|----------|---------------|
| Vote immutability | SHA-256 chaining — altering any vote breaks all subsequent hashes |
| Proof-of-Work | Configurable difficulty (N leading zeroes) |
| Duplicate vote prevention | Voter ID uniqueness enforced per election |
| API authentication | JWT — voters and admins have separate roles |
| Transport security | HTTPS enforced via Nginx Proxy Manager |
| Secret management | All credentials via environment variables, never hardcoded |

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Database | PostgreSQL 15 |
| Auth | JWT (jjwt) |
| Build | Maven |
| Containerization | Docker + Docker Compose |
| Reverse proxy | Nginx Proxy Manager |
| Frontend | React (planned) |

---

## Local Development (Planned)

```bash
# Clone
git clone https://github.com/sysec/votechain.git
cd votechain

# Run with Docker Compose
docker compose up -d

# API available at
http://localhost:8080/api
```

---

## Roadmap

See [ROADMAP.md](ROADMAP.md) for the full implementation plan.

---

## Why This Matters

Electronic voting systems are critical infrastructure. Most existing solutions rely on trusting a central database — if that database is compromised, results can be altered with no trace. VoteChain's approach makes every vote independently verifiable: the full chain is public, and any tampering produces a provably invalid chain.

This is not a toy — the cryptographic properties are the same as those used in production blockchain systems. What differs is scale and network distribution; those are engineering problems, not cryptographic ones.

---

*SYSEC Technologies — Cybersecurity & Secure Systems Development — El Salvador*
