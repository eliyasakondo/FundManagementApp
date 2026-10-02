# 🏦 Organization Member Contribution & Fund Management System
## Enterprise Android Application & Supabase Backend Documentation

---

> [!NOTE]
> **System Purpose:**
> Built for managing 200+ organization members. Members view their profiles, monthly contribution statements, donation histories, and overall fund health. Admins and Managers log offline payment receipts (bKash, Nagad, Bank Transfer, Cash) and manage organizational operating expenses.

---

## 📚 Complete Documentation Index

| Module / Document | Path | Description & Purpose | Status |
| :--- | :--- | :--- | :--- |
| **Product Requirements (PRD)** | **[docs/product/prd.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/product/prd.md)** | Core product vision, 3 User Roles (Admin, Manager, Member), Feature Scope & Acceptance Criteria | ✅ Verified |
| **UI/UX Design System** | **[docs/product/design.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/product/design.md)** | Material 3 Color Tokens, Emerald Green branding, Bilingual Typography & Component Interactive States | ✅ Verified |
| **Financial Business Workflows** | **[docs/features/fund-workflows.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/features/fund-workflows.md)** | Monthly contribution formulas, surplus donation routing, partial payments & multi-month prepayments | ✅ Verified |
| **Database & System Architecture** | **[docs/architecture/architecture.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/architecture/architecture.md)** | 21 Supabase PostgreSQL Relational Tables SQL DDL, Foreign Keys, Indexes & Room DB Offline Caching | ✅ Verified |
| **System Overview & Infrastructure** | **[docs/architecture/system-overview.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/architecture/system-overview.md)** | Security, Rate Limiting, Audit Logging, PostgREST API Gateway & 24/7 Keep-Alive Health Ping | ✅ Verified |
| **Architectural Decision Records** | **[docs/decisions/decision.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/decisions/decision.md)** | 8 Architectural Decisions (Clean Architecture, Supabase, Room DB, Encrypted Storage, Hilt DI) | ✅ Verified |
| **Security & Compliance Hardening** | **[docs/engineering/security.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/engineering/security.md)** | Master 18 Production Security Directives, Dual-Layer Validation, XSS/SQLi Prevention & Rate Limits | ✅ Verified |
| **Automated Test Plan** | **[docs/engineering/test_plan.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/engineering/test_plan.md)** | Test Pyramid, Financial Engine Unit Test Matrix, Room DAO In-Memory Tests & 100% Coverage Mandate | ✅ Verified |
| **Engineering Rules & Git Standards** | **[docs/engineering/rules.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/engineering/rules.md)** | Architectural Guardrails, Kotlin Naming Conventions, Conventional Commits & Code Review Checklist | ✅ Verified |
| **Live Task Tracker & Milestones** | **[docs/engineering/task.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/engineering/task.md)** | Milestone 1 through Milestone 6 step-by-step implementation roadmap & active task checklist | ✅ Active |
| **Real-time Project Memory** | **[docs/engineering/memory.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/engineering/memory.md)** | Active project state, completed milestones, current task tracker, and known issues | ✅ Active |

---

> [!IMPORTANT]
> **Core Architectural Guarantees:**
> 1. **Clean Architecture Isolation:** UI Composables in `presentation/` and Domain Use Cases in `domain/` are 100% decoupled from the database engine.
> 2. **Bilingual Native Support:** Every UI screen dynamically re-renders all text, numbers, dates, and currency symbols between Bangla (`বাংলা`) and English.
> 3. **Offline-First Resilience:** Instant screen rendering from device local Room Database with background network synchronization to Supabase PostgreSQL.
