# Architectural Decision Records (ADRs) Summary

---

## ADR 001: Clean Architecture and MVVM Design Pattern
- **Status:** Accepted
- **Context:** Financial calculation logic across 200+ members requires complete separation from UI code to allow isolated unit testing.
- **Decision:** Adopt Clean Architecture (Data, Domain, Presentation layers) with ViewModels and Kotlin StateFlow.
- **Consequences:** High testability and clear code modularity.

---

## ADR 002: Supabase (PostgreSQL) as Cloud Database over NoSQL
- **Status:** Accepted
- **Context:** Financial applications demand strict relational integrity (Foreign Keys, Joins) and ACID compliance. NoSQL databases (Firestore) make complex joins and data migrations difficult.
- **Decision:** Adopt Supabase PostgreSQL as primary cloud database with Room DB for local offline caching.
- **Consequences:** Safe relational data, zero migration headaches, full SQL power.

---

## ADR 003: Offline-First Strategy via Room Database
- **Status:** Accepted
- **Context:** Members and managers in areas with intermittent internet connection must still view balances and load screens instantly.
- **Decision:** Implement Room Database as local cache layer behind Repository interfaces.
- **Consequences:** Instant UI rendering, background network sync.
