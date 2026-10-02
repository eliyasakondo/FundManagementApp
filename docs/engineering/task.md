# 📋 Live Task Tracker & Milestone Roadmap
## Fund Management App (Android / Jetpack Compose)

---

> [!TIP]
> **Active Milestone:** **Milestone 4 — Data Layer (Supabase PostgreSQL, Room DB & Repositories)**
> **Current Sprint Goal:** Implement Supabase Client configuration, DTO mappers, and repository implementations.

---

## 🚩 Milestone Overview & Status

| Milestone | Title & Scope | Status | Progress |
| :--- | :--- | :--- | :--- |
| **Milestone 1** | Planning, Architecture & Enterprise Documentation Setup | ✅ Completed | 100% |
| **Milestone 2** | Clean Architecture Package Structure & Dependencies | ✅ Completed | 100% |
| **Milestone 3** | Domain Layer Models & Financial Calculation Engine | ✅ Completed | 100% |
| **Milestone 4** | Data Layer (Supabase PostgreSQL, Room DB & Repositories) | 🚀 In Progress | 25% |
| **Milestone 5** | Presentation Layer (ViewModels, Compose UI & Bilingual Formatting) | ⏳ Pending | 0% |
| **Milestone 6** | Automated Testing, Security Hardening & Release Deployment | ⏳ Pending | 0% |

---

## 🛠️ Detailed Task Breakdown

### Milestone 1: Planning & Architecture Setup
- [x] Review project requirements and scope (`documents.txt` & PRD)
- [x] Harden `AndroidManifest.xml` security settings (`allowBackup=false`, `usesCleartextTraffic=false`, `INTERNET` permission)
- [x] Establish comprehensive enterprise documentation suite in `docs/` (14 detailed markdown documents)
- [x] Configure release build type in `build.gradle.kts` with R8 code shrinking and ProGuard optimization
- [x] Configure Git branches (`main` and `production`) and `.gitignore` secret exclusions

---

### Milestone 2: Clean Architecture Package Structure & Dependencies
- [x] Create `domain/`, `data/`, `presentation/`, and `di/` package hierarchy under `com.eliyas.fundmanagementapp`
- [x] Add Room Database dependencies (`androidx.room:room-runtime`, `androidx.room:room-ktx`)
- [x] Add Supabase SDK & Ktor Engine dependencies (`postgrest-kt`, `auth-kt`, `realtime-kt`, `ktor-client-android`)
- [x] Add Kotlinx Serialization & Coroutines dependencies (`kotlinx-serialization-json`, `kotlinx-coroutines-android`)

---

### Milestone 3: Domain Layer Models & Financial Calculation Engine
- [x] Create Pure Kotlin Domain Models (`Member`, `Contribution`, `Payment`, `ExtraDonation`, `Expense`, `Notice`, `FundAccount`)
- [x] Implement `ProcessPaymentUseCase.kt` (Monthly contribution vs. surplus donation split formula)
- [x] Implement `PartialPaymentUseCase.kt` (Status lifecycle: `UNPAID` $\rightarrow$ `PARTIAL` $\rightarrow$ `PAID`)
- [x] Implement `MultiMonthPaymentUseCase.kt` (Lump sum multi-month distribution algorithm)
- [x] Implement `GetMemberStatementUseCase.kt` (Bilingual annual contribution statement summary)
- [x] Write 100% Coverage Unit Tests for all Use Cases (`8 passed, 0 failed`)

---

### Milestone 4: Data Layer (Supabase PostgreSQL & Room DB)
- [x] Create Room Database `AppDatabase`, Entities (`MemberEntity`, `ContributionEntity`, `PaymentEntity`), and DAOs (`MemberDao`)
- [ ] Configure Supabase Client instance and Network DTO Mappers
- [x] Implement Repository interfaces (`MemberRepositoryImpl`, `ContributionRepositoryImpl`)

---

### Milestone 5: Presentation Layer (ViewModels, Compose UI & Bilingual Formatting)
- [x] Implement Bilingual Dictionary & Currency Formatter (`Strings.kt`, `CurrencyFormatter`)
- [ ] Implement Login, Role Selection & Password Change Screens
- [ ] Implement Member Home Dashboard & Individual Contribution Statement Screens
- [ ] Implement Admin & Manager Payment Recording & Approval Screens

---

### Milestone 6: Automated Testing, Security Hardening & Deployment
- [x] Write 100% Coverage Unit Tests for all Domain Use Cases (`app/src/test/`)
- [ ] Execute Room DAO In-Memory Instrumented Database Tests
- [ ] Execute release build verification (`gradlew assembleRelease`) and verify zero APK vulnerabilities
