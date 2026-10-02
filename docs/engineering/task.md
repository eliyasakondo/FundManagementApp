# 📋 Live Task Tracker & Milestone Roadmap
## Fund Management App (Android / Jetpack Compose)

---

> [!TIP]
> **Active Milestone:** **Milestone 2 — Clean Architecture Package Restructuring & Dependencies**
> **Current Sprint Goal:** Create `data/`, `domain/`, `presentation/`, and `di/` package hierarchy and configure Gradle dependencies.

---

## 🚩 Milestone Overview & Status

| Milestone | Title & Scope | Status | Progress |
| :--- | :--- | :--- | :--- |
| **Milestone 1** | Planning, Architecture & Enterprise Documentation Setup | ✅ Completed | 100% |
| **Milestone 2** | Clean Architecture Package Structure & Dependencies | 🚀 In Progress | 0% |
| **Milestone 3** | Domain Layer Models & Financial Calculation Engine | ⏳ Pending | 0% |
| **Milestone 4** | Data Layer (Supabase PostgreSQL, Room DB & Repositories) | ⏳ Pending | 0% |
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
- [ ] Create `domain/`, `data/`, `presentation/`, and `di/` package hierarchy under `com.eliyas.fundmanagementapp`
- [ ] Add Room Database dependencies (`androidx.room:room-runtime`, `androidx.room:room-compiler`, `room-ktx`)
- [ ] Add Supabase SDK dependencies (`io.github.jan-tennert.supabase:postgrest-kt`, `auth-kt`, `realtime-kt`)
- [ ] Add Google Hilt Dependency Injection dependencies (`com.google.dagger:hilt-android`)

---

### Milestone 3: Domain Layer Models & Financial Calculation Engine
- [ ] Create Pure Kotlin Domain Models (`Member`, `Contribution`, `Payment`, `ExtraDonation`, `Expense`)
- [ ] Implement `ProcessPaymentUseCase.kt` (Monthly contribution vs. surplus donation split formula)
- [ ] Implement `PartialPaymentUseCase.kt` (Status lifecycle: `UNPAID` $\rightarrow$ `PARTIAL` $\rightarrow$ `PAID`)
- [ ] Implement `MultiMonthPaymentUseCase.kt` (Lump sum multi-month distribution algorithm)

---

### Milestone 4: Data Layer (Supabase PostgreSQL & Room DB)
- [ ] Create Room Database `AppDatabase`, Entities (`MemberEntity`, `ContributionEntity`), and DAOs
- [ ] Configure Supabase Client instance and Network DTO Mappers
- [ ] Implement Repository interfaces (`MemberRepositoryImpl`, `ContributionRepositoryImpl`, `PaymentRepositoryImpl`)

---

### Milestone 5: Presentation Layer (ViewModels, Compose UI & Bilingual Formatting)
- [ ] Implement Bilingual Dictionary & Currency Formatter (`Strings.kt`, `CurrencyFormatter`)
- [ ] Implement Login, Role Selection & Password Change Screens
- [ ] Implement Member Home Dashboard & Individual Contribution Statement Screens
- [ ] Implement Admin & Manager Payment Recording & Approval Screens

---

### Milestone 6: Automated Testing, Security Hardening & Deployment
- [ ] Write 100% Coverage Unit Tests for all Domain Use Cases (`ProcessPaymentUseCaseTest`)
- [ ] Execute Room DAO In-Memory Instrumented Database Tests
- [ ] Execute release build verification (`gradlew assembleRelease`) and verify zero APK vulnerabilities
