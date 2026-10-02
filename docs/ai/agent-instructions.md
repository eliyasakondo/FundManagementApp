# 🤖 AI Agent Operating Directives & Execution Instructions
## Fund Management App (Android / Jetpack Compose)

---

> [!IMPORTANT]
> **Mandatory Agent Operating Rules:**
> 1. **Read Before Coding:** Always read `docs/product/prd.md`, `docs/product/design.md`, and `docs/features/fund-workflows.md` before writing code.
> 2. **Never Invent Product Rules:** Business rules in `docs/` take strict priority over AI assumptions.
> 3. **Clean Architecture Guardrails:** Keep UI Composables in `presentation/` pure and business logic strictly inside `domain/usecase/`.
> 4. **No Shell File Editing Commands:** NEVER use `sed`, `awk`, `rm`, `echo >`, or shell redirection. Use built-in IDE tools (`write_file`, `replace_file_content`).

---

## 1. Context Read Order Matrix

Before implementing any feature or refactoring code, inspect these documents in sequence:

| Priority | Document Path | Purpose |
| :--- | :--- | :--- |
| **1** | **[docs/product/prd.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/product/prd.md)** | Core product requirements, roles (Admin, Manager, Member) & permissions |
| **2** | **[docs/features/fund-workflows.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/features/fund-workflows.md)** | Monthly contribution, surplus donation split & partial payment formulas |
| **3** | **[docs/product/design.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/product/design.md)** | Material 3 Color Tokens, Emerald Green branding & component states |
| **4** | **[docs/architecture/architecture.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/architecture/architecture.md)** | 21 Supabase PostgreSQL tables schema & Room DB entity mappings |
| **5** | **[docs/engineering/security.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/engineering/security.md)** | Production security directives, dual-layer validation & rate limiting |
| **6** | **[docs/engineering/task.md](file:///C:/Users/Tusar/AndroidStudioProjects/FundManagementApp/docs/engineering/task.md)** | Milestone roadmap & active task checklist |

---

## 2. Core Product Principles

- **Admin-Controlled System:** The initial version is admin-controlled. Members mainly log in to view their statement, payment history, notifications, and fund status.
- **Member Self-Service Boundaries:** Members CANNOT create their own accounts, edit contribution rates, approve payments, or modify income/expense records.
- **Financial Ledger Integrity:** Every received payment must preserve an auditable split between monthly contribution and extra donation.
- **Rate Change Isolation:** Changing a member's assigned monthly rate today MUST NEVER alter or recalculate past historical contribution records.

---

## 3. Bilingual & Formatting Compliance

> [!NOTE]
> All user-facing UI labels, status badges, buttons, error messages, and numbers MUST dynamically switch between Bangla (`বাংলা`) and English:
> - **English:** `৳1,500.00` | `2026-09-12` | `Paid`
> - **Bangla:** `৳১,৫০০.০০` | `১২-০৯-২০২৬` | `পরিশোধিত`

---

## 4. Definition of Done (Feature Completion Checklist)

A task or feature is considered 100% complete ONLY when:
- [ ] Business logic implemented and verified against `docs/features/fund-workflows.md`.
- [ ] Role-based access control (RBAC) enforced.
- [ ] Client-side and server-side input validation present.
- [ ] Bilingual text and number formatting supported.
- [ ] Unit tests written and passing in `app/src/test/`.
- [ ] Zero lint warnings or compiler errors.
