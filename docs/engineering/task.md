# Live Task Tracker & Milestone Checklist

---

## Milestone 1: Planning & Architecture Setup
- [x] Review project requirements and scope (`documents.txt`)
- [x] Fix AndroidManifest.xml warnings and set up `production` Git branch
- [x] Establish comprehensive enterprise documentation suite in `docs/`
- [x] Configure modular AI Guardrails in `.cursor/rules/` (`general.mdc`, `architecture.mdc`, `backend.mdc`, `testing.mdc`)

---

## Milestone 2: Clean Architecture Package Structure
- [ ] Create `data/`, `domain/`, `presentation/`, and `di/` packages in `com.eliyas.fundmanagementapp`
- [ ] Configure dependencies in `build.gradle.kts` (Room DB, Supabase SDK, Coroutines, StateFlow, Compose M3)

---

## Milestone 3: Domain Layer & Financial Engine
- [ ] Implement Domain Models (`Member`, `Contribution`, `ExtraDonation`, `Expense`)
- [ ] Implement `ProcessPaymentUseCase.kt` (Extra donation routing & formula)
- [ ] Implement `PartialPaymentUseCase.kt` (Accumulator & status lifecycle)
- [ ] Implement `MultiMonthPaymentUseCase.kt` (Lump sum distribution)

---

## Milestone 4: Data Layer (Supabase & Room DB)
- [ ] Set up Room Database `AppDatabase`, Entities, and DAOs
- [ ] Set up Supabase Client and DTO mappers
- [ ] Implement Repositories (`MemberRepositoryImpl`, `ContributionRepositoryImpl`)

---

## Milestone 5: Presentation Layer (ViewModels & Compose UI)
- [ ] Implement Bilingual Strings & Number Formatter (`Strings.kt`, `CurrencyFormatter`)
- [ ] Implement Login, Password Change & Role Selection Screens
- [ ] Implement Member Home Dashboard & Contribution History Screens
- [ ] Implement Admin & Manager Payment Recording & Approval Screens

---

## Milestone 6: Testing & Production Deployment
- [ ] Write Unit Tests for all financial calculation Use Cases
- [ ] Run full build verification and deploy to device
