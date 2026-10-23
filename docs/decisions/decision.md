# Architectural Decision Records (ADRs) Log
## Fund Management App (Android / Supabase Backend)

---

## ADR 001: Clean Architecture & MVVM Pattern

- **Status:** Accepted
- **Date:** 2026-03-01
- **Context:** Financial calculation logic across 200+ members requires complete isolation from UI rendering code to allow 100% unit testing and maintainability.
- **Decision:** Adopt Clean Architecture (split into `data/`, `domain/`, `presentation/`, and `di/` packages) paired with MVVM and Kotlin `StateFlow`.
- **Consequences:**
  - High unit testability without Android SDK dependencies in Domain Use Cases.
  - Clear separation of concerns between UI Compose screens, ViewModel state managers, and Data Repositories.

---

## ADR 002: Supabase (PostgreSQL) Cloud Database over NoSQL (Firestore)

- **Status:** Accepted
- **Date:** 2026-03-01
- **Context:** Financial applications demand strict relational data integrity (Foreign Keys, Joins, Transactions) and ACID compliance. NoSQL databases make complex relational queries, multi-table transactions, and data integrity constraints difficult.
- **Decision:** Adopt Supabase PostgreSQL as primary cloud database with Row Level Security (RLS) policies.
- **Consequences:**
  - Guarantees ACID compliance and relational integrity across 12 connected tables.
  - Native PostgREST API generation and Realtime WebSocket subscriptions.

---

## ADR 003: Offline-First Architecture via Room Database

- **Status:** Accepted
- **Date:** 2026-03-01
- **Context:** Members and managers in areas with low or intermittent cellular internet must still view financial balances and load app screens instantly.
- **Decision:** Implement local Room Database (`AppDatabase`) as a local cache layer behind Repository interfaces.
- **Consequences:**
  - Instant UI rendering from local database cache.
  - Background network synchronization when connection is restored.

---

## ADR 004: Supabase Auth with Biometric & Encrypted Local Storage

- **Status:** Accepted
- **Date:** 2026-03-02
- **Context:** User authentication requires secure session persistence without exposing sensitive access tokens or credentials to malicious apps or unauthorized device users.
- **Decision:** Use Supabase JWT Authentication paired with local Android `EncryptedSharedPreferences` / `Encrypted DataStore` powered by Android Keystore, with optional Biometric Prompt (Fingerprint/Face Unlock).
- **Consequences:**
  - Tokens are encrypted at rest on the Android device.
  - Zero plaintext credential storage in app files.

---

## ADR 005: Database Migration Policy & Versioning

- **Status:** Accepted
- **Date:** 2026-03-02
- **Context:** Schema changes to local Room Database or cloud PostgreSQL during app updates must not cause app crashes or data loss for existing members.
- **Decision:** Write explicit Room `Migration` objects for every local database version bump, and execute idempotent SQL migration scripts on Supabase.
- **Consequences:**
  - Zero `IllegalStateException` crashes due to schema mismatch.
  - Preserves local cached member records across app updates.

---

## ADR 006: Hilt for Dependency Injection

- **Status:** Accepted
- **Date:** 2026-03-02
- **Context:** Managing dependencies (Database DAOs, Repositories, Use Cases, ViewModels) manually leads to boilerplate factory code and testing difficulties.
- **Decision:** Standardize on Google's Hilt (built on Dagger) for Dependency Injection.
- **Consequences:**
  - Automated compile-time dependency graph generation.
  - Seamless ViewModel injection via `@HiltViewModel` and easy test mocking.

---

## ADR 007: Bilingual Strings & Dynamic Currency Formatter Architecture

- **Status:** Accepted
- **Date:** 2026-03-03
- **Context:** The app must support seamless runtime switching between Bangla (`বাংলা`) and English for all text, numbers, dates, and currency symbols (`৳`).
- **Decision:** Centralize all UI strings in `Strings.kt` locale dictionary and route all financial numbers through `CurrencyFormatter.format(amount, locale)`.
- **Consequences:**
  - Instant runtime language switching without restarting the application.
  - Consistent Bengali number digit rendering (`১,৫০০.০০` $\leftrightarrow$ `1,500.00`).

---

## ADR 008: Jetpack Compose Material 3 & Design Token System

- **Status:** Accepted
- **Date:** 2026-03-03
- **Context:** To ensure a consistent, modern visual identity across all screens and prevent UI inconsistencies across pages.
- **Decision:** Standardize on Jetpack Compose Material 3 with custom brand color tokens (`#1B5E20` Emerald Green, `#C9A227` Gold) and defined interactive component states (Default, Hover/Focus, Pressed, Disabled).
- **Consequences:**
  - Consistent 12dp/16dp card radii and 48dp touch targets across all screens.
  - Native dark mode support and responsive Compose layouts.

---

## ADR 009: Feature-First Package Architecture over Role-Based Monoliths

- **Status:** Accepted
- **Date:** 2026-03-03
- **Context:** Monolithic role-based UI files (`AdminScreens.kt`, `ManagerScreens.kt`) grew over 1,000+ lines, violating the Single Responsibility Principle and causing Kotlin overload resolution ambiguity errors.
- **Decision:** Migrate UI layer to a Feature-First package structure (`ui/dashboard`, `ui/members`, `ui/payments`, `ui/reports`, `ui/settings`, `ui/member`), where each screen and dialog lives in its own dedicated single-responsibility file.
- **Consequences:**
  - Prevents monolithic file compilation errors and simplifies git merges.
  - Improves component reusability and Clean Architecture presentation separation.

---

## ADR 010: Complete Navigation Menu Coverage & Role-Gated Expense Management

- **Status:** Accepted
- **Date:** 2026-03-03
- **Context:** Placeholder routes for Member `messages` and `profile` tabs lacked dedicated screens, and organizational expenses lacked a dedicated management ledger screen and voucher entry modal.
- **Decision:** Implement `NotificationsScreen.kt`, `MemberProfileScreen.kt`, `ExpensesScreen.kt`, `RecordExpenseModal.kt`, and `NotificationBottomSheet.kt`, while enforcing role-based access control (RBAC) guards on Admin System Settings.
- **Consequences:**
  - 100% complete navigation menu coverage for Admin, Manager, and Member roles.
  - Dedicated expense voucher logging and categorized expense ledger.

---

## ADR 011: Admin & Manager Individual Member Payment History Ledger

- **Status:** Accepted
- **Date:** 2026-03-03
- **Context:** Admins and Managers required the ability to inspect any individual member's month-by-month contribution records (Required, Paid, Due, Status Badges, and Last Payment Date).
- **Decision:** Enhance `ViewMemberProfileModal.kt` with a Tab Switcher ("Profile Info" $\leftrightarrow$ "Monthly Ledger") bound to `OrganizationRepository.memberHistory` StateFlow.
- **Consequences:**
  - Provides instant month-by-month financial breakdown for any selected member.
  - Zero duplication across Admin and Manager member management views.

---

## ADR 012: Dynamic Month & Date Range Filter Engine for Financial Reports

- **Status:** Accepted
- **Date:** 2026-03-03
- **Context:** Financial audits require filtering collection ledgers and summaries by custom month ranges (e.g. Jan 2025 to Jun 2025) rather than viewing static annual totals.
- **Decision:** Implement `MonthRangeFilterCard.kt` with Start Month & End Month dropdowns and Quick Preset Filter Chips (`All Months`, `Current Month`, `Last 3 Months`, `Last 6 Months`), integrated into `PaymentsScreen.kt` and `ReportsScreen.kt`.
- **Consequences:**
  - Dynamic recalculation of total funds collected, contribution portions, and extra donations across custom time periods.
  - Consistent filtering capabilities for both Admin and Manager roles.
